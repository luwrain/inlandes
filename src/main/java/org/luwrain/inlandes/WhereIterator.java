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
        private final Matching matching;
    final ArrayList<Level> levels = new ArrayList<>();

    public WhereIterator(Matching matching, WhereStatement whereStatement)
    {
	if (matching == null)
	    throw new NullPointerException("matching can't be null");
	if (whereStatement == null)
	    throw new NullPointerException("whereStatement can't be null");
	this.matching = matching;
	this.levels.add(new Level(whereStatement.items));
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
		newIt.levels.add(new Level(new Item[]{ alt.items[i] }));
		matching.addCurrentPos(newIt);
	    }
	    return;
	}

		if (item instanceof Block)
		{
		    final Block block = (Block)item;
		    levels.add(new Level(block.items));
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
    Level(Item[] items, int pos)
    {
	this.items = items;
	this.pos = pos;
    }
    Level(Item[] items)
    {
	this(items, 0);
    }
    @Override public Level clone()
    {
	return new Level(items, pos);
    }
}

}
