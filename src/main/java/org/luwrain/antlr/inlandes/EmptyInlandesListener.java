/*
 * Copyright 2021 Michael Pozhidaev <msp@luwrain.org>
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
