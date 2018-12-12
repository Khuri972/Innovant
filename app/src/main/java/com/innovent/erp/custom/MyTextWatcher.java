package com.innovent.erp.custom;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by CRAFT BOX on 5/8/2018.
 */

public class MyTextWatcher implements TextWatcher {
    TextInputLayout view1;

    public MyTextWatcher(TextInputLayout view1) {
        this.view1 = view1;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        try {
            view1.setErrorEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
