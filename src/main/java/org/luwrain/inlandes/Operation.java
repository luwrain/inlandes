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

import org.luwrain.inlandes.Matcher.Matching;

public abstract class Operation
{
    static public final int NO_REF = Matcher.NO_REF;

    abstract public Execution getExecution(Token[] tokens, Matching matching);

    static public abstract class Execution
    {
	protected final Token[] tokens;
	protected final Matching matching;
	public final int rangeFrom, rangeTo;
	public Execution(Token[] tokens, Matching matching, int rangeFrom, int rangeTo)
	{
	    this.tokens = tokens;
	    this.matching = matching;
	    this.rangeFrom = rangeFrom;
	    this.rangeTo = rangeTo;
	}
	public Execution(Token[] tokens, Matching matching)
	{
	    this(tokens, matching, NO_REF, NO_REF);
	}
	abstract public Token exec(ScriptEngine scriptEngine);
    }
}
