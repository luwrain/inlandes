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

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class WhereFixedTest
{
    private SyntaxParser p = null;

    @Test public void oneToTwoCyril()
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
    }

        @Test public void ThreeCyril()
    {
	final RuleStatement[] r = p.parse("RULE WHERE сижу на стуле");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	final WhereStatement w = r[0].getWhere();
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
	assertFalse(w.items[0].isOptional());
    }

                @Test public void oneLatinWithRefAndOptional()
    {
	RuleStatement[] r = p.parse("RULE WHERE 'Linux'_1?");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("LINUX", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
	assertTrue(w.items[0].isOptional());
    }

                    @Test public void oneDictWithRefAndOptional()
    {
	RuleStatement[] r = p.parse("RULE WHERE #test-dict-_1?");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		final WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("#test-dict-", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
	assertTrue(w.items[0].isOptional());
    }

                @Test public void onePuncWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE ','_1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals(",", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }

                    @Test public void numClassWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE \\num_1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("\\num", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }

                        @Test public void jsObjWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE @test _1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
		WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("@test", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }

    @Test public void lemmaCyrilWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE `дом`_1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("`дом`", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }

        @Test public void lemmaLatinWithRef()
    {
	RuleStatement[] r = p.parse("RULE WHERE `house`_1");
	assertNotNull(r);
	assertEquals(1, r.length);
	assertNotNull(r[0]);
	WhereStatement w = r[0].getWhere();
	assertNotNull(w);
	assertEquals(1, w.items.length);
	assertEquals("`house`", w.items[0].toString());
	assertNotNull(w.items[0].getRef());
	assertEquals(1, w.items[0].getRef().num);
    }

    @Test void twoCharsPuncError()
    {
	assertThrows(RuntimeException.class, ()->p.parse("RULE WHERE ',,'"));
    }

    @BeforeEach public void createParser()
    {
	final Lang lang = new Lang(){
		@Override public boolean isWordWithLemma(Token word, String lemma)
		{
		    return word.getText().toUpperCase().equals("ДОМУ") && lemma.toUpperCase().equals("ДОМ");
		}
	    };
	final HashMap<String, Set<String>> dicts = new HashMap<>();
	dicts.put("test-dict-", new HashSet<>());
	p = new SyntaxParser(new GraalVmEngine(), lang, dicts);
    }
}
