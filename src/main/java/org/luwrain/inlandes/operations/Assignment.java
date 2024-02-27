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

package org.luwrain.inlandes.operations;

import java.util.*;
import static java.util.Arrays.*;

import org.luwrain.inlandes.*;
import org.luwrain.inlandes.Matcher.Matching;
import static org.luwrain.inlandes.Token.*;

public final class Assignment extends Operation
{
    public enum ValueType { STRING, JS };

    public final Ref ref;
public final ValueType valueType;
    public final String value;

    public Assignment(Ref ref, ValueType valueType, String value)
    {
	if (ref == null)
	    throw new NullPointerException("ref can't be null");
	if (valueType == null)
	    throw new NullPointerException("valueType can't be null");
	if (value == null)
	    throw new NullPointerException("value can't be null");
	this.ref = ref;
	this.valueType = valueType;
	this.value = value;
    }

    private Token exec(Token[] tokens, Matching matching, ScriptEngine scriptEngine)
    {
	switch(valueType)
	{
	case STRING:
	    return new TextToken(value);
	case JS:
	    return new ScriptObjectToken(scriptEngine.eval(value, scriptEngine.createBindings(tokens, matching)));
	default:
	    return null;
	}
    }

    @Override public Execution getExecution(Token[] tokens, Matching matching)
    {
	return new Execution(tokens, matching);
    }

    public final class Execution extends Operation.Execution
    {
	Execution(Token[] tokens, Matching matching)
	{
	    super(tokens, matching, matching.getRefBegin(ref.num), matching.getRefEnd(ref.num));
	}
	@Override public org.luwrain.inlandes.Token exec(ScriptEngine scriptEngine)
	{
	    return new ReplacementToken(tokens, rangeFrom , rangeTo, Assignment.this.exec(tokens, matching, scriptEngine));
	}
    }
}
