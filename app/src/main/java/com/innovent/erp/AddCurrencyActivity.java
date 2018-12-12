package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.adapter.AddCurrencyAdapter;
import com.innovent.erp.custom.NumberToWordsConverter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.AddCurrencyModel;
import com.innovent.erp.model.ReceptionModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCurrencyActivity extends AppCompatActivity implements AddCurrencyAdapter.Intercommmunitator {


    @BindView(R.id.final_total)
    TextView finalTotal;
    @BindView(R.id.final_total_word)
    TextView finalTotalWord;
    @BindView(R.id.nestedScrollview)
    NestedScrollView nestedScrollview;
    @BindView(R.id.final_qty)
    TextView finalQty;

    String insert_update_flag = "", object_string = "", title, desc, id;
    int position = 0;
    JSONArray itemArray;

    ArrayList<AddCurrencyModel> data = new ArrayList<>();
    AddCurrencyAdapter adapter;
    @BindView(R.id.recyleView)
    RecyclerView recyleView;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Currency Calculate");
        myPreferences = new MyPreferences(this);
        recyleView.setNestedScrollingEnabled(false);


        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");
            if (insert_update_flag.equals("1")) {
                position = bundle.getInt("position");
                id = bundle.getString("id");
                title = bundle.getString("title");
                desc = bundle.getString("desc");
                object_string = bundle.getString("itemArray");
                itemArray = new JSONArray(object_string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!myPreferences.getPreferences(MyPreferences.currencyArray).equals("")) {
            try {
                JSONArray currency = new JSONArray(myPreferences.getPreferences(MyPreferences.currencyArray));
                for (int i = 0; i < currency.length(); i++) {
                    JSONObject c = currency.getJSONObject(i);
                    AddCurrencyModel da = new AddCurrencyModel();
                    da.setId("" + c.getString("id"));
                    da.setTitle("" + c.getString("title"));
                    da.setQty("");
                    da.setTotal("");
                    if (insert_update_flag.equals("1")) {
                        for (int j = 0; j < itemArray.length(); j++) {
                            JSONObject t = itemArray.getJSONObject(j);
                            if (da.getId().equals("" + t.getString("note_id"))) {
                                da.setQty("" + t.getString("qty"));
                                da.setTotal("" + t.getString("value"));
                                break;
                            }
                        }
                    } else {
                        da.setQty("");
                        da.setTotal("");
                    }
                    data.add(da);
                }
                if (insert_update_flag.equals("1")) {
                    CalculateMethod();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new AddCurrencyAdapter(AddCurrencyActivity.this, data);
        recyleView.setAdapter(adapter);
        recyleView.setLayoutManager(new LinearLayoutManager(AddCurrencyActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_currency, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                getScreenshot();
                break;
            case R.id.action_save:
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddCurrencyActivity.this);

                    LayoutInflater inflater = AddCurrencyActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_add_currency, null);
                    alertDialog2.setView(dialogView);

                    final EditText titleTxt = (EditText) dialogView.findViewById(R.id.new_title);
                    final EditText descTxt = (EditText) dialogView.findViewById(R.id.new_desc);

                    if (insert_update_flag.equals("1")) {
                        titleTxt.setText(title);
                        descTxt.setText(desc);
                    }

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    try {
                                        if (GlobalElements.isConnectingToInternet(AddCurrencyActivity.this)) {
                                            JSONArray item = new JSONArray();
                                            JSONObject object;
                                            for (int i = 0; i < data.size(); i++) {
                                                if (!data.get(i).getQty().equals("")) {
                                                    object = new JSONObject();
                                                    object.put("id", "" + data.get(i).getId());
                                                    object.put("qty", "" + data.get(i).getQty());
                                                    item.put(object);
                                                }
                                            }
                                            addCurrency(item.toString(), titleTxt.getText().toString(), descTxt.getText().toString());
                                        } else {
                                            GlobalElements.showDialog(AddCurrencyActivity.this);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    alertDialog2.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                }
                            });

                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(AddCurrencyActivity.this, R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(AddCurrencyActivity.this, R.color.colorPrimary));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getScreenshot() {
        try {
            nestedScrollview.post(new Runnable() {
                @Override
                public void run() {
                    File filePath = null;
                    Bitmap screen = getBitmapFromView(nestedScrollview, nestedScrollview.getChildAt(0).getHeight(), nestedScrollview.getChildAt(0).getWidth());
                    if (screen != null) {
                        try {
                            filePath = saveImage(screen);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent shareIntent = new Intent();
                    Uri contentUri = null;
                    if (GlobalElements.getVersionCheck()) {
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        contentUri = FileProvider.getUriForFile(AddCurrencyActivity.this, "" + GlobalElements.fileprovider_path, filePath);
                    } else {
                        contentUri = Uri.fromFile(filePath);
                    }

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);  //optional//use this when you want to send an image
                    shareIntent.setType("image/png");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Intent intent = Intent.createChooser(shareIntent, "send");
                    ShareActionProvider sap = new ShareActionProvider(AddCurrencyActivity.this);
                    sap.setShareIntent(shareIntent);
                    sap.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
                        @Override
                        public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                            return false;
                        }
                    });
                    startActivityForResult(intent, 111);
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public static File saveImage(Bitmap bitmap) throws IOException {
        File pdfDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "" + GlobalElements.directory);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = new File(pdfDir, "" + GlobalElements.getCurrentdate() + ".png");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    @Override
    public void CalculateMethod() {
        try {
            int total = 0, totalQty = 0;
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).getQty().equals("")) {
                    totalQty = totalQty + Integer.parseInt(data.get(i).getQty());
                }
                if (!data.get(i).getTotal().equals("")) {
                    total = total + Integer.parseInt(data.get(i).getTotal());
                }
            }
            finalQty.setText("" + totalQty);
            finalTotal.setText(getResources().getString(R.string.currence) + " " + total);
            String return_val_in_english = NumberToWordsConverter.convert(total);
            finalTotalWord.setText("" + return_val_in_english);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCurrency(String json, String title, String desc) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddCurrencyActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);

            Call<ResponseBody> call;
            if (insert_update_flag.equals("0")) {
                call = request.addCurrency(myPreferences.getPreferences(MyPreferences.id), title, desc, json);
            } else {
                call = request.updateCurrency(myPreferences.getPreferences(MyPreferences.id), id, title, desc, json);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            if (insert_update_flag.equals("1")) {
                                Intent intent = new Intent();
                                intent.putExtra("position", position);
                                intent.putExtra("id", result.getString("id"));
                                intent.putExtra("title", result.getString("title"));
                                intent.putExtra("desc", result.getString("description"));
                                intent.putExtra("total", result.getString("total"));
                                intent.putExtra("itemArray", "" + result.getString("item"));
                                setResult(11, intent);
                                finish();
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("id", result.getString("id"));
                                intent.putExtra("title", result.getString("title"));
                                intent.putExtra("desc", result.getString("description"));
                                intent.putExtra("total", result.getString("total"));
                                intent.putExtra("itemArray", "" + result.getString("item"));
                                setResult(10, intent);
                                finish();
                            }
                            //  finish();
                        } else {
                            Toaster.show(AddCurrencyActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }

                        /*if (insert_update_flag.equals("1")) {
                                                Intent intent = new Intent();
                                                intent.putExtra("position", position);
                                                intent.putExtra("object", "" + item.toString());
                                                setResult(11, intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent();
                                                intent.putExtra("object", "" + item.toString());
                                                setResult(10, intent);
                                                finish();
                                            }*/

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
