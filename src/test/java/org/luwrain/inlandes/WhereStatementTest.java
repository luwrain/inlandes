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

public class WhereStatementTest extends Assert
{
    private final SyntaxParser p = new SyntaxParser();

    @Test public void oneToThreeCyril()
    {
	RuleStatement[] r = p.parse("RULE WHERE табуретка");
	WhereStatement w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.size());
	assertEquals("ТАБУРЕТКА", w.items.get(0).toString());

	r = p.parse("RULE WHERE иду домой");
	w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(2, w.items.size());
	assertEquals("ИДУ", w.items.get(0).toString());
	assertEquals("ДОМОЙ", w.items.get(1).toString());

	r = p.parse("RULE WHERE сижу на стуле");
	w = null;
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(3, w.items.size());
	assertEquals("СИЖУ", w.items.get(0).toString());
	assertEquals("НА", w.items.get(1).toString());
	assertEquals("СТУЛЕ", w.items.get(2).toString());
    }

        @Test public void blockSpaces() { p.parse("RULE WHERE { домой }"); }
            @Test public void blockNoSpaces() { p.parse("RULE WHERE{домой}"); }
    @Test(expected = RuntimeException.class) public void blockUnbalancedRightError() { p.parse("RULE WHERE { иду домой }}"); }
        @Test(expected = RuntimeException.class) public void blockUnbalancedLeftError() { p.parse("RULE WHERE {{ иду домой }"); }
}
