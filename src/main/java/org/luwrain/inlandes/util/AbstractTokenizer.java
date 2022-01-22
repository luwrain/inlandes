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

package org.luwrain.inlandes.util;

import java.util.*;

public abstract class AbstractTokenizer
{
    static public final char NBSP = 160;

    protected final List<Token> output = new ArrayList();

    abstract char getCh();
    abstract public boolean hasCh();
    abstract public void backCh(char ch);

    public void tokenize()
    {
	while (hasCh())
	{
	    final char ch = getCh();
	    if (ch >= '0' && ch <= '9')
		onNumToken(ch); else
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		    onLatinToken(ch); else
		    if (isCyrChar(ch))
			onCyrilToken(ch); else
			if (Character.isSpace(ch) || ch == NBSP)
			    onSpaceToken(ch); else
			    onPuncToken(ch);
	}
    }

    private void onNumToken(char ch)
    {
	final StringBuilder b = new StringBuilder();
	b.append(ch);
	while(hasCh())
	{
	    final char nextCh = getCh();
	    if (nextCh >= '0' && nextCh <= '9')
	    {
		b.append(nextCh);
		continue;
	    }
	    backCh(nextCh);
	    break;
	}
	output.add(new Token(Token.Type.NUM, new String(b)));
    }

    private void onLatinToken(char ch)
    {
	final StringBuilder b = new StringBuilder();
	b.append(ch);
	while(hasCh())
	{
	    final char nextCh = getCh();
	    if ((nextCh >= 'a' && nextCh <= 'z') || (nextCh >= 'A' && nextCh <= 'Z'))
	    {
		b.append(nextCh);
		continue;
	    }
	    backCh(nextCh);
	    break;
	}
	output.add(new Token(Token.Type.LATIN, new String(b)));
    }

    private void onCyrilToken(char ch)
    {
	final StringBuilder b = new StringBuilder();
	b.append(ch);
	while(hasCh())
	{
	    final char nextCh = getCh();
	    if (isCyrChar(nextCh))
	    {
		b.append(nextCh);
		continue;
	    }
	    backCh(nextCh);
	    break;
	}
	output.add(new Token(Token.Type.CYRIL, new String(b)));
    }

    private void onSpaceToken(char ch)
    {
	final StringBuilder b = new StringBuilder();
	b.append(ch);
	while(hasCh())
	{
	    final char nextCh = getCh();
	    if (Character.isSpace(nextCh) || nextCh == NBSP)
	    {
		b.append(nextCh);
		continue;
	    }
	    backCh(nextCh);
	    break;
	}
	output.add(new Token(Token.Type.SPACE, new String(b)));
    }

    private void onPuncToken(char ch)
    {
	output.add(new Token(Token.Type.PUNC, new Character(ch).toString()));
    }

    public Token[] getOutput()
    {
	return output.toArray(new Token[output.size()]);
    }

        static public boolean isCyrChar(char ch)
    {
	if (ch >= 'а' && ch <= 'я')
	    return true;
	if (ch >= 'А' && ch <= 'Я')
	    return true;
	if (ch == 'ё' || ch == 'Ё')
	    return true;
	return false;
    }
}
