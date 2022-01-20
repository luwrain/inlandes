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

public final class Script implements AutoCloseable
{
    private final Bindings bindings;
    private Context context = null;

    Script(Bindings bindings)
    {
	this.bindings = bindings;
    }

    void run(String text)
    {
	close();
	this.context = Context.newBuilder()
	.allowExperimentalOptions(true)
	.build();
	if (bindings != null)
	    bindings.onBindings(context.getBindings("js"));
	context.eval("js", text);
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
