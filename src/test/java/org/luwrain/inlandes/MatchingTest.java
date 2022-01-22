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

import static java.util.Arrays.*;
import java.io.*;

import org.junit.*;

import org.luwrain.inlandes.Matcher.*;
import static org.luwrain.inlandes.Matcher.NO_REF;
import static org.luwrain.inlandes.util.Tokenizer.*;
import static org.luwrain.inlandes.util.Token.*;

public class MatchingTest extends Assert
{
    static private final org.luwrain.inlandes.util.Token[] TEXT = tokenize("Это был замечательный день весны, который создавал настроение и вдохновлял на прекрасное.");
    private SyntaxParser parser = null;

    @Test public void blockSingleWordWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE {день}_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.match(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("день", text(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
    }

    @Test public void nestedBlocksSingleWordWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE {{день}_1}_2");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.match(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	for(int i = 1; i< 3;i++)
	{
	    assertFalse(res[0].getRefBegin(i) == NO_REF);
	    assertFalse(res[0].getRefEnd(i) == NO_REF);
	    assertTrue(res[0].getRefEnd(i) > res[0].getRefBegin(i));
	    assertEquals("день", text(copyOfRange(TEXT, res[0].getRefBegin(i), res[0].getRefEnd(i))));
	}
    }

	    @Test public void alternativeSingleWordWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE (холодный прекрасный замечательный знойный)_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.match(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("замечательный", text(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
    }

    @Ignore @Test public void speed01()
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
	final Matching[] res = m.match(tokens);
	assertNotNull(res);
	
	System.out.println("proba " + tokens.length);
	
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


    
    @Before public void createParser()
    {
	parser = new SyntaxParser();
    }
}
