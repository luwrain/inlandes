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
import static org.luwrain.inlandes.Token.*;

public class MatchingTest extends Assert
{
    static private final org.luwrain.inlandes.Token[] TEXT = tokenize("Это был замечательный день весны, который создавал настроение и вдохновлял на прекрасное.");
    private SyntaxParser parser = null;

    @Test public void fixed()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE день");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(0) == NO_REF);
	assertFalse(res[0].getRefEnd(0) == NO_REF);
	assertTrue(res[0].getRefEnd(0) > res[0].getRefBegin(01));
	assertEquals("день", concat(copyOfRange(TEXT, res[0].getRefBegin(0), res[0].getRefEnd(0))));
    }

    @Test public void fixedWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE день_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("день", concat(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
    }

    @Test public void blockSingleWordWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE {день}_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("день", concat(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
    }

    @Test public void optionalSpace()
    {
	final Token[]
	text1 = tokenize("Это было давно, т. к. это было в прошлом году."),
	text2 = tokenize("Это было давно, т.к. это было в прошлом году.");
	final RuleStatement[] rr = parser.parse("RULE WHERE т '.' .? к '.'");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(5, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[]
	res1 = m.matchAsArray(text1),
	res2 = m.matchAsArray(text2);

	assertNotNull(res1);
	assertEquals(1, res1.length);
	assertEquals(res1[0].getRule(), rr[0]);
	assertFalse(res1[0].getRefBegin(0) == NO_REF);
	assertFalse(res1[0].getRefEnd(0) == NO_REF);
	assertTrue(res1[0].getRefEnd(0) > res1[0].getRefBegin(0));
	assertEquals("т. к.", concat(copyOfRange(text1, res1[0].getRefBegin(0), res1[0].getRefEnd(0))));

	assertNotNull(res2);
	assertEquals(1, res2.length);
	assertEquals(res2[0].getRule(), rr[0]);
	assertFalse(res2[0].getRefBegin(0) == NO_REF);
	assertFalse(res2[0].getRefEnd(0) == NO_REF);
	assertTrue(res2[0].getRefEnd(0) > res2[0].getRefBegin(0));
	assertEquals("т.к.", concat(copyOfRange(text2, res2[0].getRefBegin(0), res2[0].getRefEnd(0))));
    }


    @Test public void blockThreeWordsWithRef()
    {
	final RuleStatement[] rr = parser.parse("RULE WHERE {был . замечательный . день}_1");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("был замечательный день", concat(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
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
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	for(int i = 1; i< 3;i++)
	{
	    assertFalse(res[0].getRefBegin(i) == NO_REF);
	    assertFalse(res[0].getRefEnd(i) == NO_REF);
	    assertTrue(res[0].getRefEnd(i) > res[0].getRefBegin(i));
	    assertEquals("день", concat(copyOfRange(TEXT, res[0].getRefBegin(i), res[0].getRefEnd(i))));
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
	final Matching[] res = m.matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertFalse(res[0].getRefBegin(1) == NO_REF);
	assertFalse(res[0].getRefEnd(1) == NO_REF);
	assertTrue(res[0].getRefEnd(1) > res[0].getRefBegin(1));
	assertEquals("замечательный", concat(copyOfRange(TEXT, res[0].getRefBegin(1), res[0].getRefEnd(1))));
    }

        @Test public void lastToken()
    {
	final Token[] text = tokenize("Томск Лондон");
	final RuleStatement[] rr = parser.parse("RULE WHERE Лондон");
	assertNotNull(rr);
	assertEquals(1, rr.length);
	assertNotNull(rr[0]);
	assertNotNull(rr[0].getWhere());
	assertEquals(1, rr[0].getWhere().items.length);
	final Matcher m = new Matcher(rr);
	final Matching[] res = m.matchAsArray(text);
	assertNotNull(res);
	assertEquals(1, res.length);
	assertEquals(res[0].getRule(), rr[0]);
	assertEquals("Лондон", concat(copyOfRange(text, res[0].getRefBegin(0), res[0].getRefEnd(0))));
    }


    @Before public void createParser()
    {
	parser = new SyntaxParser();
    }
}
