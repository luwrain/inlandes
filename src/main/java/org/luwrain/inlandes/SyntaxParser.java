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
static private final String
    OPTIONAL_MARK = "?";
    
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
	    if (c.Str() != null)
	    {
		final String str = c.Str().toString();
		rule.addOperation(new Assignment(new Ref(parseInt(c.Ref().toString().substring(1))), Assignment.ValueType.STRING, str.substring(1, str.length() - 1)));
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
	    return new WhereStatement.Block(items, (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null, c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));
	}

	if (c.whereAlternative() != null)
	{
	    final WhereAlternativeContext alt = c.whereAlternative();
	    final List<WhereStatement.Item> items = new ArrayList<>();
	    for(WhereItemContext i: alt.whereItem()) 
		items.add(createWhereItem(i));
	    return new WhereStatement.Alternative(items, (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null, c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));
	}

	if (c.whereFixed() != null)
	{
	    final WhereFixedContext fixed = c.whereFixed();

	    if (fixed.Space() != null)
		return new WhereStatement.Fixed((token)->token.isSpace(), " ",
						(c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null,
						c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));

	    		if (fixed.CyrilPlain() != null)
		{
		    final String textUpper = fixed.CyrilPlain().toString().toUpperCase();
		    return new WhereStatement.Fixed((token)->(token.isCyril() && token.getText().length() == textUpper.length() && token.getText().toUpperCase().equals(textUpper)), textUpper,
						    (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null,
						    c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));
		}

				    		if (fixed.Latin() != null)
		{
		    final String text = fixed.Latin().toString();
		    final String textUpper = text.substring(1, text.length() - 1).toUpperCase();
		    return new WhereStatement.Fixed((token)->(token.isLatin() && token.getText().toUpperCase().equals(textUpper)), textUpper,
						    (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null,
						    c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));
		}

										    		if (fixed.Punc() != null)
		{
		    final String t = fixed.Punc().toString();
		    final String text = t.substring(1, t.length() - 1);
		    return new WhereStatement.Fixed((token)->(token.isPunc() && token.getText().toUpperCase().equals(text)), text,
						    (c.Ref() != null)?new Ref(parseInt(c.Ref().toString().substring(1))):null,
						    c.Optional() != null && c.Optional().toString().equals(OPTIONAL_MARK));
		}
	} //fixed
	throw new RuntimeException(c.toString());
    }

    public RuleStatement[] parse(String text)
    {
	final BaseErrorListener errors = new BaseErrorListener() {
		@Override public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
		{
		    throw new RuntimeException(msg);
		}
	    };
	final InlandesLexer l = new InlandesLexer(CharStreams.fromString(text));
	l.removeErrorListeners();
	l.addErrorListener(errors);
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final InlandesParser p = new InlandesParser(tokens);
	final ParseTree tree = p.notation();
	final ParseTreeWalker walker = new ParseTreeWalker();
	final Listener listener = new Listener();
	walker.walk(listener, tree);
	return listener.rules.toArray(new RuleStatement[listener.rules.size()]);
    }
}
