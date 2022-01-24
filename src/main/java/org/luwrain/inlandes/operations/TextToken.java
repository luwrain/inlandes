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

package org.luwrain.inlandes.operations;

import org.luwrain.inlandes.*;

public final class TextToken implements Token
{
    private final String text;

    TextToken(String text)
    {
	if (text == null)
	    throw new NullPointerException("text can't be null");
	this.text = text;
	    }

    @Override public boolean isLatin() { return false; }
    @Override public boolean isCyril() { return false; }
    @Override public boolean isNum() { return false; }
    @Override public boolean isPunc() { return false; }
    @Override public boolean isSpace() { return false; }
    @Override public String getText() { return text; }
    @Override public String toString() { return text; }
}
