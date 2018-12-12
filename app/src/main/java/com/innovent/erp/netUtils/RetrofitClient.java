package com.innovent.erp.netUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CRAFT BOX on 5/1/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit_erp = null;
    public static String service_url = "http://24.24.25.215/cdms_erp1/service/";
    //public static String service_url = "http://craftbox.in/server/innovant/service/";

    public static String video_url = "http://logixx.in/Freedom_video.mp4";
    public static String message_pack_name = "LOGIXX";

    /* todo erp link */

   // public static String service_url_erp = "http://24.24.25.215/cdms_erp1/service/";
    //public static String service_url_erp = "http://craftbox.in/server/innovant/service/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(36000, TimeUnit.SECONDS)
                    .connectTimeout(36000, TimeUnit.SECONDS)
                    .writeTimeout(1, TimeUnit.HOURS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(service_url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient1() {
        if (retrofit_erp == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(36000, TimeUnit.SECONDS)
                    .connectTimeout(36000, TimeUnit.SECONDS)
                    .writeTimeout(1, TimeUnit.HOURS)
                    .build();
            retrofit_erp = new Retrofit.Builder()
                    .baseUrl(service_url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit_erp;
    }
}
