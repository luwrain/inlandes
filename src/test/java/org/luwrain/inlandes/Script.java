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

import org.graalvm.polyglot.*;

public final class Script implements AutoCloseable
{
    public final String text;
    private Context context = null;

    public Script(String text)
    {
	if (text == null)
	    throw new NullPointerException("text can't be null");
	this.text = text;
    }

    public Object eval(Bindings bindings)
    {
	close();
			final Engine engine = Engine.newBuilder()
	.option("engine.WarnInterpreterOnly", "false")
	.build();
	this.context = Context.newBuilder()
	.engine(engine)
	.allowExperimentalOptions(true)
	.build();
	if (bindings != null)
	    bindings.onBindings(context.getBindings("js"));
	return context.eval("js", text);
    }

    public Object eval()
    {
	return eval(null);
    }

    @Override public void close()
    {
	if (context == null)
	    return;
	context.close();
	context = null;
    }

    public interface Bindings
    {
	void onBindings(Object obj);
    }
}
