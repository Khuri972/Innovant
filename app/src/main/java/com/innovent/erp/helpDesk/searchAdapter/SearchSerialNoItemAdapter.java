package com.innovent.erp.helpDesk.searchAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import okhttp3.ResponseBody;

/**
 * Created by CRAFT BOX on 1/24/2017.
 */

public class SearchSerialNoItemAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public List<SerialNoModel> suggestions;
    private LayoutInflater inflater1 = null;
    String customer_id = "";

    public SearchSerialNoItemAdapter(Context context, String customer_id) {
        this.context = context;
        suggestions = new ArrayList<SerialNoModel>();
        this.customer_id = customer_id;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int i) {
        return suggestions.get(i).getSerialNo();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            inflater1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater1.inflate(R.layout.list_general, null);
        }
        try {
            TextView name;
            name = (TextView) vi.findViewById(R.id.general_name);
            name.setText("" + suggestions.get(position).getSerialNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = null;
                try {
                    filterResults = new FilterResults();
                    if (constraint != null) {
                        // A class that queries a web API, parses the data and
                        // returns an ArrayList<GoEuroGetSet>

                        Observable<ResponseBody> observable = dataFromNetwork(constraint.toString());
                        final FilterResults finalFilterResults = filterResults;
                        observable.debounce(300, TimeUnit.MILLISECONDS).filter(new Predicate<ResponseBody>() {
                            @Override
                            public boolean test(ResponseBody responseBody) throws Exception {
                                return true;
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    System.out.print("" + responseBody.byteStream());
                                    String json_response = null;
                                    try {
                                        json_response = responseBody.string();
                                        ArrayList<SerialNoModel> ListData = new ArrayList<SerialNoModel>();
                                        JSONObject jsonResponse = new JSONObject(json_response);
                                        if (jsonResponse.getInt("ack") == 1) {
                                            JSONObject result = jsonResponse.getJSONObject("result");
                                            JSONObject detail = result.getJSONObject("detail");
                                            SerialNoModel da = new SerialNoModel();
                                            da.setId(result.getString("id"));
                                            da.setName(detail.getString("name"));
                                            da.setHsn_code(detail.getString("hsn_code"));
                                            da.setBrand(detail.getString("brand_name"));
                                            da.setColor(detail.getString("color_name"));
                                            da.setSerialNo(result.getString("batch_no"));
                                            da.setModel(detail.getString("model_name"));
                                            if (result.getString("created_date_format").equals("")) {
                                                da.setPurchaseDate("");
                                            } else {
                                                try {
                                                    String[] date = result.getString("created_date_format").split(" ");
                                                    da.setPurchaseDate("" + date[0]);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    da.setPurchaseDate("");
                                                }
                                            }
                                            da.setQty(detail.getInt("qty"));
                                            ListData.add(da);
                                            List<SerialNoModel> new_suggestions = ListData;
                                            suggestions.clear();
                                            suggestions.addAll(new_suggestions);

                                            finalFilterResults.values = suggestions;
                                            finalFilterResults.count = suggestions.size();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                       /* UserFunction jp = new UserFunction();
                        List<SerialNoModel> new_suggestions = jp.searSerialNoItem(constraint.toString());
                        suggestions.clear();
                        suggestions.addAll(new_suggestions);
                        filterResults.values = suggestions;
                        filterResults.count = suggestions.size(); */
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

    public void setUpSearchObservable(String NewText) {

    }

    public Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
        return request.getSearch1(query);
    }
}
