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

import org.luwrain.inlandes.*;
import org.luwrain.inlandes.Matcher.Matching;

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

    public Result perform(Matching matching)
    {
final ReplacementToken newToken = new ReplacementToken(matching.getRefBegin(ref.num), matching.getRefEnd(ref.num), org.luwrain.inlandes.util.Token.latin("TEST"));
return new Result(newToken);
    }


    static public final class Result
    {
	final ReplacementToken newToken;
	Result(ReplacementToken newToken)
	{
	    this.newToken = newToken;
	}
    }
}
