
package org.luwrain.inlandes;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.luwrain.antlr.inlandes.*;

public class SyntaxParser
{
    public void parse(String text)
    {
	final InlandesLexer l = new InlandesLexer(CharStreams.fromString(text));
	final CommonTokenStream tokens = new CommonTokenStream(l);
	final InlandesParser p = new InlandesParser(tokens);
	final ParseTree tree = p.notation();
	final ParseTreeWalker walker = new ParseTreeWalker();
	//	final List<Token[]> resTokens = new LinkedList();
	//	final List<Boolean> resOptional = new LinkedList();
	final InlandesListener listener = new EmptyInlandesListener(){
		private List<Token> tokens = new LinkedList();
		/*
				@Override public void enterUnitBase(TokenFilterNotationParser.UnitBaseContext c)
		{
		    if (c.Cyril() != null)
		}
		*/
	    };
	walker.walk(listener, tree);
}
}
