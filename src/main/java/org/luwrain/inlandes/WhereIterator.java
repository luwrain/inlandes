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

import org.luwrain.inlandes.WhereStatement.*;

public final class WhereIterator
{
    static private final int NO_REF = -1;

        private final Matching matching;
    private final ArrayList<Level> levels = new ArrayList<>();
    private final int[] refsBegin, refsEnd;

    public WhereIterator(Matching matching, WhereStatement whereStatement)
    {
	if (matching == null)
	    throw new NullPointerException("matching can't be null");
	if (whereStatement == null)
	    throw new NullPointerException("whereStatement can't be null");
	this.matching = matching;
	this.levels.add(new Level(whereStatement.items));
	this.refsBegin = new int[10];
		this.refsEnd = new int[10];
		Arrays.fill(refsBegin, NO_REF);
		Arrays.fill(refsEnd, NO_REF);
    }

    WhereIterator(WhereIterator it)
    {
	if (it == null)
	    throw new NullPointerException("it can't be null");
	if (it.matching == null)
	    throw new NullPointerException("it.matching can't be null");
	this.matching = it.matching;
	this.levels.ensureCapacity(it.levels.size());
	for(Level l: it.levels)
	    this.levels.add(l.clone());
	this.refsBegin = it.refsEnd.clone ();
	this.refsEnd = it.refsEnd.clone();
    }

    public void check()
    {
	final Level level = getLevel();
	if (level.pos >= level.items.length)
	{
	    if (levels.size() == 1)
	    {
		matching.success(this);
		return;
	    }
	    if (level.ref != NO_REF)
		this.refsEnd[level.ref] = matching.tokenIndex;
	    levels.remove(levels.size() - 1);
	    matching.addCurrentPos(this);
	    return;
	}
	final Item item = level.items[level.pos];

	if (item instanceof Alternative)
	{
	    level.pos++;
	    final Alternative alt = (Alternative)item;
	    for(int i = 0;i < alt.items.length;i++)
	    {
final WhereIterator newIt = new WhereIterator(this);
newIt.levels.add(new Level(new Item[]{ alt.items[i] }, item.getRef() != null?item.getRef().num:NO_REF));
		if (item.getRef() != null)
		    newIt.refsBegin[item.getRef().num] = matching.tokenIndex;
		matching.addCurrentPos(newIt);
	    }
	    return;
	}

		if (item instanceof Block)
		{
		    final Block block = (Block)item;
		    levels.add(new Level(block.items, item.getRef() != null?item.getRef().num:NO_REF));
		    if (item.getRef() != null)
			this.refsBegin[item.getRef().num] = matching.tokenIndex;
		    matching.addCurrentPos(this);
		    return;
		}

	if (item instanceof Fixed)
	{
	    final Fixed fixed = (Fixed)item;
	    fixed.match(matching.token);
	}
    }

    private Level getLevel()
    {
	return this.levels.get(this.levels.size() - 1);
    }

    static private final class Level
{
    final Item[] items;
    int pos;
    final int ref;
    Level(Item[] items, int pos, int ref)
    {
	this.items = items;
	this.pos = pos;
	this.ref = ref;
    }
    Level(Item[] items, int ref)
    {
	this(items, 0, ref);
    }
    Level(Item[] items)
    {
	this(items, 0, NO_REF);
    }
    @Override public Level clone()
    {
	return new Level(items, pos);
    }
}
}
