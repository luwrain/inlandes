
package org.luwrain.inlandes;

public final class Ref
{
    public final int num;

    public Ref(int num)
    {
	if (num < 0)
	    throw new IllegalArgumentException("num can't be negative");
	this.num = num;
    }
}
