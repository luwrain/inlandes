
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
