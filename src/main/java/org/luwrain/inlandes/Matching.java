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

public final class Matching
{
    private final RuleStatement[] rules;
    private List<WhereIterator> current = null, next = null;
    Token token = null;

    public Matching(RuleStatement[] rules)
    {
	if (rules == null)
	    throw new NullPointerException("rules can't be null");
	this.rules = rules.clone();
    }

    public void match(Token[] tokens)
    {
	List<WhereIterator> a = new ArrayList<>();
	for(RuleStatement r: rules)
	    a.add(new WhereIterator(this, r.getWhere()));
	this.current = new ArrayList<>();
	this.next = new ArrayList<>();
	for(Token t: tokens)
	{
	    this.token = t;
	    while(!a.isEmpty())
	    {
		for(WhereIterator i: a)
		    i.check();
		a = this.current;
		this.current = new ArrayList<>();
	    }
	    a = this.next;
	    this.next = new ArrayList<>();
	}
    }

    void addCurrentPos(WhereIterator it)
    {
	this.current.add(it);
    }

    void addNextPos(WhereIterator it)
    {
	this.next.add(it);
    }

    void success(WhereIterator it)
    {
	
    }
}
