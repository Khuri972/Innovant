package com.innovent.erp.visitorBookModual.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CB-PHP-1 on 8/9/2016.
 */
public class RatingDialog extends DialogFragment {
    EditText et_review;
    TextView tv_rating_title;
    RatingBar rb_rate;
    Button btn_submit;
    Context context;
    String id, position = "0";
    String review, rate = "1";
    String[] rateTitle = {"", "Hated it", "Disliked it", "It's Ok", "Liked it", "Loved it"};
    int rat = 0;

    public interface InterfaceCommunicator {
        void sendRequestCode(int position, int rating);
    }

    public static RatingDialog newInstance(Context context, String id, String position) {
        RatingDialog f = new RatingDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.id = getArguments().getString("id");
        this.position = getArguments().getString("position");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rating_dialog_layout, container, false);
        tv_rating_title = (TextView) v.findViewById(R.id.tv_rating_title);
        tv_rating_title.setText(rateTitle[1]);
        rb_rate = (RatingBar) v.findViewById(R.id.rb_rating_rate);
        rb_rate.setRating(1f);
        et_review = (EditText) v.findViewById(R.id.et_rating_review);
        btn_submit = (Button) v.findViewById(R.id.btn_rate_submit);

        rb_rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate = String.valueOf(ratingBar
                        .getRating());
                rat = (int) ratingBar.getRating();
                tv_rating_title.setText(rateTitle[rat]);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_review.getText().toString().equals("") || et_review.getText().toString().length() < 10) {
                    et_review.setError("Please add review with atleast 10 characters ");
                } else {
                    review = et_review.getText().toString();
                    addReview();
                }
            }
        });
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.rating_dialog_layout, null);
        final Dialog d = new Dialog(getActivity());
        d.setContentView(root);
        d.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return d;
    }

    public void addReview() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.addRestuarantRate(id, rate, review);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();
                try {
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);

                    if (json.getInt("ack") == 1) {
                        Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.SUCCESS);
                        InterfaceCommunicator i = (InterfaceCommunicator) getActivity();
                        i.sendRequestCode(Integer.parseInt("" + position), rat);
                        dismiss();
                    } else {
                        Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.SUCCESS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                System.out.print("error" + t.getMessage());
            }
        });
    }
}
