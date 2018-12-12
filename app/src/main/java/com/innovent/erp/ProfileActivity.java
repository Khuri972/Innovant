package com.innovent.erp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.innovent.erp.custom.Toaster;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_layout_mobile)
    TextInputLayout inputLayoutMobile;
    @BindView(R.id.signup_old_password)
    EditText signupOldPassword;
    @BindView(R.id.signup_new_password)
    EditText signupNewPassword;
    @BindView(R.id.signup_new_password_retype)
    EditText signupNewPasswordRetype;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;

    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(ProfileActivity.this);

        inputName.setText("" + myPreferences.getPreferences(MyPreferences.name));
        inputEmail.setText("" + myPreferences.getPreferences(MyPreferences.email));
        inputMobile.setText("" + myPreferences.getPreferences(MyPreferences.mobile_no));

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signupNewPassword.getText().toString().equals("")) {
                    Toaster.show(ProfileActivity.this, "Password Required!!", false, Toaster.DANGER);
                    return;
                } else if (!signupNewPasswordRetype.getText().toString().equals(signupNewPassword.getText().toString())) {
                    Toaster.show(ProfileActivity.this, "This Should be same as above!!", false, Toaster.DANGER);
                    return;
                }
                if (GlobalElements.isConnectingToInternet(ProfileActivity.this)) {
                    ChangePassword("change_password");
                } else {
                    GlobalElements.showDialog(ProfileActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ChangePassword(final String temp) {
        try {
            final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = null;
            if (temp.equals("change_password")) {
                call = request.ChangeUserPassword(myPreferences.getPreferences(MyPreferences.id), signupOldPassword.getText().toString(), signupNewPassword.getText().toString());
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (temp.equals("change_password")) {
                            int success = json.getInt("ack");
                            if (success == 1) {
                                signupNewPassword.setText("");
                                signupNewPasswordRetype.setText("");
                                Toaster.show(ProfileActivity.this, "" + json.getString("ack_msg"), true, Toaster.SUCCESS);
                                finish();
                            } else if (success == 2) {
                                signupNewPassword.setText("");
                                signupNewPasswordRetype.setText("");
                                Toaster.show(ProfileActivity.this, "" + json.getString("ack_msg"), true, Toaster.DANGER);
                                finish();
                            } else {
                                signupNewPassword.setText("");
                                signupNewPasswordRetype.setText("");
                                Toaster.show(ProfileActivity.this, "" + json.getString("ack_msg"), true, Toaster.DANGER);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
