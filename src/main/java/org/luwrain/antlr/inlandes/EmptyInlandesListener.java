
package org.luwrain.antlr.inlandes;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class EmptyInlandesListener implements InlandesListener
{
    @Override public void enterEveryRule(ParserRuleContext ctx) {}
    @Override public void exitEveryRule(ParserRuleContext ctx) {}
    @Override public void visitTerminal(TerminalNode node) {}

            @Override public void visitErrorNode(ErrorNode node)
    {
	throw new IllegalStateException(node.toString());
    }

    @Override public void enterNotation(InlandesParser.NotationContext ctx) {}
    @Override public void exitNotation(InlandesParser.NotationContext ctx) {}

            @Override public void enterRuleStatement(InlandesParser.RuleStatementContext ctx) {}
    @Override public void exitRuleStatement(InlandesParser.RuleStatementContext ctx) {}

        @Override public void enterRuleWord(InlandesParser.RuleWordContext ctx) {}
    @Override public void exitRuleWord(InlandesParser.RuleWordContext ctx) {}
}
