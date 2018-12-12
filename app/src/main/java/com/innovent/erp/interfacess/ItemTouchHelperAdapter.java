package com.innovent.erp.interfacess;

/**
 * Created by om on 22-Feb-17.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
