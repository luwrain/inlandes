
package org.luwrain.inlandes;

import org.junit.*;

public class WhereStatementTest extends Assert
{
    private final SyntaxParser p = new SyntaxParser();

    @Test public void simple()
    {
	p.parse("RULE RULE");
    }
}
