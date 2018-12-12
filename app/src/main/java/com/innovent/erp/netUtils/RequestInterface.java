package com.innovent.erp.netUtils;


import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.model.CourierModel;
import com.innovent.erp.model.GeneralModel;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterface {

    String secure_field = "key";
    String secure_value = "1226";

    // todo GetCheckVersion
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=97")
    Call<ResponseBody> CheckVersion();

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=100&type=Android")
    Call<ResponseBody> changeAlias();

    // todo GetAttendance
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=81")
    Call<ResponseBody> AddAttendance(@Query("user_id") String user_id
            , @Query("imei") String imei
            , @Query("type") String type);

    // todo Login
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=3&type=Android")
    Call<ResponseBody> LoginUser(@Query("mobile") String mobile,
                                 @Query("password") String password,
                                 @Query("imei") String imei,
                                 @Query("refreshToken") String refreshToken
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=82&type=Android")
    Call<ResponseBody> otpVerification(@Query("user_id") String user_id,
                                       @Query("otp") String otp
    );

    // todo Change Password
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=2&type=Android")
    Call<ResponseBody> ChangeUserPassword(@Query("user_id") String user_id,
                                          @Query("password") String password,
                                          @Query("new_password") String new_password
    );

    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=4&type=Android")
    Call<ResponseBody> getBanner();

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=52&type=Android")
    Call<ResponseBody> getNoticeCircule(@Query("user_id") String id);

    // todo Add company contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=1&type=Android")
    Call<ResponseBody> addCompanyContact(@Query("user_id") String id,
                                         @Query("label_id") String label_id,
                                         @Query("tag_id") String tag_id,
                                         @Query("company_name") String company_name,
                                         @Query("company_address") String office_address,
                                         @Query("company_country") String country,
                                         @Query("company_state") String state,
                                         @Query("company_city") String city,
                                         @Query("company_zone") String zone,
                                         @Query("company_area") String area,
                                         @Query("company_district") String district,
                                         @Query("company_pincode") String pincode,
                                         @Query("company_office_phone") String office_phone_no,
                                         @Query("company_mobile") String mobile_no,
                                         @Query("company_email") String email,
                                         @Query("company_website") String website,
                                         @Query("company_wharehouse_address") String where_house_address,
                                         @Query("company_wharehouse_country") String where_house_country,
                                         @Query("company_wharehouse_state") String where_house_state,
                                         @Query("company_wharehouse_city") String where_house_city,
                                         @Query("company_wharehouse_zone") String where_house_zone,
                                         @Query("company_wharehouse_area") String where_area,
                                         @Query("company_wharehouse_district") String where_district,
                                         @Query("company_wharehouse_pincode") String where_house_pincode,
                                         @Query("company_wharehouse_phone") String where_house_phone,
                                         @Query("company_wharehouse_mobile") String where_house_mobile,
                                         @Query("company_wharehouse_email") String where_house_email,
                                         @Query("company_gst_no") String gst_no,
                                         @Query("company_pan_no") String pan_no,
                                         @Query("courier_address_company") String courier_address_company,
                                         @Query("print_label") String print_lable,
                                         @Query("courier_address_wherehouse") String courier_address_wherehouse,
                                         @Query("company_bank_name") String company_bank_name,
                                         @Query("company_bank_acc_no") String company_bank_acc_no,
                                         @Query("company_bank_ifsc") String company_bank_ifsc,
                                         @Query("company_account_name") String company_account_name,
                                         @Query("same_as_company") String same_as_company
    );

    // todo Add company contact
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=1&type=Android")
    Call<ResponseBody> addCompanyContact(@Query("user_id") String id,
                                         @Query("label_id") String label_id,
                                         @Query("tag_id") String tag_id,
                                         @Query("company_name") String company_name,
                                         @Query("company_address") String office_address,
                                         @Query("company_country") String country,
                                         @Query("company_state") String state,
                                         @Query("company_city") String city,
                                         @Query("company_zone") String zone,
                                         @Query("company_area") String area,
                                         @Query("company_district") String district,
                                         @Query("company_pincode") String pincode,
                                         @Query("company_office_phone") String office_phone_no,
                                         @Query("company_mobile") String mobile_no,
                                         @Query("company_email") String email,
                                         @Query("company_website") String website,
                                         @Query("company_wharehouse_address") String where_house_address,
                                         @Query("company_wharehouse_country") String where_house_country,
                                         @Query("company_wharehouse_state") String where_house_state,
                                         @Query("company_wharehouse_city") String where_house_city,
                                         @Query("company_wharehouse_zone") String where_house_zone,
                                         @Query("company_wharehouse_area") String where_area,
                                         @Query("company_wharehouse_district") String where_district,
                                         @Query("company_wharehouse_pincode") String where_house_pincode,
                                         @Query("company_wharehouse_phone") String where_house_phone,
                                         @Query("company_wharehouse_mobile") String where_house_mobile,
                                         @Query("company_wharehouse_email") String where_house_email,
                                         @Query("company_gst_no") String gst_no,
                                         @Query("company_pan_no") String pan_no,
                                         @Query("courier_address") String courier_address,
                                         @Query("print_label") String print_lable,
                                         @Query("courier_address_wherehouse") String courier_address_wherehouse,
                                         @Query("company_bank_name") String company_bank_name,
                                         @Query("company_bank_acc_no") String company_bank_acc_no,
                                         @Query("company_bank_ifsc") String company_bank_ifsc,
                                         @Query("company_account_name") String company_account_name,
                                         @Query("same_as_company") String same_as_company,
                                         @Part MultipartBody.Part file
    );

    // todo Update company contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=5&type=Android")
    Call<ResponseBody> updateCompanyContact(@Query("user_id") String user_id,
                                            @Query("id") String id,
                                            @Query("label_id") String label_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("company_name") String company_name,
                                            @Query("company_address") String office_address,
                                            @Query("company_country") String country,
                                            @Query("company_state") String state,
                                            @Query("company_city") String city,
                                            @Query("company_zone") String zone,
                                            @Query("company_area") String area,
                                            @Query("company_district") String district,
                                            @Query("company_pincode") String pincode,
                                            @Query("company_office_phone") String office_phone_no,
                                            @Query("company_mobile") String mobile_no,
                                            @Query("company_email") String email,
                                            @Query("company_website") String website,
                                            @Query("company_wharehouse_address") String where_house_address,
                                            @Query("company_wharehouse_country") String where_house_country,
                                            @Query("company_wharehouse_state") String where_house_state,
                                            @Query("company_wharehouse_city") String where_house_city,
                                            @Query("company_wharehouse_zone") String where_house_zone,
                                            @Query("company_wharehouse_area") String where_house_area,
                                            @Query("company_wharehouse_district") String where_house_district,
                                            @Query("company_wharehouse_pincode") String where_house_pincode,
                                            @Query("company_wharehouse_phone") String where_house_phone,
                                            @Query("company_wharehouse_mobile") String where_house_mobile,
                                            @Query("company_wharehouse_email") String where_house_email,
                                            @Query("company_gst_no") String gst_no,
                                            @Query("company_pan_no") String pan_no,
                                            @Query("courier_address_company") String courier_address_company,
                                            @Query("print_label") String print_lable,
                                            @Query("courier_address_wherehouse") String courier_address_wherehouse,
                                            @Query("company_bank_name") String company_bank_name,
                                            @Query("company_bank_acc_no") String company_bank_acc_no,
                                            @Query("company_bank_ifsc") String company_bank_ifsc,
                                            @Query("company_account_name") String company_account_name,
                                            @Query("same_as_company") String same_as_company
    );

    // todo Update company contact
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=5&type=Android")
    Call<ResponseBody> updateCompanyContact(@Query("user_id") String user_id,
                                            @Query("id") String id,
                                            @Query("label_id") String label_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("company_name") String company_name,
                                            @Query("company_address") String office_address,
                                            @Query("company_country") String country,
                                            @Query("company_state") String state,
                                            @Query("company_city") String city,
                                            @Query("company_zone") String zone,
                                            @Query("company_area") String area,
                                            @Query("company_district") String district,
                                            @Query("company_pincode") String pincode,
                                            @Query("company_office_phone") String office_phone_no,
                                            @Query("company_mobile") String mobile_no,
                                            @Query("company_email") String email,
                                            @Query("company_website") String website,
                                            @Query("company_wharehouse_address") String where_house_address,
                                            @Query("company_wharehouse_country") String where_house_country,
                                            @Query("company_wharehouse_state") String where_house_state,
                                            @Query("company_wharehouse_city") String where_house_city,
                                            @Query("company_wharehouse_zone") String where_house_zone,
                                            @Query("company_wharehouse_area") String where_house_area,
                                            @Query("company_wharehouse_district") String where_house_district,
                                            @Query("company_wharehouse_pincode") String where_house_pincode,
                                            @Query("company_wharehouse_phone") String where_house_phone,
                                            @Query("company_wharehouse_mobile") String where_house_mobile,
                                            @Query("company_wharehouse_email") String where_house_email,
                                            @Query("company_gst_no") String gst_no,
                                            @Query("company_pan_no") String pan_no,
                                            @Query("courier_address") String courier_address,
                                            @Query("print_label") String print_lable,
                                            @Query("courier_address_wherehouse") String courier_address_wherehouse,
                                            @Query("company_bank_name") String company_bank_name,
                                            @Query("company_bank_acc_no") String company_bank_acc_no,
                                            @Query("company_bank_ifsc") String company_bank_ifsc,
                                            @Query("company_account_name") String company_account_name,
                                            @Query("same_as_company") String same_as_company,
                                            @Part MultipartBody.Part file
    );

    // todo Add Individual contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=6&type=Android")
    Call<ResponseBody> addIndividualContact(@Query("user_id") String id,
                                            @Query("label_id") String label_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("person_name") String person_name,
                                            @Query("person_middle_name") String person_middle_name,
                                            @Query("person_surname") String person_surname,
                                            @Query("person_jobtitle") String person_jobtitle,
                                            @Query("person_home_address") String person_home_address,
                                            @Query("person_city") String person_city,
                                            @Query("person_area") String person_area,
                                            @Query("person_district") String person_district,
                                            @Query("person_state") String person_state,
                                            @Query("person_country") String person_country,
                                            @Query("person_pincode") String person_pincode,
                                            @Query("person_home_phone") String person_home_phone,
                                            @Query("person_mobile") String person_mobile,
                                            @Query("person_whatsapp") String person_whatsapp,
                                            @Query("person_email") String person_email,
                                            @Query("person_website") String person_website,
                                            @Query("person_birthdate") String person_birthdate,
                                            @Query("person_anniversary") String person_anniversary,
                                            @Query("person_event") String person_event,
                                            @Query("person_event_note") String person_event_note,
                                            @Query("person_note") String person_note,
                                            @Query("person_adhar_no") String person_adhar_no,
                                            @Query("person_pan_no") String person_pan_no,
                                            @Query("courier_address") String courier_address,
                                            @Query("print_label") String print_lable,
                                            @Query("same_as_office") String same_as_office,
                                            @Query("person_office_address") String person_office_address,
                                            @Query("person_office_country") String person_office_country,
                                            @Query("person_office_state") String person_office_state,
                                            @Query("person_office_city") String person_office_city,
                                            @Query("person_office_area") String person_office_area,
                                            @Query("person_office_district") String person_office_district,
                                            @Query("person_office_pincode") String person_office_pincode,
                                            @Query("person_account_name") String person_account_name,
                                            @Query("person_bank_name") String person_bank_name,
                                            @Query("person_bank_acc_no") String person_bank_acc_no,
                                            @Query("person_bank_ifsc") String person_bank_ifsc
    );

    // todo Add Individual contact 1
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=6&type=Android")
    Call<ResponseBody> addIndividualContact(@Query("user_id") String id,
                                            @Query("label_id") String label_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("person_name") String person_name,
                                            @Query("person_middle_name") String person_middle_name,
                                            @Query("person_surname") String person_surname,
                                            @Query("person_jobtitle") String person_jobtitle,
                                            @Query("person_home_address") String person_home_address,
                                            @Query("person_city") String person_city,
                                            @Query("person_area") String person_area,
                                            @Query("person_district") String person_district,
                                            @Query("person_state") String person_state,
                                            @Query("person_country") String person_country,
                                            @Query("person_pincode") String person_pincode,
                                            @Query("person_home_phone") String person_home_phone,
                                            @Query("person_mobile") String person_mobile,
                                            @Query("person_whatsapp") String person_whatsapp,
                                            @Query("person_email") String person_email,
                                            @Query("person_website") String person_website,
                                            @Query("person_birthdate") String person_birthdate,
                                            @Query("person_anniversary") String person_anniversary,
                                            @Query("person_event") String person_event,
                                            @Query("person_event_note") String person_event_note,
                                            @Query("person_note") String person_note,
                                            @Query("person_adhar_no") String person_adhar_no,
                                            @Query("person_pan_no") String person_pan_no,
                                            @Query("courier_address") String courier_address,
                                            @Query("print_label") String print_lable,
                                            @Query("same_as_office") String same_as_office,
                                            @Query("person_office_address") String person_office_address,
                                            @Query("person_office_country") String person_office_country,
                                            @Query("person_office_state") String person_office_state,
                                            @Query("person_office_city") String person_office_city,
                                            @Query("person_office_area") String person_office_area,
                                            @Query("person_office_district") String person_office_district,
                                            @Query("person_office_pincode") String person_office_pincode,
                                            @Query("person_account_name") String person_account_name,
                                            @Query("person_bank_name") String person_bank_name,
                                            @Query("person_bank_acc_no") String person_bank_acc_no,
                                            @Query("person_bank_ifsc") String person_bank_ifsc,
                                            @Part MultipartBody.Part file
    );

    // todo update Individual contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=7&type=Android")
    Call<ResponseBody> updateIndividualContact(@Query("user_id") String user_id,
                                               @Query("id") String id,
                                               @Query("label_id") String label_id,
                                               @Query("tag_id") String tag_id,
                                               @Query("person_name") String person_name,
                                               @Query("person_middle_name") String person_middle_name,
                                               @Query("person_surname") String person_surname,
                                               @Query("person_jobtitle") String person_jobtitle,
                                               @Query("person_home_address") String person_home_address,
                                               @Query("person_city") String person_city,
                                               @Query("person_area") String person_area,
                                               @Query("person_district") String person_district,
                                               @Query("person_state") String person_state,
                                               @Query("person_country") String person_country,
                                               @Query("person_pincode") String person_pincode,
                                               @Query("person_home_phone") String person_home_phone,
                                               @Query("person_mobile") String person_mobile,
                                               @Query("person_whatsapp") String person_whatsapp,
                                               @Query("person_email") String person_email,
                                               @Query("person_website") String person_website,
                                               @Query("person_birthdate") String person_birthdate,
                                               @Query("person_anniversary") String person_anniversary,
                                               @Query("person_event") String person_event,
                                               @Query("person_event_note") String person_event_note,
                                               @Query("person_note") String person_note,
                                               @Query("person_adhar_no") String person_adhar_no,
                                               @Query("person_pan_no") String person_pan_no,
                                               @Query("courier_address") String courier_address,
                                               @Query("print_label") String print_lable,
                                               @Query("same_as_office") String same_as_office,
                                               @Query("person_office_address") String person_office_address,
                                               @Query("person_office_country") String person_office_country,
                                               @Query("person_office_state") String person_office_state,
                                               @Query("person_office_city") String person_office_city,
                                               @Query("person_office_area") String person_office_area,
                                               @Query("person_office_district") String person_office_district,
                                               @Query("person_office_pincode") String person_office_pincode,
                                               @Query("person_account_name") String person_account_name,
                                               @Query("person_bank_name") String person_bank_name,
                                               @Query("person_bank_acc_no") String person_bank_acc_no,
                                               @Query("person_bank_ifsc") String person_bank_ifsc
    );

    // todo update Individual contact
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=7&type=Android")
    Call<ResponseBody> updateIndividualContact(@Query("user_id") String user_id,
                                               @Query("id") String id,
                                               @Query("label_id") String label_id,
                                               @Query("tag_id") String tag_id,
                                               @Query("person_name") String person_name,
                                               @Query("person_middle_name") String person_middle_name,
                                               @Query("person_surname") String person_surname,
                                               @Query("person_jobtitle") String person_jobtitle,
                                               @Query("person_home_address") String person_home_address,
                                               @Query("person_city") String person_city,
                                               @Query("person_area") String person_area,
                                               @Query("person_district") String person_district,
                                               @Query("person_state") String person_state,
                                               @Query("person_country") String person_country,
                                               @Query("person_pincode") String person_pincode,
                                               @Query("person_home_phone") String person_home_phone,
                                               @Query("person_mobile") String person_mobile,
                                               @Query("person_whatsapp") String person_whatsapp,
                                               @Query("person_email") String person_email,
                                               @Query("person_website") String person_website,
                                               @Query("person_birthdate") String person_birthdate,
                                               @Query("person_anniversary") String person_anniversary,
                                               @Query("person_event") String person_event,
                                               @Query("person_event_note") String person_event_note,
                                               @Query("person_note") String person_note,
                                               @Query("person_adhar_no") String person_adhar_no,
                                               @Query("person_pan_no") String person_pan_no,
                                               @Query("courier_address") String courier_address,
                                               @Query("print_label") String print_lable,
                                               @Query("same_as_office") String same_as_office,
                                               @Query("person_office_address") String person_office_address,
                                               @Query("person_office_country") String person_office_country,
                                               @Query("person_office_state") String person_office_state,
                                               @Query("person_office_city") String person_office_city,
                                               @Query("person_office_area") String person_office_area,
                                               @Query("person_office_district") String person_office_district,
                                               @Query("person_office_pincode") String person_office_pincode,
                                               @Query("person_account_name") String person_account_name,
                                               @Query("person_bank_name") String person_bank_name,
                                               @Query("person_bank_acc_no") String person_bank_acc_no,
                                               @Query("person_bank_ifsc") String person_bank_ifsc,
                                               @Part MultipartBody.Part file
    );


    // todo Add company person Individual contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=6&type=Android")
    Call<ResponseBody> addIndividualContact(@Query("user_id") String id,
                                            @Query("company_id") String company_id,
                                            @Query("employee_id") String employee_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("person_name") String person_name,
                                            @Query("person_middle_name") String person_middle_name,
                                            @Query("person_surname") String person_surname,
                                            @Query("person_jobtitle") String person_jobtitle,
                                            @Query("person_home_address") String person_home_address,
                                            @Query("person_city") String person_city,
                                            @Query("person_area") String person_area,
                                            @Query("person_district") String person_district,
                                            @Query("person_state") String person_state,
                                            @Query("person_country") String person_country,
                                            @Query("person_pincode") String person_pincode,
                                            @Query("person_home_phone") String person_home_phone,
                                            @Query("person_mobile") String person_mobile,
                                            @Query("person_whatsapp") String person_whatsapp,
                                            @Query("person_email") String person_email,
                                            @Query("person_website") String person_website,
                                            @Query("person_birthdate") String person_birthdate,
                                            @Query("person_anniversary") String person_anniversary,
                                            @Query("person_event") String person_event,
                                            @Query("person_event_note") String person_event_note,
                                            @Query("person_note") String person_note,
                                            @Query("person_adhar_no") String person_adhar_no,
                                            @Query("person_pan_no") String person_pan_no,
                                            @Query("courier_address") String courier_address,
                                            @Query("print_label") String print_lable,
                                            @Query("same_as_office") String same_as_office,
                                            @Query("person_office_address") String person_office_address,
                                            @Query("person_office_country") String person_office_country,
                                            @Query("person_office_state") String person_office_state,
                                            @Query("person_office_city") String person_office_city,
                                            @Query("person_office_area") String person_office_area,
                                            @Query("person_office_district") String person_office_district,
                                            @Query("person_office_pincode") String person_office_pincode,
                                            @Query("person_account_name") String person_account_name,
                                            @Query("person_bank_name") String person_bank_name,
                                            @Query("person_bank_acc_no") String person_bank_acc_no,
                                            @Query("person_bank_ifsc") String person_bank_ifsc
    );

    // todo Add company person Individual contact
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=6&type=Android")
    Call<ResponseBody> addIndividualContact(@Query("user_id") String id,
                                            @Query("company_id") String company_id,
                                            @Query("employee_id") String employee_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("person_name") String person_name,
                                            @Query("person_middle_name") String person_middle_name,
                                            @Query("person_surname") String person_surname,
                                            @Query("person_jobtitle") String person_jobtitle,
                                            @Query("person_home_address") String person_home_address,
                                            @Query("person_city") String person_city,
                                            @Query("person_area") String person_area,
                                            @Query("person_district") String person_district,
                                            @Query("person_state") String person_state,
                                            @Query("person_country") String person_country,
                                            @Query("person_pincode") String person_pincode,
                                            @Query("person_home_phone") String person_home_phone,
                                            @Query("person_mobile") String person_mobile,
                                            @Query("person_whatsapp") String person_whatsapp,
                                            @Query("person_email") String person_email,
                                            @Query("person_website") String person_website,
                                            @Query("person_birthdate") String person_birthdate,
                                            @Query("person_anniversary") String person_anniversary,
                                            @Query("person_event") String person_event,
                                            @Query("person_event_note") String person_event_note,
                                            @Query("person_note") String person_note,
                                            @Query("person_adhar_no") String person_adhar_no,
                                            @Query("person_pan_no") String person_pan_no,
                                            @Query("courier_address") String courier_address,
                                            @Query("print_label") String print_lable,
                                            @Query("same_as_office") String same_as_office,
                                            @Query("person_office_address") String person_office_address,
                                            @Query("person_office_country") String person_office_country,
                                            @Query("person_office_state") String person_office_state,
                                            @Query("person_office_city") String person_office_city,
                                            @Query("person_office_area") String person_office_area,
                                            @Query("person_office_district") String person_office_district,
                                            @Query("person_office_pincode") String person_office_pincode,
                                            @Query("person_account_name") String person_account_name,
                                            @Query("person_bank_name") String person_bank_name,
                                            @Query("person_bank_acc_no") String person_bank_acc_no,
                                            @Query("person_bank_ifsc") String person_bank_ifsc,
                                            @Part MultipartBody.Part file
    );

    // todo update company person Individual contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=7&type=Android")
    Call<ResponseBody> updateIndividualContact(@Query("user_id") String user_id,
                                               @Query("company_id") String company_id,
                                               @Query("id") String id,
                                               @Query("employee_id") String employee_id,
                                               @Query("tag_id") String tag_id,
                                               @Query("person_name") String person_name,
                                               @Query("person_middle_name") String person_middle_name,
                                               @Query("person_surname") String person_surname,
                                               @Query("person_jobtitle") String person_jobtitle,
                                               @Query("person_home_address") String person_home_address,
                                               @Query("person_city") String person_city,
                                               @Query("person_area") String person_area,
                                               @Query("person_district") String person_district,
                                               @Query("person_state") String person_state,
                                               @Query("person_country") String person_country,
                                               @Query("person_pincode") String person_pincode,
                                               @Query("person_home_phone") String person_home_phone,
                                               @Query("person_mobile") String person_mobile,
                                               @Query("person_whatsapp") String person_whatsapp,
                                               @Query("person_email") String person_email,
                                               @Query("person_website") String person_website,
                                               @Query("person_birthdate") String person_birthdate,
                                               @Query("person_anniversary") String person_anniversary,
                                               @Query("person_event") String person_event,
                                               @Query("person_event_note") String person_event_note,
                                               @Query("person_note") String person_note,
                                               @Query("person_adhar_no") String person_adhar_no,
                                               @Query("person_pan_no") String person_pan_no,
                                               @Query("courier_address") String courier_address,
                                               @Query("print_label") String print_lable,
                                               @Query("same_as_office") String same_as_office,
                                               @Query("person_office_address") String person_office_address,
                                               @Query("person_office_country") String person_office_country,
                                               @Query("person_office_state") String person_office_state,
                                               @Query("person_office_city") String person_office_city,
                                               @Query("person_office_area") String person_office_area,
                                               @Query("person_office_district") String person_office_district,
                                               @Query("person_office_pincode") String person_office_pincode,
                                               @Query("person_account_name") String person_account_name,
                                               @Query("person_bank_name") String person_bank_name,
                                               @Query("person_bank_acc_no") String person_bank_acc_no,
                                               @Query("person_bank_ifsc") String person_bank_ifsc
    );

    // todo update company person Individual contact
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=7&type=Android")
    Call<ResponseBody> updateIndividualContact(@Query("user_id") String user_id,
                                               @Query("company_id") String company_id,
                                               @Query("id") String id,
                                               @Query("employee_id") String employee_id,
                                               @Query("tag_id") String tag_id,
                                               @Query("person_name") String person_name,
                                               @Query("person_middle_name") String person_middle_name,
                                               @Query("person_surname") String person_surname,
                                               @Query("person_jobtitle") String person_jobtitle,
                                               @Query("person_home_address") String person_home_address,
                                               @Query("person_city") String person_city,
                                               @Query("person_area") String person_area,
                                               @Query("person_district") String person_district,
                                               @Query("person_state") String person_state,
                                               @Query("person_country") String person_country,
                                               @Query("person_pincode") String person_pincode,
                                               @Query("person_home_phone") String person_home_phone,
                                               @Query("person_mobile") String person_mobile,
                                               @Query("person_whatsapp") String person_whatsapp,
                                               @Query("person_email") String person_email,
                                               @Query("person_website") String person_website,
                                               @Query("person_birthdate") String person_birthdate,
                                               @Query("person_anniversary") String person_anniversary,
                                               @Query("person_event") String person_event,
                                               @Query("person_event_note") String person_event_note,
                                               @Query("person_note") String person_note,
                                               @Query("person_adhar_no") String person_adhar_no,
                                               @Query("person_pan_no") String person_pan_no,
                                               @Query("courier_address") String courier_address,
                                               @Query("print_label") String print_lable,
                                               @Query("same_as_office") String same_as_office,
                                               @Query("person_office_address") String person_office_address,
                                               @Query("person_office_country") String person_office_country,
                                               @Query("person_office_state") String person_office_state,
                                               @Query("person_office_city") String person_office_city,
                                               @Query("person_office_area") String person_office_area,
                                               @Query("person_office_district") String person_office_district,
                                               @Query("person_office_pincode") String person_office_pincode,
                                               @Query("person_account_name") String person_account_name,
                                               @Query("person_bank_name") String person_bank_name,
                                               @Query("person_bank_acc_no") String person_bank_acc_no,
                                               @Query("person_bank_ifsc") String person_bank_ifsc,
                                               @Part MultipartBody.Part file
    );

    // todo Company Contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=10&type=Android")
    Call<ResponseBody> getCompanyContactHistory(@Query("user_id") String user_id);

    // todo Individual Contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=11&type=Android")
    Call<ResponseBody> getIndividualContactHistory(@Query("user_id") String user_id);

    // todo Individual Contact
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=11&type=Android")
    Call<ResponseBody> getIndividualContactHistory(@Query("user_id") String user_id,
                                                   @Query("company_id") String company_id);

    // todo Add Courier
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=12&type=Android")
    Call<ResponseBody> addCourier(@Query("user_id") String id,
                                  @Query("assigned_person_name") String assigned_person_name,
                                  @Query("assigned_person_mobile") String assigned_person_mobile,
                                  @Query("sender_company") String sender_company,
                                  @Query("sender_person") String sender_person,
                                  @Query("sender_contact_no") String sender_contact_no,
                                  @Query("sender_address") String sender_address,
                                  @Query("sender_area") String sender_area,
                                  @Query("sender_city") String sender_city,
                                  @Query("sender_district") String sender_district,
                                  @Query("sender_state") String sender_state,
                                  @Query("sender_country") String sender_country,
                                  @Query("sender_pincode") String sender_pincode,
                                  @Query("sender_zone") String sender_zone,
                                  @Query("receiver_company_name") String receiver_company_name,
                                  @Query("receiver_person_name") String receiver_person_name,
                                  @Query("receiver_mobile") String receiver_mobile,
                                  @Query("receiver_address") String receiver_address,
                                  @Query("receiver_area") String receiver_area,
                                  @Query("receiver_city") String receiver_city,
                                  @Query("receiver_district") String receiver_district,
                                  @Query("receiver_state") String receiver_state,
                                  @Query("receiver_country") String receiver_country,
                                  @Query("receiver_pincode") String receiver_pincode,
                                  @Query("receiver_zone") String receiver_zone,
                                  @Query("parcel_type") String parcel_type,
                                  @Query("parcel_description") String parcel_description,
                                  @Query("parcel_weight") String parcel_weight,
                                  @Query("parcel_cost") String parcel_cost,
                                  @Query("courier_company_name") String courier_company_name,
                                  @Query("pickup_person_name") String pickup_person_name,
                                  @Query("delivered_date") String delivered_date,
                                  @Query("tracking_no") String tracking_no,
                                  @Query("delivery_status") String delivery_status,
                                  @Query("courier_type") String courier_type,
                                  @Query("gm_value") String gm_value,
                                  @Query("kg_value") String kg_value
    );


    // todo get courier
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=18&type=Android")
    Call<ResponseBody> getCourierHistory(@Query("user_id") String user_id,
                                         @Query("todate") String to_date,
                                         @Query("fromdate") String from_date,
                                         @Query("mode") String mode,
                                         @Query("courier_list") String courier_list
    );

    // todo Notification
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=46&type=Android")
    Call<ResponseBody> getNotification(@Query("user_id") String user_id,
                                       @Query("ul") int ul,
                                       @Query("ll") int ll
    );

    // todo search Courier history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=17&type=Android")
    io.reactivex.Observable<ResponseBody> searchCourierHistory(@Query("user_id") String user_id,
                                                               @Query("query") String query,
                                                               @Query("courier_list") String courier_list
    );

    // todo search Company contact history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=23&type=Android")
    io.reactivex.Observable<ResponseBody> searchCompanyContactHistory(@Query("user_id") String user_id,
                                                                      @Query("query") String query
    );

    // todo get courier
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=14&type=Android")
    io.reactivex.Observable<ResponseBody> getCourierHistory1(@Query("user_id") String user_id
    );

    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=14&type=Android")
    Call<ResponseBody> getCourierHistory(@Query("user_id") String user_id,
                                         @Query("courier_list") String courier_list
    );

    // todo search Company contact history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=23&type=Android")
    Call<ResponseBody> filterCompanyContactHistory(@Query("user_id") String user_id,
                                                   @Query("employee_id") String employee_id,
                                                   @Query("tag_id") String tag_id,
                                                   @Query("label_id") String label_id,
                                                   @Query("city") String city,
                                                   @Query("zone") String zone,
                                                   @Query("district") String district,
                                                   @Query("query") String query,
                                                   @Query("mobile") String mobile,
                                                   @Query("pincode") String pincode
    );

    // todo Export contact history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=98&type=Android")
    Call<ResponseBody> ExportContactHistory(@Query("user_id") String user_id,
                                            @Query("tag_id") String tag_id,
                                            @Query("label_id") String label_id,
                                            @Query("city") String city,
                                            @Query("zone") String zone,
                                            @Query("selectedtype") String selectedtype
    );

    // todo search Individual contact history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=24&type=Android")
    io.reactivex.Observable<ResponseBody> searchIndividualContactHistory(@Query("user_id") String user_id,
                                                                         @Query("query") String query
    );

    // todo search Individual contact history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=24&type=Android")
    Call<ResponseBody> filterIndividualContactHistory(@Query("user_id") String user_id,
                                                      @Query("employee_id") String employee_id,
                                                      @Query("tag_id") String tag_id,
                                                      @Query("label_id") String label_id,
                                                      @Query("city") String city,
                                                      @Query("district") String district,
                                                      @Query("query") String query,
                                                      @Query("mobile") String mobile,
                                                      @Query("pincode") String pincode
    );

    // todo add Label
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=19&type=Android")
    Call<ResponseBody> addLabel(@Query("name") String name
    );

    // todo add Employee
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=20&type=Android")
    Call<ResponseBody> addEmployee(@Query("name") String name
    );

    // todo add tag
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=21&type=Android")
    Call<ResponseBody> addTag(@Query("name") String name
    );

    // todo create directory
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=22&type=Android")
    Call<ResponseBody> createDirectory(@Query("resource_owner") String resource_owner,
                                       @Query("resource_title") String resource_title,
                                       @Query("resource_type") String resource_type,
                                       @Query("resource_parent") String resource_parent
    );

    // todo upload file
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=22&type=Android")
    Call<ResponseBody> uploadFile(@Query("resource_owner") String resource_owner,
                                  @Query("resource_type") String resource_type,
                                  @Query("resource_parent") String resource_parent,
                                  @Part MultipartBody.Part file
    );

    // todo get courier
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=25&type=Android")
    Call<ResponseBody> getDocumentHistory(@Query("resource_owner") String resource_owner,
                                          @Query("resource_parent") String resource_parent
    );

    // todo get courier
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=25&type=Android")
    Call<ResponseBody> getDocumentHistory(@Query("resource_owner") String resource_owner,
                                          @Query("resource_parent") String resource_parent,
                                          @Query("sort") String sort,
                                          @Query("query") String query
    );

    // todo search Document history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=25&type=Android")
    io.reactivex.Observable<ResponseBody> searchDocumentHistory(@Query("resource_owner") String resource_owner,
                                                                @Query("resource_parent") String resource_parent,
                                                                @Query("sort") String sort,
                                                                @Query("query") String query
    );

    // todo get Resource Visibility Public
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=35&type=Android")
    Call<ResponseBody> getResourceVisibilityPublic(@Query("resource_owner") String resource_owner,
                                                   @Query("resource_id") String resource_id
    );

    // todo get Resource Visibility Private
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=36&type=Android")
    Call<ResponseBody> getResourceVisibilityPrivate(@Query("resource_owner") String resource_owner,
                                                    @Query("resource_id") String resource_id
    );

    // todo get Resource Visibility Private
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=37&type=Android")
    Call<ResponseBody> getPerson(@Query("resource_owner") String resource_owner,
                                 @Query("resource_id") String resource_id
    );

    // todo share Resource Private
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=33&type=Android")
    Call<ResponseBody> shareResourcePrivate(@Query("resource_owner") String resource_owner,
                                            @Query("resource_id") String resource_id,
                                            @Query("users") String users
    );

    // todo get check
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=27&type=Android")
    Call<ResponseBody> addCheque(@Query("user_id") String user_id,
                                 @Query("bank_name") String bank_name,
                                 @Query("company_name") String company_name,
                                 @Query("cheque_no") String cheque_no,
                                 @Query("party_name") String party_name,
                                 @Query("cheque_type") String cheque_type,
                                 @Query("cheque_date") String cheque_date,
                                 @Query("amount") String amount,
                                 @Query("cheque_add") String cheque_add
    );

    // todo get check
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=31&type=Android")
    Call<ResponseBody> updateCheque(@Query("user_id") String user_id,
                                    @Query("id") String id,
                                    @Query("bank_name") String bank_name,
                                    @Query("company_name") String company_name,
                                    @Query("cheque_no") String cheque_no,
                                    @Query("party_name") String party_name,
                                    @Query("cheque_type") String cheque_type,
                                    @Query("cheque_date") String cheque_date,
                                    @Query("amount") String amount,
                                    @Query("cheque_add") String cheque_add
    );

    // todo get check
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=28&type=Android")
    Call<ResponseBody> getChequeHistory(@Query("user_id") String user_id,
                                        @Query("cheque_type") String cheque_type
    );

    // todo get check

    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=32&type=Android")
    Call<ResponseBody> deleteCheque(@Query("user_id") String user_id,
                                    @Query("id") String id,
                                    @Query("cheque_type") String cheque_type
    );

    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=32&type=Android")
    Call<ResponseBody> deleteCheque(@Query("user_id") String user_id,
                                    @Query("id") String id
    );

    // todo search Cheque history
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=29&type=Android")
    io.reactivex.Observable<ResponseBody> searchChequeHistory(@Query("user_id") String user_id,
                                                              @Query("query") String query,
                                                              @Query("cheque_type") String cheque_type
    );

    // todo filter cheque
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=30&type=Android")
    Call<ResponseBody> filterChequeHistory(@Query("user_id") String user_id,
                                           @Query("todate") String to_date,
                                           @Query("fromdate") String from_date,
                                           @Query("cheque_type") String cheque_type
    );

    // todo get chat room
    @POST("service_chat.php?" + secure_field + "=" + secure_value + "&s=21&user_type=android")
    Call<ResponseBody> getChatRoom(@Query("eid") String eid,
                                   @Query("etype") String etype);


    // todo task status
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=43&type=Android")
    Call<ResponseBody> getTaskStatus(@Query("user_id") String user_id
    );

    // todo task status
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=40&type=Android")
    Call<ResponseBody> getTask(@Query("user_id") String user_id,
                               @Query("status") String status,
                               @Query("assigned_by") String task_assigned_by,
                               @Query("assigned_to") String task_assigned_to,
                               @Query("query") String task_name,
                               @Query("to_date") String to_date,
                               @Query("from_date") String from_date,
                               @Query("show_hidden_task") String show_hidden_task,
                               @Query("task_type") String task_type,
                               @Query("admin") String admin
    );

    // todo export task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=95&type=Android")
    Call<ResponseBody> exportTask(@Query("user_id") String user_id,
                                  @Query("status") String status,
                                  @Query("assigned_by") String task_assigned_by,
                                  @Query("assigned_to") String task_assigned_to,
                                  @Query("query") String task_name,
                                  @Query("to_date") String to_date,
                                  @Query("from_date") String from_date,
                                  @Query("show_hidden_task") String show_hidden_task,
                                  @Query("task_type") String task_type,
                                  @Query("admin") String admin
    );

    // todo task status
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=44&type=Android")
    Call<ResponseBody> getTaskUtils(@Query("user_id") String user_id
    );

    // todo create task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=38&type=Android")
    Call<ResponseBody> createTask(@Query("user_id") String user_id,
                                  @Query("status") String status,
                                  @Query("task_name") String task_name,
                                  @Query("task_type") String task_type,
                                  @Query("task_assigned_by") String task_assigned_by,
                                  @Query("task_assigned_to") String task_assigned_to,
                                  @Query("task_description") String task_description,
                                  @Query("task_color_tag") String task_color_tag,
                                  @Query("task_deadline") String task_deadline,
                                  @Query("task_priority") String task_priority
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=99&type=Android")
    Call<ResponseBody> getTaskLog(@Query("user_id") String user_id,
                                  @Query("task_id") String task_id
    );

    // todo create task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=45&type=Android")
    Call<ResponseBody> updateTask(@Query("user_id") String user_id,
                                  @Query("tid") String tid,
                                  @Query("status") String status,
                                  @Query("task_name") String task_name,
                                  @Query("task_type") String task_type,
                                  @Query("task_assigned_by") String task_assigned_by,
                                  @Query("task_assigned_to") String task_assigned_to,
                                  @Query("task_description") String task_description,
                                  @Query("task_color_tag") String task_color_tag,
                                  @Query("task_deadline") String task_deadline,
                                  @Query("task_priority") String task_priority
    );

    // todo archive task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=41&type=Android")
    Call<ResponseBody> archiveTask(@Query("user_id") String user_id,
                                   @Query("tid") String tid,
                                   @Query("old_status") String old_status,
                                   @Query("new_status") String new_status

    );

    // todo delete task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=42&type=Android")
    Call<ResponseBody> deleteTask(@Query("user_id") String user_id,
                                  @Query("tid") String tid

    );

    // todo delete task
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=39&type=Android")
    Call<ResponseBody> createNote(@Query("user_id") String user_id,
                                  @Query("task_id") String task_id,
                                  @Query("task_stage") String task_stage,
                                  @Query("task_note_description") String task_note_description

    );

    // todo delete task
    @Multipart
    @POST("service_user.php?" + secure_field + "=" + secure_value + "&s=39&type=Android")
    Call<ResponseBody> createNote(@Query("user_id") String user_id,
                                  @Query("task_id") String task_id,
                                  @Query("task_stage") String task_stage,
                                  @Query("task_note_description") String task_note_description,
                                  @Part MultipartBody.Part file

    );

    // todo get catalogue
    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=46&type=Android")
    Call<ResponseBody> getCatalogue(@Query("user_id") String user_id
    );

    // todo get catalogue
    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=48&type=Android")
    Call<ResponseBody> getCatalogue(@Query("user_id") String user_id,
                                    @Query("todate") String to_date,
                                    @Query("fromdate") String from_date
    );

    // todo search Cheque history
    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=47&type=Android")
    io.reactivex.Observable<ResponseBody> getCatalogue(@Query("user_id") String user_id,
                                                       @Query("query") String query
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=49&type=Android")
    Call<ResponseBody> addReceptionEntery(@Query("user_id") String user_id,
                                          @Query("type") String type,
                                          @Query("tag_id") String tag_id,
                                          @Query("label_id") String label_id,
                                          @Query("title") String title,
                                          @Query("person_name") String person_name,
                                          @Query("company_name") String company_name,
                                          @Query("mobile_no") String mobile_no,
                                          @Query("email") String email,
                                          @Query("whom_to_meet") String whom_to_meet,
                                          @Query("reception_detail") String reception_detail,
                                          @Query("city") String city,
                                          @Query("visitor_name") String visitor_name
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=49&type=Android")
    Call<ResponseBody> addReceptionEntery1(@Query("user_id") String user_id,
                                           @Query("type") String type,
                                           @Query("tag_id") String tag_id,
                                           @Query("label_id") String label_id,
                                           @Query("title") String title,
                                           @Query("person_name") String person_name,
                                           @Query("company_name") String company_name,
                                           @Query("mobile_no") String mobile_no,
                                           @Query("email") String email,
                                           @Query("whom_to_call") String whom_to_call,
                                           @Query("reception_detail") String reception_detail,
                                           @Query("city") String city,
                                           @Query("caller_name") String caller_name
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=60&type=Android")
    Call<ResponseBody> addCurrency(@Query("user_id") String user_id,
                                   @Query("title") String title,
                                   @Query("description") String description,
                                   @Query("item") String item
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=74&type=Android")
    Call<ResponseBody> updateCurrency(@Query("user_id") String user_id,
                                      @Query("id") String id,
                                      @Query("title") String title,
                                      @Query("description") String description,
                                      @Query("item") String item
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=73&type=Android")
    Call<ResponseBody> getCurrencyHistory(@Query("user_id") String user_id
    );

    // todo get catalogue
    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=50&type=Android")
    Call<ResponseBody> getReceptionHistory(@Query("user_id") String user_id,
                                           @Query("type") String type
    );

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=53&type=Android")
    Call<ResponseBody> addCategory(@Query("user_id") String user_id,
                                   @Query("category") String category);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=56&type=Android")
    Call<ResponseBody> getCategory(@Query("user_id") String user_id);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=55&type=Android")
    Call<ResponseBody> getNoteHistory(@Query("user_id") String user_id,
                                      @Query("category_id") String category_id);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=55&type=Android")
    Call<ResponseBody> getNoteHistory(@Query("user_id") String user_id,
                                      @Query("category_id") String category_id,
                                      @Query("ul") int ul,
                                      @Query("ll") int ll);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=54&type=Android")
    Call<ResponseBody> addNotes(@Query("user_id") String user_id,
                                @Query("title") String title,
                                @Query("description") String description,
                                @Query("category_id") String category_id);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=54&type=Android")
    Call<ResponseBody> updateNotes(@Query("user_id") String user_id,
                                   @Query("id") String title,
                                   @Query("description") String description,
                                   @Query("id") String id);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=55&type=Android")
    Call<ResponseBody> deleteNote(@Query("user_id") String user_id,
                                  @Query("id") String idd);


    /* todo visitor book api */

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=64")
    Call<ResponseBody> getCategory();


    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=72")
    Call<ResponseBody> getProjectManager(@Query("project_id") String project_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=62")
    Call<ResponseBody> add_view_data(@Query("user_id") String user_id,
                                     @Query("name") String name,
                                     @Query("mobile_no") String mobile_no,
                                     @Query("email") String email,
                                     @Query("detail") String detail,
                                     @Query("save_type") String save_type,
                                     @Query("category_id") int category_id,
                                     @Query("reference") String reference,
                                     @Query("project_id") String project_id,
                                     @Query("project_manager_id") String project_manager_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=63")
    Call<ResponseBody> view_history(@Query("user_id") String user_id,
                                    @Query("project_id") String project_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=63")
    Call<ResponseBody> search_view_history(@Query("user_id") String user_id,
                                           @Query("project_id") String project_id,
                                           @Query("searchName") String searchName,
                                           @Query("todate") String tdate,
                                           @Query("fromdate") String fdate,
                                           @Query("category_id") String category_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=11")
    Call<ResponseBody> addRestuarantRate(@Query("visitor_id") String visitor_id,
                                         @Query("rating") String rating,
                                         @Query("remark") String remark);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=68")
    Call<ResponseBody> getFollowupHistory(@Query("user_id") String user_id,
                                          @Query("project_id") String project_id,
                                          @Query("visitor_id") String visitor_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=70")
    Call<ResponseBody> getTodaysFollowup(@Query("user_id") String user_id,
                                         @Query("project_id") String project_id);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=65")
    Call<ResponseBody> addFollowup(@Query("user_id") String user_id,
                                   @Query("visitor_id") String visitor_id,
                                   @Query("description") String description,
                                   @Query("through") String through,
                                   @Query("followup_date") String followup_date);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=66")
    Call<ResponseBody> addFollowupResponse(@Query("user_id") String user_id,
                                           @Query("followup_id") String followup_id,
                                           @Query("response") String response,
                                           @Query("followup_action") String followup_action,
                                           @Query("followup_future_date") String followup_future_date);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=67")
    Call<ResponseBody> updateFollowupResponse(@Query("user_id") String user_id,
                                              @Query("followup_id") String followup_id,
                                              @Query("response") String response);

    @POST("service_visitor.php?" + secure_field + "=" + secure_value + "&s=71")
    Call<ResponseBody> getFollowUpDetail(@Query("followup_id") String followup_id);

    /* end */


    /* todo music */

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=58&type=Android")
    Call<ResponseBody> getMusicCategory();

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=59&type=Android")
    Call<ResponseBody> getMusicSubCategory(@Query("cid") String cid);

    @POST("utility_service.php?" + secure_field + "=" + secure_value + "&s=57&type=Android")
    Call<ResponseBody> getMusicList(@Query("cid") String cid,
                                    @Query("sid") String sid);

    /* todo */

    /* todo tracking */

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=80")
    Call<ResponseBody> salesTrackingUpdate(@Query("user_id") String user_id,
                                           @Body JSONArray tracking);
    /* todo */

    /* todo employee modual*/

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=75&type=Android")
    Call<ResponseBody> addLeaveRequest(@Query("user_id") String user_id,
                                       @Query("start_date") String start_date,
                                       @Query("start_time") String start_time,
                                       @Query("end_date") String end_date,
                                       @Query("end_time") String end_time,
                                       @Query("reason") String reason
    );

    @Multipart
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=75&type=Android")
    Call<ResponseBody> addLeaveRequest(@Query("user_id") String user_id,
                                       @Query("start_date") String start_date,
                                       @Query("start_time") String start_time,
                                       @Query("end_date") String end_date,
                                       @Query("end_time") String end_time,
                                       @Query("reason") String reason,
                                       @Part MultipartBody.Part file
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=79&type=Android")
    Call<ResponseBody> getLeaveHistory(@Query("user_id") String user_id);

    @POST("leave_service.php?" + secure_field1 + "=" + secure_value1 + "&s=76&type=Android")
    Call<ResponseBody> getExpanceCategory();


    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=78&type=Android")
    Call<ResponseBody> getExpanceHistory(@Query("user_id") String user_id);


    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=77&type=Android")
    Call<ResponseBody> addExpance(@Query("user_id") String user_id,
                                  @Query("category_id") String category_id,
                                  @Query("amount") String amount,
                                  @Query("remark") String remark,
                                  @Query("note") String note,
                                  @Query("type") String type,
                                  @Query("expense_date") String expance_date
    );

    @Multipart
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=77&type=Android")
    Call<ResponseBody> addExpance(@Query("user_id") String user_id,
                                  @Query("category_id") String category_id,
                                  @Query("amount") String amount,
                                  @Query("remark") String remark,
                                  @Query("note") String note,
                                  @Query("type") String type,
                                  @Query("expense_date") String expance_date,
                                  @Part MultipartBody.Part file
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=83&type=Android")
    Call<ResponseBody> updateExpanceStatus(@Query("user_id") String user_id,
                                           @Query("expense_id") String expense_id,
                                           @Query("status") String status
    );


    /* todo daily sales report */
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=86&type=Android")
    Call<ResponseBody> getSalesReport(@Query("user_id") String user_id
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=84&type=Android")
    Call<ResponseBody> addSalesReport(@Query("user_id") String user_id,
                                      @Query("company_name") String customer_name,
                                      @Query("address") String address,
                                      @Query("remark") String remark,
                                      @Query("person_name") String person_name,
                                      @Query("isSubmitted") String isSubmitted
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=84&type=Android")
    Call<ResponseBody> updateSalesReport(@Query("user_id") String user_id,
                                         @Query("company_name") String customer_name,
                                         @Query("address") String address,
                                         @Query("remark") String remark,
                                         @Query("person_name") String person_name,
                                         @Query("isSubmitted") String isSubmitted,
                                         @Query("id") String id,
                                         @Query("mode") String mode
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=85&type=Android")
    Call<ResponseBody> createSalesReportNote(@Query("user_id") String user_id,
                                             @Query("sales_report_id") String sales_report_id,
                                             @Query("note") String note

    );

    @Multipart
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=85&type=Android")
    Call<ResponseBody> createSalesReportAttachment(@Query("user_id") String user_id,
                                                   @Query("sales_report_id") String sales_report_id,
                                                   @Part MultipartBody.Part file
    );


    /* tod daily service report */

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=87&type=Android")
    Call<ResponseBody> addDailyServiceReport(@Query("user_id") String user_id,
                                             @Query("customer_name") String customer_name,
                                             @Query("address") String address,
                                             @Query("service_type_id") String service_type_id,
                                             @Query("remark") String remark,
                                             @Query("isSubmitted") String isSubmitted
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=87&type=Android")
    Call<ResponseBody> updateDailyServiceReport(@Query("user_id") String user_id,
                                                @Query("customer_name") String customer_name,
                                                @Query("address") String address,
                                                @Query("service_type_id") String service_type_id,
                                                @Query("remark") String remark,
                                                @Query("isSubmitted") String isSubmitted,
                                                @Query("id") String id,
                                                @Query("mode") String mode
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=89&type=Android")
    Call<ResponseBody> getDailyServiceReport(@Query("user_id") String user_id
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=93&type=Android")
    Call<ResponseBody> getServiceType(@Query("user_id") String user_id
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=88&type=Android")
    Call<ResponseBody> createServiceReportNote(@Query("user_id") String user_id,
                                               @Query("service_report_id") String sales_report_id,
                                               @Query("note") String note

    );

    @Multipart
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=88&type=Android")
    Call<ResponseBody> createServiceReportAttachment(@Query("user_id") String user_id,
                                                     @Query("service_report_id") String sales_report_id,
                                                     @Part MultipartBody.Part file
    );

    /* todo daily work report */

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=90&type=Android")
    Call<ResponseBody> addDailyWorkReport(@Query("user_id") String user_id,
                                          @Query("work_type") String work_type,
                                          @Query("address") String address,
                                          @Query("person_name") String person_name,
                                          @Query("remark") String remark,
                                          @Query("isSubmitted") String isSubmitted
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=90&type=Android")
    Call<ResponseBody> updateDailyWorkReport(@Query("user_id") String user_id,
                                             @Query("work_type") String work_type,
                                             @Query("address") String address,
                                             @Query("person_name") String person_name,
                                             @Query("remark") String remark,
                                             @Query("isSubmitted") String isSubmitted,
                                             @Query("id") String id,
                                             @Query("mode") String mode
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=92&type=Android")
    Call<ResponseBody> getDailyWorkReport(@Query("user_id") String user_id
    );

    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=91&type=Android")
    Call<ResponseBody> createWorkReportNote(@Query("user_id") String user_id,
                                            @Query("work_report_id") String sales_report_id,
                                            @Query("note") String note
    );

    @Multipart
    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=91&type=Android")
    Call<ResponseBody> createWorkReportAttachment(@Query("user_id") String user_id,
                                                  @Query("work_report_id") String sales_report_id,
                                                  @Part MultipartBody.Part file
    );


    @POST("leave_service.php?" + secure_field + "=" + secure_value + "&s=94&type=Android")
    Call<ResponseBody> deleteDailyReport(@Query("user_id") String user_id,
                                         @Query("report_id") String report_id,
                                         @Query("type") String type

    );

    /* todo */


    /* todo erp modual */

    String secure_field1 = "key";
    String secure_value1 = "1226";

    @GET("customer?" + secure_field1 + "=" + secure_value1 + "&s=15&type=Android")
    Call<ResponseBody> searchCustomer(@Query("user_id") String user_id
    );

    @GET("itemfg?" + secure_field1 + "=" + secure_value1 + "&s=1&type=Android")
    Call<ResponseBody> searchFinishGood(@Query("user_id") String user_id
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=105&type=Android")
    Call<ResponseBody> getQuatationRequest(@Query("user_id") String user_id,
                                           @Query("ul") String ul,
                                           @Query("ll") String ll,
                                           @Query("to_date") String to_date,
                                           @Query("from_date") String from_date
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=105&type=Android")
    io.reactivex.Observable<ResponseBody> searchQuatationRequest(@Query("user_id") String user_id,
                                                                 @Query("query") String query
    );


    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=105&flag=pending")
    Call<ResponseBody> getPendingQuatation(@Query("user_id") String user_id,
                                           @Query("customer_id") String customer_id,
                                           @Query("ul") String ul,
                                           @Query("ll") String ll,
                                           @Query("to_date") String to_date,
                                           @Query("from_date") String from_date
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=107&type=Android")
    Call<ResponseBody> getSalesOrder(@Query("user_id") String user_id,
                                     @Query("ul") String ul,
                                     @Query("ll") String ll,
                                     @Query("query") String query,
                                     @Query("to_date") String to_date,
                                     @Query("from_date") String from_date
    );



    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=107&type=Android")
    io.reactivex.Observable<ResponseBody> searchSalesOrder(@Query("user_id") String user_id,
                                                           @Query("query") String query
    );


    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=102")
    Call<ResponseBody> deleteQuatationRequest(@Query("user_id") String user_id,
                                              @Query("id") String id,
                                              @Query("type") String type
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=105")
    Call<ResponseBody> getQuatationRequestDetail(@Query("quotation_id") String quotation_id,
                                                 @Query("user_id") String user_id
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=101&type=Android&mode=add")
    Call<ResponseBody> addQuatationRequest(@Query("user_id") String user_id,
                                           @Query("customer_id") String customer_id,
                                           @Query("customer_name") String customer_name,
                                           @Query("customer_email") String customer_email,
                                           @Query("customer_phone_1") String customer_phone_1,
                                           @Query("customer_phone_2") String customer_phone_2,
                                           @Query("customer_address_1") String customer_address_1,
                                           @Query("customer_address_2") String customer_address_2,
                                           @Query("customer_landmark") String customer_landmark,
                                           @Query("customer_city") String customer_city,
                                           @Query("customer_state") String customer_state,
                                           @Query("customer_country") String customer_country,
                                           @Query("customer_pincode") String customer_pincode,
                                           @Query("customer_zone") String customer_zone,
                                           @Query("customer_area") String customer_area,
                                           @Query("customer_district") String customer_district,
                                           @Query("customer_website") String customer_website,
                                           @Query("customer_gst_no") String customer_gst_no,
                                           @Query("customer_pancard_no") String customer_pancard_no,
                                           @Query("quotation_date") String quotation_date,
                                           @Query("narration") String narration,
                                           @Query("terms_condition") String terms_dondition,
                                           @Query("discount") String discount,
                                           @Query("discount_type") String discount_type,
                                           @Query("item") String item
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=101&type=Android&mode=edit")
    Call<ResponseBody> updateQuatationRequest(@Query("user_id") String user_id,
                                              @Query("customer_id") String customer_id,
                                              @Query("customer_name") String customer_name,
                                              @Query("customer_email") String customer_email,
                                              @Query("customer_phone_1") String customer_phone_1,
                                              @Query("customer_phone_2") String customer_phone_2,
                                              @Query("customer_address_1") String customer_address_1,
                                              @Query("customer_address_2") String customer_address_2,
                                              @Query("customer_landmark") String customer_landmark,
                                              @Query("customer_city") String customer_city,
                                              @Query("customer_state") String customer_state,
                                              @Query("customer_country") String customer_country,
                                              @Query("customer_pincode") String customer_pincode,
                                              @Query("customer_zone") String customer_zone,
                                              @Query("customer_area") String customer_area,
                                              @Query("customer_district") String customer_district,
                                              @Query("customer_website") String customer_website,
                                              @Query("customer_gst_no") String customer_gst_no,
                                              @Query("customer_pancard_no") String customer_pancard_no,
                                              @Query("quotation_date") String quotation_date,
                                              @Query("narration") String narration,
                                              @Query("terms_condition") String terms_dondition,
                                              @Query("discount") String discount,
                                              @Query("discount_type") String discount_type,
                                              @Query("item") String item,
                                              @Query("id") String id
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=106&mode=add")
    Call<ResponseBody> addOrder(@Query("user_id") String user_id,
                                @Query("customer_id") String customer_id,
                                @Query("quotation_id") String quotation_id,
                                @Query("customer_name") String customer_name,
                                @Query("customer_email") String customer_email,
                                @Query("customer_phone_1") String customer_phone_1,
                                @Query("customer_phone_2") String customer_phone_2,
                                @Query("customer_address_1") String customer_address_1,
                                @Query("customer_address_2") String customer_address_2,
                                @Query("customer_landmark") String customer_landmark,
                                @Query("customer_city") String customer_city,
                                @Query("customer_state") String customer_state,
                                @Query("customer_country") String customer_country,
                                @Query("customer_pincode") String customer_pincode,
                                @Query("customer_zone") String customer_zone,
                                @Query("customer_area") String customer_area,
                                @Query("customer_district") String customer_district,
                                @Query("customer_website") String customer_website,
                                @Query("customer_gst_no") String customer_gst_no,
                                @Query("customer_pancard_no") String customer_pancard_no,
                                @Query("quotation_date") String quotation_date,
                                @Query("narration") String narration,
                                @Query("terms_condition") String terms_dondition,
                                @Query("discount") String discount,
                                @Query("discount_type") String discount_type,
                                @Query("item") String item
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=106&mode=edit")
    Call<ResponseBody> updateOrder(@Query("user_id") String user_id,
                                   @Query("id") String id,
                                   @Query("customer_id") String customer_id,
                                   @Query("quotation_id") String quotation_id,
                                   @Query("customer_name") String customer_name,
                                   @Query("customer_email") String customer_email,
                                   @Query("customer_phone_1") String customer_phone_1,
                                   @Query("customer_phone_2") String customer_phone_2,
                                   @Query("customer_address_1") String customer_address_1,
                                   @Query("customer_address_2") String customer_address_2,
                                   @Query("customer_landmark") String customer_landmark,
                                   @Query("customer_city") String customer_city,
                                   @Query("customer_state") String customer_state,
                                   @Query("customer_country") String customer_country,
                                   @Query("customer_pincode") String customer_pincode,
                                   @Query("customer_zone") String customer_zone,
                                   @Query("customer_area") String customer_area,
                                   @Query("customer_district") String customer_district,
                                   @Query("customer_website") String customer_website,
                                   @Query("customer_gst_no") String customer_gst_no,
                                   @Query("customer_pancard_no") String customer_pancard_no,
                                   @Query("quotation_date") String quotation_date,
                                   @Query("narration") String narration,
                                   @Query("terms_condition") String terms_dondition,
                                   @Query("discount") String discount,
                                   @Query("discount_type") String discount_type,
                                   @Query("item") String item
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=107")
    Call<ResponseBody> getOrderDetail(@Query("order_id") String order_id,
                                      @Query("user_id") String user_id
    );


    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=112")
    Call<ResponseBody> getMyPipelineStatus(@Query("user_id") String user_id
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=111")
    Call<ResponseBody> getMyPipelineTask(@Query("user_id") String user_id,
                                         @Query("status") String status_id
    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=120")
    Call<ResponseBody> getDealer(@Query("user_id") String user_id
    );


    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=110&mode=add")
    Call<ResponseBody> addDemo(@Query("user_id") String user_id,
                               @Query("customer_id") String customer_id,
                               @Query("customer_name") String customer_name,
                               @Query("customer_email") String customer_email,
                               @Query("customer_phone_1") String customer_phone_1,
                               @Query("customer_phone_2") String customer_phone_2,
                               @Query("customer_address_1") String customer_address_1,
                               @Query("customer_address_2") String customer_address_2,
                               @Query("customer_landmark") String customer_landmark,
                               @Query("customer_city") String customer_city,
                               @Query("customer_state") String customer_state,
                               @Query("customer_country") String customer_country,
                               @Query("customer_pincode") String customer_pincode,
                               @Query("customer_zone") String customer_zone,
                               @Query("customer_area") String customer_area,
                               @Query("customer_district") String customer_district,
                               @Query("customer_website") String customer_website,
                               @Query("customer_gst_no") String customer_gst_no,
                               @Query("customer_pancard_no") String customer_pancard_no,
                               @Query("narration") String narration,
                               @Query("terms_condition") String terms_dondition,
                               @Query("request_date") String request_date,
                               @Query("demo_priority") String demo_priority,
                               @Query("dealer_id") String dealer_id,
                               @Query("type") String type,
                               @Query("item_id") String item_id,
                               @Query("model") String model,
                               @Query("color") String color,
                               @Query("purchase_date") String purchase_date,
                               @Query("part_guarantee") String part_guarantee,
                               @Query("full_guarantee") String full_guarantee,
                               @Query("batch_no") String batch_no,
                               @Query("item") String item
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=110&mode=edit")
    Call<ResponseBody> updateDemo(@Query("user_id") String user_id,
                                  @Query("demo_id") String demo_id,
                                  @Query("customer_id") String customer_id,
                                  @Query("customer_name") String customer_name,
                                  @Query("customer_email") String customer_email,
                                  @Query("customer_phone_1") String customer_phone_1,
                                  @Query("customer_phone_2") String customer_phone_2,
                                  @Query("customer_address_1") String customer_address_1,
                                  @Query("customer_address_2") String customer_address_2,
                                  @Query("customer_landmark") String customer_landmark,
                                  @Query("customer_city") String customer_city,
                                  @Query("customer_state") String customer_state,
                                  @Query("customer_country") String customer_country,
                                  @Query("customer_pincode") String customer_pincode,
                                  @Query("customer_zone") String customer_zone,
                                  @Query("customer_area") String customer_area,
                                  @Query("customer_district") String customer_district,
                                  @Query("customer_website") String customer_website,
                                  @Query("customer_gst_no") String customer_gst_no,
                                  @Query("customer_pancard_no") String customer_pancard_no,
                                  @Query("narration") String narration,
                                  @Query("terms_condition") String terms_dondition,
                                  @Query("request_date") String request_date,
                                  @Query("demo_priority") String demo_priority,
                                  @Query("dealer_id") String dealer_id,
                                  @Query("type") String type,
                                  @Query("item_id") String item_id,
                                  @Query("model") String model,
                                  @Query("color") String color,
                                  @Query("purchase_date") String purchase_date,
                                  @Query("part_guarantee") String part_guarantee,
                                  @Query("full_guarantee") String full_guarantee,
                                  @Query("batch_no") String batch_no,
                                  @Query("item") String item
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=121&type=Android")
    Call<ResponseBody> createPipelineNote(@Query("user_id") String user_id,
                                          @Query("task_id") String task_id,
                                          @Query("task_stage") String task_stage,
                                          @Query("task_note") String task_note

    );

    @Multipart
    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=121&type=Android")
    Call<ResponseBody> createPipelineNote(@Query("user_id") String user_id,
                                          @Query("task_id") String task_id,
                                          @Query("task_stage") String task_stage,
                                          @Query("task_note") String task_note,
                                          @Part MultipartBody.Part file

    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=122")
    Call<ResponseBody> getPipelineViewTask(@Query("task_id") String task_id,
                                           @Query("user_id") String user_id
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=113&type=Android")
    Call<ResponseBody> deletePipelineTask(@Query("user_id") String user_id,
                                          @Query("tid") String tid

    );


    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=41&type=Android")
    Call<ResponseBody> archivePipelineTask(@Query("user_id") String user_id,
                                           @Query("demo_request_id") String tid,
                                           @Query("old_status") String old_status,
                                           @Query("new_status") String new_status

    );


    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=114&type=Android")
    Call<ResponseBody> movePipelineTask(@Query("user_id") String user_id,
                                        @Query("tid") String tid,
                                        @Query("old_status") String old_status,
                                        @Query("new_status") String new_status

    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=116")
    Call<ResponseBody> getTechnician(@Query("user_id") String user_id);

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=126")
    Call<ResponseBody> getTechnicianLog(@Query("user_id") String user_id,
                                        @Query("task_id") String task_id);

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=117")
    Call<ResponseBody> addTechnician(@Query("user_id") String user_id,
                                     @Query("demo_request_id") String demo_request_id,
                                     @Query("remarks") String remarks);

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=127&type=Android")
    Call<ResponseBody> getServiceInvoice(@Query("user_id") String user_id,
                                         @Query("ul") String ul,
                                         @Query("ll") String ll,
                                         @Query("query") String query,
                                         @Query("to_date") String to_date,
                                         @Query("from_date") String from_date
    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=127&type=Android")
    io.reactivex.Observable<ResponseBody> searchServiceInvoice(@Query("user_id") String user_id,
                                                               @Query("query") String query
    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=127")
    Call<ResponseBody> getServiceInvoiceDetail(@Query("invoice_id") String invoice_id,
                                               @Query("user_id") String user_id
    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=123")
    Call<ResponseBody> getServiceInvoicePipeline(@Query("demo_id") String demo_id,
                                                 @Query("user_id") String user_id
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=124&mode=add")
    Call<ResponseBody> addServiceInvoicePipeline(@Query("user_id") String user_id,
                                                 @Query("demo_id") String demo_id,
                                                 @Query("customer_id") String customer_id,
                                                 @Query("customer_name") String customer_name,
                                                 @Query("customer_email") String customer_email,
                                                 @Query("customer_phone_1") String customer_phone_1,
                                                 @Query("customer_phone_2") String customer_phone_2,
                                                 @Query("customer_address_1") String customer_address_1,
                                                 @Query("customer_address_2") String customer_address_2,
                                                 @Query("customer_landmark") String customer_landmark,
                                                 @Query("customer_city") String customer_city,
                                                 @Query("customer_state") String customer_state,
                                                 @Query("customer_country") String customer_country,
                                                 @Query("customer_pincode") String customer_pincode,
                                                 @Query("customer_zone") String customer_zone,
                                                 @Query("customer_area") String customer_area,
                                                 @Query("customer_district") String customer_district,
                                                 @Query("customer_website") String customer_website,
                                                 @Query("customer_gst_no") String customer_gst_no,
                                                 @Query("customer_pancard_no") String customer_pancard_no,
                                                 @Query("narration") String narration,
                                                 @Query("terms_condition") String terms_dondition,
                                                 @Query("discount") String discount,
                                                 @Query("discount_type") String discount_type,
                                                 @Query("item") String item
    );

    @POST("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=125")
    Call<ResponseBody> deleteServiceInvoice(@Query("user_id") String user_id,
                                            @Query("invoice_id") String invoice_id
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=129&type=Android")
    Call<ResponseBody> getSalesReturn(@Query("user_id") String user_id,
                                      @Query("ul") String ul,
                                      @Query("ll") String ll,
                                      @Query("query") String query,
                                      @Query("to_date") String to_date,
                                      @Query("from_date") String from_date
    );

    @GET("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=129&type=Android")
    io.reactivex.Observable<ResponseBody> searchSalesReturn(@Query("user_id") String user_id,
                                                           @Query("query") String query
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=130&mode=add")
    Call<ResponseBody> addSalesReturn(@Query("user_id") String user_id,
                                      @Query("customer_id") String customer_id,
                                      @Query("customer_name") String customer_name,
                                      @Query("customer_email") String customer_email,
                                      @Query("customer_phone_1") String customer_phone_1,
                                      @Query("customer_phone_2") String customer_phone_2,
                                      @Query("customer_address_1") String customer_address_1,
                                      @Query("customer_address_2") String customer_address_2,
                                      @Query("customer_landmark") String customer_landmark,
                                      @Query("customer_city") String customer_city,
                                      @Query("customer_state") String customer_state,
                                      @Query("customer_country") String customer_country,
                                      @Query("customer_pincode") String customer_pincode,
                                      @Query("customer_zone") String customer_zone,
                                      @Query("customer_area") String customer_area,
                                      @Query("customer_district") String customer_district,
                                      @Query("customer_website") String customer_website,
                                      @Query("customer_gst_no") String customer_gst_no,
                                      @Query("customer_pancard_no") String customer_pancard_no,
                                      @Query("narration") String narration,
                                      @Query("terms_condition") String terms_dondition,
                                      @Query("item") String item
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=129&type=Android")
    Call<ResponseBody> viewSalesReturn(@Query("user_id") String user_id,
                                       @Query("invoice_id") String invoice_id
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=128&type=Android")
    Call<ResponseBody> deleteSalesReturn(@Query("user_id") String user_id,
                                         @Query("invoice_id") String invoice_id
    );

    @POST("sales_quotation.php?" + secure_field1 + "=" + secure_value1 + "&s=131")
    Call<ResponseBody> SalesReturnStatus(@Query("user_id") String user_id,
                                         @Query("id") String id,
                                         @Query("mode") String mode
    );

    @GET("service_pipeline.php?" + secure_field1 + "=" + secure_value1 + "&s=119")
    io.reactivex.Observable<ResponseBody> getSearch1(@Query("batch_no") String batch_no
    );

    /* todo */
}
