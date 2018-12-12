package com.innovent.erp.model;

/**
 * Created by om on 21-Feb-17.
 */

public class DashboardCardModel {
    String CardName;
    boolean isVisible;
    int Type;

    public DashboardCardModel() {
    }

    public DashboardCardModel(String cardName, boolean isVisible, int type) {
        CardName = cardName;
        this.isVisible = isVisible;
        Type = type;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public DashboardCardModel(int type) {
        Type = type;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
