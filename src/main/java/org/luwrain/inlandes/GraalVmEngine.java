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

import java.util.*;
import org.graalvm.polyglot.*;
import org.luwrain.inlandes.operations.*;

public class GraalVmEngine implements ScriptEngine
{
    	private final Context context;

    public GraalVmEngine()
    {
		this.context = Context.newBuilder()
	.allowExperimentalOptions(true)
	.build();
    }

    @Override public Object eval(String text, Map<String, Object> bindings)
    {
	final Value b = 		context.getBindings("js");
	try {
	    for(Map.Entry<String, Object> e: bindings.entrySet())
		b.putMember(e.getKey(), e.getValue());
	    return context.eval("js", text);
	}
	finally {
	    for(Map.Entry<String, Object> e: bindings.entrySet())
		b.removeMember(e.getKey());
	}
    }

    @Override public void close()
    {
	context.close();
    }

        @Override public boolean isObjWithTrueValue(Object obj, String valueName)
    {
	if (obj == null)
	    throw new NullPointerException("obj can't be null");
	if (valueName == null)
	    throw new NullPointerException("valueName can't be null");
	if (valueName.isEmpty())
	    throw new IllegalArgumentException("valueName can't be empty");
	if (obj instanceof Value)
	{
	    final Value value = (Value)obj;
	    final Value v = value.getMember(valueName);
	    if (v == null || v.isNull() || !v.isBoolean())
		return false;
	    return v.asBoolean();
	}
	if (obj instanceof ScriptObjectToken)
	{
	    final ScriptObjectToken token = (ScriptObjectToken)obj;
	    return this.isObjWithTrueValue(token.obj, valueName);
	}
	if (obj instanceof ReplacementToken)
	{
	    final ReplacementToken token = (ReplacementToken)obj;
	    return this.isObjWithTrueValue(token.token, valueName);
	}
	return false;
    }

        @Override public Object createBindingObj(Token token)
    {
	if (token instanceof ScriptObjectToken)
	    return (Value)((ScriptObjectToken)token).obj;
	if (token instanceof ReplacementToken)
	    return createBindingObj(((ReplacementToken)token).token);
	return token.getText();
    }
}
