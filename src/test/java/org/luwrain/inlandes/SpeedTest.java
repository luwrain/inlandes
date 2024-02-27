/*
 * Copyright 2021-2024 Michael Pozhidaev <msp@luwrain.org>
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

import static java.util.Arrays.*;
import java.io.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.inlandes.Matcher.*;
import static org.luwrain.inlandes.util.Tokenizer.*;
import static org.luwrain.inlandes.util.Token.*;

public class SpeedTest
{
    private SyntaxParser parser = null;

    @Test public void speed01()
    {
	final String text = readResource("speed01.txt");
	if (text == null)
	    return;
	final Token[] tokens = tokenize(text);
	final RuleStatement[] rr = parser.parse("RULE WHERE (холодный прекрасный замечательный знойный)_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final long startTime = System.currentTimeMillis();
	final Matching[] res = m.matchAsArray(tokens);
	final long elapsed = System.currentTimeMillis() - startTime;
	assertNotNull(res);
	System.out.println("Processed " + String.valueOf(tokens.length) + " tokens in " + String.valueOf(elapsed) + "ms, " + String.valueOf(res.length) + " matching(s)");
    }

    private String readResource(String resName)
    {
	try {
	    final InputStream is = getClass().getResourceAsStream(resName);
	    if (is == null)
		return null;
	    try (final BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
		final StringBuilder b = new StringBuilder();
		String line = r.readLine();
		while (line != null)
		{
		    b.append(line).append(System.lineSeparator());
		    line = r.readLine();
		}
		return new String(b);
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @BeforeEach public void createParser()
    {
	parser = new SyntaxParser();
    }
}
