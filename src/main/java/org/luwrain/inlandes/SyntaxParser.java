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

import static java.lang.Integer.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.luwrain.antlr.inlandes.*;
import org.luwrain.antlr.inlandes.InlandesParser.*;
import org.luwrain.inlandes.operations.*;

public class SyntaxParser
{
    private final class Listener extends InlandesBaseListener
    {
	List<RuleStatement> rules = new ArrayList<RuleStatement>();
	private RuleStatement rule = null;
	@Override public void visitErrorNode(ErrorNode node)
	{
	    throw new RuntimeException(node.toString());
	}
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
	@Override public void exitWhereStatement(WhereStatementContext c)
	{
	    final List<WhereStatement.Item> res = new ArrayList<>();
	    for(WhereItemContext i: c.whereItem())
		res.add(createWhereItem(i));
	    this.rule.setWhere(new WhereStatement(res));
	}
	@Override public void exitAssignment(AssignmentContext c)
	{
	    if (c.Js() != null)
	    {
		final String js = c.Js().toString();
		rule.addOperation(new Assignment(new Ref(parseInt(c.Ref().toString().substring(1))), Assignment.ValueType.JS, js.substring(2, js.length() - 2)));
	    }
	}
    }

    private WhereStatement.Item createWhereItem(WhereItemContext c)
    {
	if (c.whereBlock() != null)
	{
	    final WhereBlockContext block = c.whereBlock();
	    final List<WhereStatement.Item> items = new ArrayList<>();
	    for(WhereItemContext i: block.whereItem()) 
		items.add(createWhereItem(i));
	    return new WhereStatement.Block(items, (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null);
	}

	if (c.whereAlternative() != null)
	{
	    final WhereAlternativeContext alt = c.whereAlternative();
	    final List<WhereStatement.Item> items = new ArrayList<>();
	    for(WhereItemContext i: alt.whereItem()) 
		items.add(createWhereItem(i));
	    return new WhereStatement.Alternative(items, (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null);
	}

	if (c.whereFixed() != null)
	{
	    final WhereFixedContext fixed = c.whereFixed();

	    if (fixed.Space() != null)
	    {
	    }

	    
	    if (fixed.cons() != null)
	    {
		final ConsContext cons = fixed.cons();
		if (cons.CyrilPlain() != null)
		    return new WhereStatement.Fixed((token)->{ return token.isCyril() && noCaseEquals(token.getText(), cons.CyrilPlain().toString()); }, cons.CyrilPlain().toString().toUpperCase());
	    }
	    return null;
	}
	return null;
    }

        public RuleStatement[] parse(String text)
    {
	final InlandesLexer l = new InlandesLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final InlandesParser p = new InlandesParser(tokens);
	final ParseTree tree = p.notation();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final Listener listener = new Listener();
	walker.walk(listener, tree);
		return listener.rules.toArray(new RuleStatement[listener.rules.size()]);
    }

    static private final boolean noCaseEquals(String s1, String s2)
    {
	return s1.toUpperCase().equals(s2.toUpperCase());
    }
}
