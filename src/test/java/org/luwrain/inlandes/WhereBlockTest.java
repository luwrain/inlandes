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

import org.luwrain.inlandes.WhereStatement.*;

public class WhereBlockTest extends Assert
{
    private final SyntaxParser p = new SyntaxParser();

    @Test public void emptyBlock()
    {
	final RuleStatement[] r = p.parse("RULE WHERE {}");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertEquals(0, b.items.length);
    }

        @Test public void emptyBlockWithSpace()
    {
	final RuleStatement[] r = p.parse("RULE WHERE{ }");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertEquals(0, b.items.length);
    }

            @Test public void coupleWords()
    {
	final RuleStatement[] r = p.parse("RULE WHERE{ иду домой }");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertEquals(2, b.items.length);
	final Item i1 = b.items[0], i2 = b.items[1];
	assertNotNull(i1);
	assertNotNull(i2);
	assertTrue(i1 instanceof Fixed);
	assertTrue(i2 instanceof Fixed);
	assertEquals("ИДУ", i1.toString());
		assertEquals("ДОМОЙ", i2.toString());
    }

                @Test public void coupleWordsAndOptional()
    {
	final RuleStatement[] r = p.parse("RULE WHERE{ иду домой }?");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertTrue(b.isOptional());
	assertEquals(2, b.items.length);
	final Item i1 = b.items[0], i2 = b.items[1];
	assertNotNull(i1);
	assertNotNull(i2);
	assertTrue(i1 instanceof Fixed);
	assertTrue(i2 instanceof Fixed);
	assertEquals("ИДУ", i1.toString());
		assertEquals("ДОМОЙ", i2.toString());
    }


                @Test public void coupleWordsWithRef()
    {
	final RuleStatement[] r = p.parse("RULE WHERE{ иду домой }_5");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertEquals(2, b.items.length);
	assertNotNull(b.getRef());
	assertEquals(5, b.getRef().num);
	final Item i1 = b.items[0], i2 = b.items[1];
	assertNotNull(i1);
	assertNotNull(i2);
	assertTrue(i1 instanceof Fixed);
	assertTrue(i2 instanceof Fixed);
	assertEquals("ИДУ", i1.toString());
		assertEquals("ДОМОЙ", i2.toString());
    }

                    @Test public void coupleWordsWithSpaceAndRef()
    {
	final RuleStatement[] r = p.parse("RULE WHERE{иду домой} _5");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertTrue(w.items[0] instanceof WhereStatement.Block);
	final Block b = (Block)w.items[0];
	assertEquals(2, b.items.length);
	assertNotNull(b.getRef());
	assertEquals(5, b.getRef().num);
	final Item i1 = b.items[0], i2 = b.items[1];
	assertNotNull(i1);
	assertNotNull(i2);
	assertTrue(i1 instanceof Fixed);
	assertTrue(i2 instanceof Fixed);
	assertEquals("ИДУ", i1.toString());
		assertEquals("ДОМОЙ", i2.toString());
    }





        @Test public void blockSpaces() { p.parse("RULE WHERE { домой }"); }
            @Test public void blockNoSpaces() { p.parse("RULE WHERE{домой}"); }
    @Test(expected = RuntimeException.class) public void blockUnbalancedRightError() { p.parse("RULE WHERE { иду домой }}"); }
        @Test(expected = RuntimeException.class) public void blockUnbalancedLeftError() { p.parse("RULE WHERE {{ иду домой }"); }
}
