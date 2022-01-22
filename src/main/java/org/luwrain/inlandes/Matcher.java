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

public final class Matcher
{
    private final RuleStatement[] rules;
    private List<WhereIterator> current = null, next = null;
    Token token = null;
    int tokenIndex = -1;

    public Matcher(RuleStatement[] rules)
    {
	if (rules == null)
	    throw new NullPointerException("rules can't be null");
	this.rules = rules.clone();
    }

    public void match(Token[] tokens)
    {

	this.current = new ArrayList<>();
	this.next = new ArrayList<>();
		List<WhereIterator> a = new ArrayList<>();
		System.out.println("proba");
	for(tokenIndex = 0;tokenIndex < tokens.length;tokenIndex++)
	{
	    this.token = tokens[tokenIndex];
	    System.out.println("Checking " + token);
	    	for(RuleStatement r: rules)
	    a.add(new WhereIterator(this, r));
	    while(!a.isEmpty())
	    {
		for(WhereIterator i: a)
		    i.check();
		a = this.current;
		System.out.println("it, " + a.size() + " items");
		this.current = new ArrayList<>();
	    }
	    a = this.next;
	    this.next = new ArrayList<>();
	}
	this.token = null;
	this.tokenIndex = -1;
	this.current = null;
	this.next = null;
    }

    void addCurrentPos(WhereIterator it)
    {
	this.current.add(it);
    }

    void addNextPos(WhereIterator it)
    {
	this.next.add(it);
    }

    void success(WhereIterator it, RuleStatement rule, int[] refsBegin, int[] refsEnd)
    {
	System.out.println("OK");
	System.out.println(Arrays.toString(refsBegin));
	System.out.println(Arrays.toString(refsEnd));
	
	
    }
}
