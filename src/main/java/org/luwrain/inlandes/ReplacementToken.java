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

public final class ReplacementToken implements Token
{
    final int sourceRangeFrom, sourceRangeTo;
    final Token token;

    public ReplacementToken(int sourceRangeFrom, int sourceRangeTo, Token token)
    {
	if (sourceRangeFrom < 0)
	    throw new IllegalArgumentException("sourceRangeFrom can't be negative");
	if (sourceRangeTo < 0)
	    throw new IllegalArgumentException("sourceRangeTo can't be negative");
	if (sourceRangeFrom > sourceRangeTo)
	    throw new IllegalArgumentException("sourceRangeFrom must be less or equal than sourceRangeTo");
	if (token == null)
	    throw new NullPointerException("token can't be null");
	this.sourceRangeFrom = sourceRangeFrom;
	this.sourceRangeTo = sourceRangeTo;
	this.token = token;
    }

    @Override public boolean isLatin() { return token.isLatin(); }
    @Override public boolean isCyril() { return token.isCyril(); }
    @Override public boolean isNum() { return token.isNum(); }
    @Override public boolean isPunc() { return token.isPunc(); }
    @Override public boolean isSpace() { return token.isSpace(); }
    @Override public String getText() { return token.getText(); }
    @Override public String toString() { return token.toString(); }
}
