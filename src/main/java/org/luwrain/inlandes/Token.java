/*
 * Copyright 2021-2024 Michael Pozhidaev <msp@luwrain.org>
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

public interface Token
{
    boolean isLatin();
    boolean isCyril();
    boolean isNum();
    boolean isPunc();
    boolean isSpace();
    String getText();

    static public String concatText(Token[] tokens)
    {
	final StringBuilder b = new StringBuilder();
	for(Token t: tokens)
	    b.append(t.getText());
	return new String(b);
    }
}
