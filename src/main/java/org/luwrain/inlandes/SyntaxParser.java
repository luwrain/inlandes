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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.luwrain.antlr.inlandes.*;
import org.luwrain.antlr.inlandes.InlandesParser.*;

public class SyntaxParser
{
    public void parse(String text)
    {
	final InlandesLexer l = new InlandesLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final InlandesParser p = new InlandesParser(tokens);
	final ParseTree tree = p.notation();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final Listener listener = new Listener();
	walker.walk(listener, tree);
    }

    static private final class Listener extends InlandesBaseListener
    {
	List<RuleStatement> rules = new ArrayList<RuleStatement>();
	private RuleStatement rule = null;

	@Override public void enterRuleStatement(RuleStatementContext ctx)
	{
	    rule = new RuleStatement();
	}

		@Override public void exitRuleStatement(RuleStatementContext ctx)
	{
	    if (rule == null)
		throw new NullPointerException("rule can't be null");
	    rules.add(rule);
	    rule = null;
	}

	@Override public void enterWhereStatement(WhereStatementContext ctx)
	{
	    for(WhereItemContext i: ctx.whereItem())
	    {
	    }
	}

			@Override public void exitWhereStatement(WhereStatementContext ctx)
	{

			}
    }
}
