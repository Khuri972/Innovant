package com.innovent.erp.interfacess;

/**
 * Created by om on 23-Jan-17.
 */

public abstract class ListItem {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;
    public static final int TYPE_HEADER = 3;
    public static final int TYPE_FOOTER = 2;

    abstract public int getType();
}
