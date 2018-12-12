package com.innovent.erp.interfacess;


import com.innovent.erp.model.DashboardCardModel;

import java.util.List;

/**
 * Created by om on 22-Feb-17.
 */
public interface OnDashboardChangedListener {
    void onNoteListChanged(List<DashboardCardModel> customers);
}