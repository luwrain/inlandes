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
	Ref getRef();
    }

        static public final class Block implements Item
	{
	    final Item[] items;
	    final Ref ref;
	    Block(List<Item> items, Ref ref)
	    {
		this.items = items.toArray(new Item[items.size()]);
		this.ref = ref;
	    }
	    Block(List<Item> items) { this(items, null); }
	    @Override public Ref getRef() { return ref; }
	}

            static public final class Alternative implements Item
	{
	    final Item[] items;
	    final Ref ref;
	    Alternative(List<Item> items, Ref ref)
	    {
		this.items = items.toArray(new Item[items.size()]);
		this.ref = ref;
	    }
	    Alternative(List<Item> items) { this(items, null); }
	    @Override public Ref getRef() { return ref; }
	}

    static public final class Fixed implements Item
    {
	final Matcher matcher;
	final String hint;
	final Ref ref;
	Fixed(Matcher matcher, String hint, Ref ref)
	{
	    if (matcher == null)
		throw new NullPointerException("matcher can't be null");
	    this.matcher = matcher;
	    this.hint = hint != null?hint:"";
	    this.ref = ref;
	}
	//	public boolean match(Token token) { return matcher.match(token); }
	@Override public Ref getRef() { return ref; }
	@Override public String toString()
	{
	    return hint;
	}
    }

    final Item[] items ;

    public WhereStatement(List<Item> items)
    {
	this.items = items.toArray(new Item[items.size()]);
    }
}
