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

import org.graalvm.polyglot.*;

import org.luwrain.inlandes.Matcher.*;
import static org.luwrain.inlandes.Matcher.NO_REF;
import static org.luwrain.inlandes.util.Tokenizer.*;
import static org.luwrain.inlandes.Token.*;

public class GraalVmEngineTest
{
    private GraalVmEngine engine = null;

    
    @Test public void romanNum()
    {
	final Object res = engine.eval("Inlandes.getRomanNum('V')", new HashMap<>());
	assertNotNull(res);
	assertTrue(res instanceof Value);
	final Value value = (Value)res;
	assertTrue(value.isNumber());
	assertEquals(5, value.asInt());
	    }

    @BeforeEach public void createEngine()
    {
	engine = new GraalVmEngine();
    }

    @AfterEach public void closeEngine() throws Exception
    {
	engine.close();
    }
}
