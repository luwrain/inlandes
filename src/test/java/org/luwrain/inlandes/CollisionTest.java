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

public class CollisionTest extends Assert
{
    static private final Token[] TEXT = tokenize("Это был замечательный день весны, который создавал настроение и вдохновлял на прекрасное.");
    private SyntaxParser parser = null;

    @Test public void overlapping()
    {
	final RuleStatement[] rr = parser.parse(
						"RULE WHERE день . весны" +
						"RULE WHERE замечательный . день");
	assertNotNull(rr);
	assertEquals(2, rr.length);
	assertNotNull(rr[0]);
		assertNotNull(rr[1]);
		final Matching[] res = new Matcher(rr).matchAsArray(TEXT);
	assertNotNull(res);
	assertEquals(2, res.length);
	assertEquals(3, res[0].len);
	assertEquals(3, res[1].len);
	assertEquals("замечательный день", concat(copyOfRange(TEXT, res[0].getRefBegin(0), res[0].getRefEnd(0))));
		assertEquals("день весны", concat(copyOfRange(TEXT, res[1].getRefBegin(0), res[1].getRefEnd(0))));
		assertTrue(res[0].overlaps(res[1]));
				assertTrue(res[1].overlaps(res[0]));
    }


    @Before public void createParser()
    {
	parser = new SyntaxParser();
    }
}
