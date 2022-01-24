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

    public void process(Token[] tokens)
    {
	final Matcher m = new Matcher(rules.toArray(new RuleStatement[rules.size()]));
	m.match(tokens);
	
    }

    public void process(String text)
    {
	process(tokenize(text));
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
