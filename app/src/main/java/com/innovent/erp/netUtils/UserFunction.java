package com.innovent.erp.netUtils;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.SearchCustomerModel;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.model.AreaModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.SearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 3/6/2018.
 */

public class UserFunction {

    public ArrayList<SearchModel> searReciverCompany(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=15&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    SearchModel da = new SearchModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("company_name"));
                    da.setAddress(r.getString("company_address"));
                    da.setCountry(r.getString("company_country"));
                    da.setState(r.getString("company_state"));
                    da.setCity(r.getString("company_city"));
                    da.setPincode(r.getString("company_pincode"));
                    da.setMobile(r.getString("company_mobile"));
                    da.setEmail(r.getString("company_email"));
                    da.setArea(r.getString("company_area"));
                    da.setDistrict(r.getString("company_district"));
                    da.setZone(r.getString("company_zone"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchModel> searReciverPerson(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=16&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    SearchModel da = new SearchModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("person_name"));
                    da.setAddress(r.getString("person_home_address"));
                    da.setCountry(r.getString("person_country"));
                    da.setState(r.getString("person_state"));
                    da.setCity(r.getString("person_city"));
                    da.setPincode(r.getString("person_pincode"));
                    da.setMobile(r.getString("person_mobile"));
                    da.setEmail(r.getString("person_email"));
                    da.setArea(r.getString("person_area"));
                    da.setDistrict(r.getString("person_district"));
                    da.setZone(r.getString("person_zone"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<AreaModel> searArea(String sName) {
        ArrayList<AreaModel> ListData = new ArrayList<AreaModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "leave_service.php" + "?key=1226&s=96&type=area&term=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    AreaModel da = new AreaModel();
                    da.setId(r.getString("id"));
                    da.setArea(r.getString("area"));
                    da.setDistrict(r.getString("district"));
                    da.setState(r.getString("state"));
                    da.setCountry(r.getString("country"));
                    da.setZone(r.getString("zone"));
                    da.setPincode(r.getString("pincode"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<AreaModel> searZone(String sName) {
        ArrayList<AreaModel> ListData = new ArrayList<AreaModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "leave_service.php" + "?key=1226&s=96&type=zone&term=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    AreaModel da = new AreaModel();
                    da.setId("");
                    da.setArea("");
                    da.setDistrict("");
                    da.setState("");
                    da.setCountry("");
                    da.setZone(r.getString("zone"));
                    da.setPincode("");
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<AreaModel> searDistrict(String sName) {
        ArrayList<AreaModel> ListData = new ArrayList<AreaModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "leave_service.php" + "?key=1226&s=96&type=district&term=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    AreaModel da = new AreaModel();
                    da.setId("");
                    da.setArea("");
                    da.setDistrict(r.getString("district"));
                    da.setState("");
                    da.setCountry("");
                    da.setZone("");
                    da.setPincode("");
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchModel> searCity(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=26&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    SearchModel da = new SearchModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("name"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchCustomerModel> searCustomer(String sName) {  // erp modual
        ArrayList<SearchCustomerModel> ListData = new ArrayList<SearchCustomerModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "customer.php" + "?key=1226&s=103&query=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    SearchCustomerModel da = new SearchCustomerModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("name"));
                    da.setEmail(r.getString("email"));
                    da.setPhone_1(r.getString("phone_1"));
                    da.setAddress_1(r.getString("address_1"));
                    da.setCity(r.getString("city"));
                    da.setState(r.getString("state"));
                    da.setCountry(r.getString("country"));
                    da.setZone(r.getString("zone"));
                    da.setArea(r.getString("area"));
                    da.setDistrict(r.getString("district"));
                    da.setCustomer_type("");
                    da.setZone_name(r.getString("zone_name"));
                    da.setPincode(r.getString("pincode"));
                    da.setGst_no(r.getString("gst_no"));
                    da.setPancard_no(r.getString("pancard_no"));
                    //da.setArea_name(r.getString("area_name"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<ProductModel> searItem(String sName, String customer_id) {  // erp modual
        ArrayList<ProductModel> ListData = new ArrayList<ProductModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "sales_quotation.php" + "?key=1226&s=104&query=" + temp + "&customer_id" + customer_id);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    ProductModel da = new ProductModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("name"));
                    da.setHsnCode(r.getString("hsn_code"));
                    da.setUnit(r.getString("unit_name"));
                    da.setGstType(1);
                    da.setQty(0);
                    da.setSell_price(r.getDouble("sell_price"));
                    da.setDiscount(0);
                    da.setAmount(0);
                    da.setGst(r.getDouble("igst_tax"));
                    da.setCgst(0);
                    da.setSgst(0);
                    da.setIgst(0);
                    da.setNetAmount(0);
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchModel> searBank(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=101&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            if (jsonResponse.getInt("ack") == 1) {
                JSONArray jsonArray = jsonResponse.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);
                    SearchModel da = new SearchModel();
                    da.setId(r.getString("id"));
                    da.setName(r.getString("name"));
                    ListData.add(da);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }


    public ArrayList<SerialNoModel> searSerialNoItem(String sName) {  // erp modual
        ArrayList<SerialNoModel> ListData = new ArrayList<SerialNoModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_pipeline.php" + "?key=1226&s=119&batch_no=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
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
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }
}
