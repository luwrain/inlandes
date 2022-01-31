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

package org.luwrain.inlandes.operations;

import java.util.*;
import static java.util.Arrays.*;

import org.luwrain.inlandes.*;
import org.luwrain.inlandes.Matcher.Matching;
import static org.luwrain.inlandes.Token.*;

public final class Action extends Operation
{
    public final String text;

    public Action(String text)
    {
	if (text == null)
	    throw new NullPointerException("text can't be null");
	this.text = text;
    }

    private void exec(Token[] tokens, Matching matching, ScriptEngine scriptEngine)
    {
	scriptEngine.eval(text, scriptEngine.createBindings(tokens, matching));
    }

    @Override public Execution getExecution(Token[] tokens, Matching matching)
    {
	return new Execution(tokens, matching);
    }

    public final class Execution extends Operation.Execution
    {
	Execution(Token[] tokens, Matching matching) { super(tokens, matching); }
	@Override public org.luwrain.inlandes.Token exec(ScriptEngine scriptEngine)
	{
	    Action.this.exec(tokens, matching, scriptEngine);
	    return null;
	}
    }
}
