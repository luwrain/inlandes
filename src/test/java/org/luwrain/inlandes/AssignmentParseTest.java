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

import org.graalvm.polyglot.*;
import org.junit.*;

import org.luwrain.inlandes.operations.*;

public class AssignmentParseTest extends Assert
{
    private SyntaxParser p = null;

    @Test public void strEmpty()
    {
	final RuleStatement[] r = p.parse("RULE WHERE иду DO _1 = \"\";");
	assertEquals(1, r.length);
	assertEquals(1, r[0].operations.size());
	final Operation op = r[0].operations.get(0);
	assertNotNull(op);
	assertTrue(op instanceof Assignment);
	final Assignment a = (Assignment)op;
	assertNotNull(a.ref);
	assertNotNull(a.valueType);
	assertNotNull(a.value);
	assertEquals(1, a.ref.num);
	assertEquals(Assignment.ValueType.STRING, a.valueType);
	assertEquals("", a.value);
    }

    @Test public void str()
    {
	final RuleStatement[] r = p.parse("RULE WHERE иду DO _1 = \"proba\";");
	assertEquals(1, r.length);
	assertEquals(1, r[0].operations.size());
	final Operation op = r[0].operations.get(0);
	assertNotNull(op);
	assertTrue(op instanceof Assignment);
	final Assignment a = (Assignment)op;
	assertNotNull(a.ref);
	assertNotNull(a.valueType);
	assertNotNull(a.value);
	assertEquals(1, a.ref.num);
	assertEquals(Assignment.ValueType.STRING, a.valueType);
	assertEquals("proba", a.value);
    }

    @Test public void jsEmpty()
    {
	final RuleStatement[] r = p.parse("RULE WHERE иду DO _1 = ``{}``;");
	assertEquals(1, r.length);
	assertEquals(1, r[0].operations.size());
	final Operation op = r[0].operations.get(0);
	assertNotNull(op);
	assertTrue(op instanceof Assignment);
	final Assignment a = (Assignment)op;
	assertNotNull(a.ref);
	assertNotNull(a.valueType);
	assertNotNull(a.value);
	assertEquals(1, a.ref.num);
	assertEquals(Assignment.ValueType.JS, a.valueType);
	assertEquals("{}", a.value);
    }

    @Test public void jsString()
    {
	final RuleStatement[] r = p.parse("RULE WHERE иду DO _1 = ``'proba'``;");
	assertEquals(1, r.length);
	assertEquals(1, r[0].operations.size());
	final Operation op = r[0].operations.get(0);
	assertNotNull(op);
	assertTrue(op instanceof Assignment);
	final Assignment a = (Assignment)op;
	assertNotNull(a.ref);
	assertNotNull(a.valueType);
	assertNotNull(a.value);
	assertEquals(1, a.ref.num);
	assertEquals(Assignment.ValueType.JS, a.valueType);
	assertEquals("'proba'", a.value);
	try (final Script s = new Script(a.value)) {
	    final Object res = s.eval();
	    assertNotNull(res);
	    assertTrue(res instanceof Value);
	    final Value v = (Value)res;
	    assertFalse(v.isNull());
	    assertTrue(v.isString());
	    assertEquals("proba", v.asString());
	}
    }

    @Test public void jsStringComplex()
    {
	final RuleStatement[] r = p.parse("RULE WHERE иду DO _1 = ``var r = ''; r += 'proba2'; r``;");
	assertEquals(1, r.length);
	assertEquals(1, r[0].operations.size());
	final Operation op = r[0].operations.get(0);
	assertNotNull(op);
	assertTrue(op instanceof Assignment);
	final Assignment a = (Assignment)op;
	assertNotNull(a.ref);
	assertNotNull(a.valueType);
	assertNotNull(a.value);
	assertEquals(1, a.ref.num);
	assertEquals(Assignment.ValueType.JS, a.valueType);
	assertEquals("var r = ''; r += 'proba2'; r", a.value);
	try (final Script s = new Script(a.value)) {
	    final Object res = s.eval();
	    assertNotNull(res);
	    assertTrue(res instanceof Value);
	    final Value v = (Value)res;
	    assertFalse(v.isNull());
	    assertTrue(v.isString());
	    assertEquals("proba2", v.asString());
	}
    }

    @Before public void createParser()
    {
	this.p = new SyntaxParser();
    }
}
