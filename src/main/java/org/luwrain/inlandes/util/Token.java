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

public final class Token implements org.luwrain.inlandes.Token
{
    public enum Type {
	NUM,
	LATIN,
	CYRIL,
	SPACE,
	PUNC};

    public final Type type;
    public final String text;

    public Token(Type type, String text)
    {
	if (type == null)
	    throw new NullPointerException("type may not be null");
	if (text == null)
	    throw new NullPointerException("text may not be null");
	if (text.isEmpty())
	    throw new IllegalArgumentException("text may not be empty");
	this.type = type;
	this.text = text;
    }

    @Override public boolean isLatin()
    {
	return type == Type.LATIN;
    }

    @Override public boolean isCyril()
    {
	return type == Type.CYRIL;
    }

    @Override public boolean isNum()
    {
	return type == Type.NUM;
    }

    @Override public boolean isPunc()
    {
	return type == Type.PUNC;
    }

    @Override public String getText()
    {
	return text;
    }

    @Override public String toString()
    {
	return text;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Token))
	    return false;
	final Token t = (Token)o;
	return type == t.type && text.equals(t.text);
    }

    public boolean isFirstUpperCase()
    {
	if (type != Type.LATIN && type != Type.CYRIL)
	    return false;
	return Character.isUpperCase(text.charAt(0));
    }

    public boolean isLastUpperCase()
    {
	if (type != Type.LATIN && type != Type.CYRIL)
	    return false;
	return Character.isUpperCase(text.charAt(text.length() - 1));
    }

    static public String text(Token[] tokens)
    {
	if (tokens == null)
	    return "";
	final StringBuilder b = new StringBuilder();
	for(Token t: tokens)
	    if (t != null)
		b.append(t.text);
	return new String(b);
    }

    static public int find(Token[] tokens, Token token)
    {
	for(int i = 0;i < tokens.length;i++)
	    if (tokens[i].equals(token))
		return i;
	return -1;
    }

    static public Token num(String text)
    {
	return new Token(Type.NUM, text);
    }

    static public Token punc(String text)
    {
	return new Token(Type.PUNC, text);
    }

    static public Token latin(String text)
    {
	return new Token(Type.LATIN, text);
    }

    static public Token cyril(String text)
    {
	return new Token(Type.CYRIL, text);
    }
}
