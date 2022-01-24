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

import org.junit.*;

public class WhereFixedTest extends Assert
{
    private SyntaxParser p = null;

    @Test public void oneToThreeCyril()
    {
	RuleStatement[] r = p.parse("RULE WHERE табуретка");
	WhereStatement w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("ТАБУРЕТКА", w.items[0].toString());

	r = p.parse("RULE WHERE иду домой");
	w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(2, w.items.length);
	assertEquals("ИДУ", w.items[0].toString());
	assertEquals("ДОМОЙ", w.items[1].toString());

	r = p.parse("RULE WHERE сижу на стуле");
	w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(3, w.items.length);
	assertEquals("СИЖУ", w.items[0].toString());
	assertEquals("НА", w.items[1].toString());
	assertEquals("СТУЛЕ", w.items[2].toString());
    }

        @Test public void oneLatin()
    {
	RuleStatement[] r = p.parse("RULE WHERE 'Linux'");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("LINUX", w.items[0].toString());
    }

            @Test public void oneLatinWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE 'Linux'_1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("LINUX", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }




    @Before public void createParser()
    {
	p = new SyntaxParser();
    }
}
