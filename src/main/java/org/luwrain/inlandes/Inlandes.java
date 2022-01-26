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
    private final Map<String, Set<String>> dicts = new HashMap<>();
    private final List<RuleStatement> rules = new ArrayList<>();
    private final SyntaxParser parser = new SyntaxParser();
    private final Context context;

    public Inlandes()
    {
		this.context = Context.newBuilder()
	.allowExperimentalOptions(true)
	.build();
    }

    public Token[] process(Token[] tokens)
    {
	final List<Token[]> history = new ArrayList<>();
	history.add(tokens);
	final Matcher m = new Matcher(rules.toArray(new RuleStatement[rules.size()]));
	final Matching[] matchings = handleCollisions(m.match(tokens));
	if (matchings.length == 0)
	    return history.get(history.size() - 1);
	history.add(processMatchings(history.get(history.size() - 1), matchings));
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
	final List<Assignment.Execution> assignments = new ArrayList<>();
	for(Matching m: matchings)
	    for(Operation o: m.getRule().operations)
		if (o instanceof Assignment)
		{
		    final Assignment a = (Assignment)o;
		    assignments.add(a.getExecution(m));
		}
	final Token[] t = tokens.clone();
	int numRemoved = 0;
	for(Assignment.Execution a: assignments)
	{
	    if (a.rangeFrom == a.rangeTo)//Should never happen
		continue;
	    t[a.rangeFrom] = a.exec(context);
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
		rules.addAll(Arrays.asList(parser.parse(rulesText)));
    }

    public void loadFile(String fileName, String charset) throws IOException
    {
	rules.addAll(Arrays.asList(parser.parse(readTextFile(fileName, charset))));
    }

    public void loadFile(String fileName) throws IOException
    {
	loadFile(fileName, "UTF-8");
    }

    @Override public void close()
    {
	this.context.close();
    }

    public int getRuleCount()
    {
	return rules.size();
    }

    public RuleStatement getRule(int index)
    {
	return rules.get(index);
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
