package com.innovent.erp.netUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;


import com.innovent.erp.model.AreaModel;
import com.innovent.erp.model.GeneralModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "logixx.db";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;

    public static String Area = "area";
    public static String District = "district";
    public static String City = "city";
    public static String STATE = "state";
    public static String COUNTRY = "country";
    public static String Zone_master = "zone_master";

    public static String SALESEXECUTIVE_TRACKING = "salesexecutive_tracking";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    public void exportDB(Context con) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh;mm a");
        String Datetime = sdf.format(c.getTime());

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + DB_NAME;
        String backupDBPath = "/logixx/database/" + DB_NAME + " " + Datetime;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            try {
                encryptfile("" + backupDB, "154", con);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            Toast.makeText(con, "DB Exported in Sdcard or Inter Storage!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    public void importDB(File currentDB, String db_name) {
        // TODO Auto-generated method stub
        try {
            decrypt("" + currentDB, "154", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Exhibition/database/" + db_name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + DB_NAME;
                String backupDBPath = "/logixx/database/" + db_name;
                File backupDB = new File(data, currentDBPath);
                File currentDB1 = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB1).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                /*   */
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Exhibition/database/" + db_name);
                boolean deleted = file.delete();
                Toast.makeText(context, "Import successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void encryptfile(String path, String Pass, Context con) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        try {
            String attachment = "" + path.concat(".crypt");
            String salt = "t784";
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(path.concat(".crypt"));
            byte[] key = (salt + Pass).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
            cos.close();
            fis.close();
            File file = new File(path);
            boolean deleted = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String path, String password, String outPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String salt = "t784";
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(outPath);
        byte[] key = (salt + password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        //------------------------------------------------------------
        PackageInfo pinfo = null;
        if (!dbExist) {
            getReadableDatabase();
            copyDataBase();
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            Cursor c = db.rawQuery(Query, null);
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public boolean DeleteTable(String table_name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db.delete(table_name, "1", null) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* todo get perticuler colomn data */
    public String rp_getvalue(String table_name, String column_name, String where) {
        try {
            Cursor c2 = getData("select * from " + table_name + " where " + where + "");
            if (c2.getCount() > 0) {
                if (c2.moveToFirst()) {
                    String value = "" + c2.getString(c2.getColumnIndex("" + column_name));
                    c2.close();
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "0";
    }

    /* todo add salse latitude and longitude*/
    public void AddSalesTracking(String date, String latitude, String longitude, String type) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("latitude", "" + latitude);
            values.put("longitude", "" + longitude);
            values.put("date", "" + date);
            values.put("type", "" + type);
            long id = db.insert(SALESEXECUTIVE_TRACKING, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray GetSalesTracking() {
        JSONArray tracking_array = new JSONArray();
        try {
            Cursor c = getData("select * from " + SALESEXECUTIVE_TRACKING + "");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("date", c.getString(c.getColumnIndex("date")));
                    jsonObject.put("lat", c.getString(c.getColumnIndex("latitude")));
                    jsonObject.put("long", c.getString(c.getColumnIndex("longitude")));
                    jsonObject.put("type", c.getString(c.getColumnIndex("type")));
                    tracking_array.put(jsonObject);
                }
                return tracking_array;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tracking_array = null;
    }

    public ArrayList<AreaModel> getArea(String query) {
        ArrayList<AreaModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + Area + " where area like '" + query + "%' ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    AreaModel da = new AreaModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setArea("" + c.getString(c.getColumnIndex("area")));
                    da.setCity_id(c.getString(c.getColumnIndex("city_id")));
                    da.setDistrict_id(c.getString(c.getColumnIndex("district_id")));
                    da.setCountry_id(c.getString(c.getColumnIndex("country_id")));
                    da.setState_id(c.getString(c.getColumnIndex("state_id")));
                    da.setPincode_id(c.getString(c.getColumnIndex("id")));
                    da.setZone_id(c.getString(c.getColumnIndex("zone_id")));
                    da.setPincode(c.getString(c.getColumnIndex("pincode")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getCity() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {

            Cursor c = getData("select * from " + City + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("name")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getDistrict() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + District + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("name")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getState() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + STATE + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("name")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getCountry() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + COUNTRY + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("name")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getZone() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + Zone_master + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("name")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<GeneralModel> getPincode() {
        ArrayList<GeneralModel> data = new ArrayList<>();
        try {
            Cursor c = getData("select * from " + Area + " ");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString(c.getColumnIndex("id")));
                    da.setName("" + c.getString(c.getColumnIndex("pincode")));
                    data.add(da);
                }
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}