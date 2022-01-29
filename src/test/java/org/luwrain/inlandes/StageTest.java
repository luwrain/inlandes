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

public class StageTest extends Assert
{
    private final SyntaxParser p = new SyntaxParser();

    @Test public void positive()
    {
	final RuleStatement[] r = p.parse("RULE STAGE 5");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	assertEquals(5, r[0].getStageNum());
	assertFalse(r[0].isDefaultStageNum());
    }

    @Test public void negative()
    {
	final RuleStatement[] r = p.parse("RULE STAGE -5");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	assertEquals(-5, r[0].getStageNum());
	assertFalse(r[0].isDefaultStageNum());
    }

    @Test public void def()
    {
	final RuleStatement[] r = p.parse("RULE");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	assertEquals(0, r[0].getStageNum());
	assertTrue(r[0].isDefaultStageNum());
    }
}
