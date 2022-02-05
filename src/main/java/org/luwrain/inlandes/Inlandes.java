/*
 * Copyright 2021-2022 Michael Pozhidaev <msp@luwrain.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.luwrain.inlandes;

import java.util.*;
import static java.util.Arrays.*;
import java.io.*;

import org.graalvm.polyglot.*;

import org.luwrain.inlandes.Matcher.Matching;
import org.luwrain.inlandes.operations.*;
import static org.luwrain.inlandes.util.Tokenizer.tokenize;

public final class Inlandes implements AutoCloseable
{
    static private final int NO_REF = Matcher.NO_REF;

    private final Map<String, Set<String>> dicts = new HashMap<>();
    private final SortedMap<Integer, List<RuleStatement>> rules = new TreeMap<>();
    private final List<RuleStatement> rulesList = new ArrayList<>();
    private final SyntaxParser parser;
    private final ScriptEngine scriptEngine;
    private final Lang lang;
    private int currentStageNum = 0;

    public Inlandes(ScriptEngine scriptEngine, Lang lang)
    {
	if (scriptEngine == null)
	    throw new NullPointerException("scriptEngine can't be null");
	if (lang == null)
	    throw new NullPointerException("lang can't be null");
	this.scriptEngine = scriptEngine;
	this.lang = lang;
	this.parser = new SyntaxParser(scriptEngine, lang, dicts);
    }

    public Inlandes()
    {
	this(new GraalVmEngine(), new DictLang());
    }

    public Token[] process(Token[] tokens)
    {
	final List<Token[]> history = new ArrayList<>();
	history.add(tokens);
	for(Map.Entry<Integer, List<RuleStatement>> e: rules.entrySet())
	{
	    final Matcher m = new Matcher(e.getValue().toArray(new RuleStatement[e.getValue().size()]));
	    final Token[] t = history.get(history.size() - 1);
	    final Matching[] matchings = handleCollisions(m.match(t));
	    if (matchings.length == 0)
		continue;
	    history.add(processMatchings(t, matchings));
	}
	return history.get(history.size() - 1);
    }

    public Token[] process(String text)
    {
	return process(tokenize(text));
    }

    private Matching[] handleCollisions(List<Matching> m)
    {
	for(int i = 0;i < m.size();i++)
	    for(int j = i + 1;j < m.size();j++)
	    {
		final Matching mi = m.get(i), mj = m.get(j);
		if (mi.overlaps(mj))
		{
		    if (mj.len < mi.len)
		    {
			m.set(j, null);
			continue;
		    }
		    m.set(i, null);
		    j = m.size();
		}
	    }
	m.removeIf((i)->(i == null));
	return m.toArray(new Matching[m.size()]);
    }

    private Token[] processMatchings(Token[] tokens, Matching[] matchings)
    {
	final List<Operation.Execution> execs = new ArrayList<>();
	for(Matching m: matchings)
	    for(Operation o: m.getRule().operations)
		execs.add(o.getExecution(tokens, m));
	final Token[] t = tokens.clone();
	int numRemoved = 0;
	for(Operation.Execution a: execs)
	{
	    if (a.rangeFrom == NO_REF || a.rangeTo == NO_REF)
	    {
		a.exec(scriptEngine);
		continue;
	    }
	    if (a.rangeFrom == a.rangeTo)//Should never happen
		continue;
	    t[a.rangeFrom] = a.exec(scriptEngine);
	    for(int i = a.rangeFrom + 1;i < a.rangeTo;i++)
		t[i] = null;
	    numRemoved += (a.rangeTo - a.rangeFrom);
	    if (t[a.rangeFrom] != null)
		numRemoved--;
	}
	final ArrayList<Token> res = new ArrayList<>();
	res.ensureCapacity(t.length - numRemoved);
	for(Token tt: t)
	    if (tt != null)
		res.add(tt);
	return res.toArray(new Token[res.size()]);
    }

    public void addDict(String dictName, List<String> items)
    {
	if (dictName == null)
	    throw new NullPointerException("dictName can't be null");
	if (dictName.trim().isEmpty())
	    throw new IllegalArgumentException("dictName can't be empty");
	dicts.put(dictName.trim(), new HashSet(items));
    }

    public void loadStandardLibrary()
    {
	try {
	    dicts.put("roman", new HashSet<>(asList(readTextResourceAsArray("roman.dict", "UTF-8"))));
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    public void loadRules(String rulesText)
    {
	final RuleStatement[] rr = parser.parse(rulesText);
	rulesList.addAll(asList(rr));
	for(RuleStatement r: rr)
	{
	    if (r.isDefaultStageNum())
		r.setStageNum(currentStageNum);
	    currentStageNum = r.getStageNum();
	    final Integer num = new Integer(currentStageNum);
	    List<RuleStatement> l = rules.get(num);
	    if (l != null)
	    {
		l.add(r);
		continue;
	    }
	    l = new ArrayList<>();
	    l.add(r);
	    rules.put(num , l);
	}
    }

    public void loadRulesFromFile(String fileName, String charset) throws IOException
    {
	loadRules(readTextFile(fileName, charset));
    }

    public void loadRulesFromFile(String fileName) throws IOException
    {
	loadRulesFromFile(fileName, "UTF-8");
    }

    @Override public void close() throws Exception
    {
	this.scriptEngine.close();
    }

    public int getStageCount()
    {
	return rules.size();
    }

    public int getRuleCount()
    {
	return rulesList.size();
    }

    public RuleStatement getRule(int index)
    {
	return rulesList.get(index);
    }

    private String readTextFile(String fileName, String charset) throws IOException
    {
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset))){
	    String line = r.readLine();
	    final StringBuilder b = new StringBuilder();
	    while (line != null)
	    {
		if (!line.trim().isEmpty() && line.trim().charAt(0) != '#')
		b.append(line);
		b.append(System.lineSeparator());
		line = r.readLine();
	    }
	    return new String(b);
	}
    }

        private String[] readTextFileAsArray(String fileName, String charset) throws IOException
    {
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset))){
	    String line = r.readLine();
	    final List<String> res = new ArrayList<>();
	    while (line != null)
	    {
		if (!line.trim().isEmpty() && line.trim().charAt(0) != '#')
		    res.add(line);
		line = r.readLine();
	    }
	    return res.toArray(new String[res.size()]);
	}
    }

            private String[] readTextResourceAsArray(String resName, String charset) throws IOException
    {
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resName), charset))){
	    String line = r.readLine();
	    final List<String> res = new ArrayList<>();
	    while (line != null)
	    {
		if (!line.trim().isEmpty() && line.trim().charAt(0) != '#')
		    res.add(line);
		line = r.readLine();
	    }
	    return res.toArray(new String[res.size()]);
	}
    }
}
