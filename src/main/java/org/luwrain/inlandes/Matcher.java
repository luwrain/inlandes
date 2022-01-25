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
        static public final int NO_REF = -1;

static public final class Matching
{
    private final RuleStatement rule;
    private final int[] refsBegin, refsEnd;
    final int rangeFrom, rangeTo, len;
    Matching(RuleStatement rule, int[] refsBegin, int[] refsEnd)
    {
	if (rule == null)
	    throw new NullPointerException("rule can't be null");
	this.rule = rule;
	this.refsBegin = refsBegin.clone();
	this.refsEnd = refsEnd.clone();
	this.rangeFrom = refsBegin[0];
	this.rangeTo = refsEnd[0];
	this.len = rangeTo - rangeFrom;
	if (len < 0)
	    throw new IllegalArgumentException("Illegal range of the matching");
    }
    public RuleStatement getRule()
    {
	return this.rule;
    }
    public int getRefBegin(int refIndex)
    {
	if (refIndex < 0)
	    throw new IllegalArgumentException("refIndex can't be negative");
	if (refIndex >= 10)
	    throw new IllegalArgumentException("refIndex can't be greater than 9");
	return this.refsBegin[refIndex];
    }
        public int getRefEnd(int refIndex)
    {
	if (refIndex < 0)
	    throw new IllegalArgumentException("refIndex can't be negative");
	if (refIndex >= 10)
	    throw new IllegalArgumentException("refIndex can't be greater than 9");
	return this.refsEnd[refIndex];
    }
    public boolean overlaps(Matching m)
    {
	if (len == 0 || m.len == 0)
	    return false;
	if (inside(m.rangeFrom) || inside(m.rangeTo - 1))
	    return true;
	if (m.inside(rangeFrom) || m.inside(rangeTo - 1))
	    return true;
	return false;
    }
    private boolean inside(int pos)
	{
	    return pos >= rangeFrom && pos < rangeTo;
	 }
}

    private final RuleStatement[] rules;
    private final List<Matching> matchings = new ArrayList<>();
    private ArrayList<WhereIterator> current = null, next = null;
    Token token = null;
    int tokenIndex = -1;

    public Matcher(RuleStatement[] rules)
    {
	if (rules == null)
	    throw new NullPointerException("rules can't be null");
	this.rules = rules.clone();
    }

    public List<Matching> match(Token[] tokens)
    {
	this.matchings.clear();
	this.current = new ArrayList<>();
	this.next = new ArrayList<>();
		List<WhereIterator> a = new ArrayList<>();
	for(tokenIndex = 0;tokenIndex < tokens.length;tokenIndex++)
	{
	    this.token = tokens[tokenIndex];
	    //System.out.println("Checking " + token);
	    	for(RuleStatement r: rules)
	    a.add(new WhereIterator(this, r));
	    while(!a.isEmpty())
	    {
		for(WhereIterator i: a)
		    i.check();
		a = this.current;
		//System.out.println("it, " + a.size() + " items");
		this.current = new ArrayList<>();
		this.current.ensureCapacity(a.size());
	    }
	    a = this.next;
	    this.next = new ArrayList<>();
	    this.next.ensureCapacity(a.size());
	}
	this.token = null;
	this.tokenIndex = -1;
	this.current = null;
	this.next = null;
	return this.matchings;
    }

    public Matching[] matchAsArray(Token[] tokens)
    {
	final List<Matching> res = match(tokens);
	return res.toArray(new Matching[res.size()]);
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
	refsEnd[0] = tokenIndex;
	matchings.add(new Matching(rule, refsBegin, refsEnd));
    }
}
