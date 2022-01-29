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

import static org.luwrain.inlandes.Token.*;
import static org.luwrain.inlandes.util.Tokenizer.*;

public class ExecTest extends Assert
{
    private Inlandes inlandes = null;

    @Test public void substOneTokenWithRef()
    {
	inlandes.loadRules("RULE WHERE ноутбуке_1 DO _1 = \"смартфоне\";");
	assertEquals(1, inlandes.getRuleCount());
	assertNotNull(inlandes.getRule(0));
	final Token[] res = inlandes.process("В том ноутбуке был странный процессор.");
	assertNotNull(res);
	assertEquals(12, res.length);
	assertEquals("В", res[0].getText());
	assertEquals("том", res[2].getText());
	assertEquals("процессор", res[10].getText());
	assertNotNull(res[4]);
	assertEquals("смартфоне", res[4].getText());
	assertEquals("В том смартфоне был странный процессор.", concat(res));
    }

        @Test public void substEpoch()
    {
	inlandes.loadRules("RULE WHERE до . н '.' . э '.' DO _0 = \"до нашей эры\";");
	assertEquals(1, inlandes.getRuleCount());
	assertNotNull(inlandes.getRule(0));
	final Token[] res = inlandes.process("События II в. до н. э. драматичны.");
	assertNotNull(res);
	assertEquals(11, res.length);
	assertEquals("События II в. до нашей эры драматичны.", concat(res));
    }


    @Test public void substEpochWithRef()
    {
	inlandes.loadRules("RULE WHERE {до . н '.' . э '.'}_1 DO _1 = \"до нашей эры\";");
	assertEquals(1, inlandes.getRuleCount());
	assertNotNull(inlandes.getRule(0));
	final Token[] res = inlandes.process("События II в. до н. э. драматичны.");
	assertNotNull(res);
	assertEquals(11, res.length);
	assertEquals("События II в. до нашей эры драматичны.", concat(res));
    }

        @Test public void jsObj()
    {
	inlandes.loadRules("RULE WHERE до . н '.' . э '.' DO _0 = ``({name: 'jstest'})``;");
	assertEquals(1, inlandes.getRuleCount());
	assertNotNull(inlandes.getRule(0));
	final Token[] res = inlandes.process("События II в. до н. э. драматичны.");
	assertNotNull(res);
	assertEquals(11, res.length);
	assertEquals("События II в. {name: \"jstest\"} драматичны.", concat(res));
    }


    @Before public void createInlandes()
    {
	inlandes = new Inlandes();
    }

    @After public void closeInlandes() throws Exception
    {
	inlandes.close();
    }
}
