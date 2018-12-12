package com.innovent.erp.firabse;

import com.innovent.erp.netUtils.MyPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by CRAFT BOX on 2/24/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String refreshedToken;
    MyPreferences myPreferences;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            myPreferences = new MyPreferences(MyFirebaseInstanceIDService.this);
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            myPreferences.setPreferences(MyPreferences.refreshedToken,refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
