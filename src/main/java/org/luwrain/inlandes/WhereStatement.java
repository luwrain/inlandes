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

public final class WhereStatement
{
    public interface Matcher
    {
	boolean match(Token token);
    }

    public interface Item
    {
    }

    static public final class Fixed implements Item
    {
	private final Matcher matcher;
	private final String hint;
	public Fixed(Matcher matcher, String hint)
	{
	    if (matcher == null)
		throw new NullPointerException("matcher can't be null");
	    this.matcher = matcher;
	    this.hint = hint != null?hint:"";
	}
	@Override public String toString()
	{
	    return hint;
	}
    }

    public final List<Item> items;

    public WhereStatement(List<Item> items)
    {
	this.items = new ArrayList<>(items);
    }
}
