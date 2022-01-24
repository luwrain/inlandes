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
import java.io.*;

public final class Tokenizer extends AbstractTokenizer
{
    private final LinkedList<Character> qu = new LinkedList();
    private final Reader reader;

    public Tokenizer(Reader reader)
    {
	if (reader == null)
	    throw new NullPointerException("reader may not be null");
	this.reader = reader;
    }

    @Override public char getCh()
    {
	if (qu.isEmpty())
	    throw new RuntimeException("Trying to get a char with the empty queue");
	final Character ch = qu.pollFirst();
	return ch.charValue();
    }

    @Override public boolean hasCh()
    {
	if (!qu.isEmpty())
	    return true;
	final int ch;
	try {
	    ch = reader.read();
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
	if (ch < 0)
	    return false;
	qu.add(Character.valueOf((char)ch));
	return true;
    }

    @Override public void backCh(char ch)
    {
	qu.addFirst(Character.valueOf(ch));
    }

    static public org.luwrain.inlandes.Token[] tokenize(String text)
    {
	if (text == null)
	    throw new NullPointerException("text may not be null");
	final Tokenizer t = new Tokenizer(new StringReader(text));
	t.tokenize();
	return t.getOutput();
    }
}
