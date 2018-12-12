package com.innovent.erp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.innovent.erp.custom.FontSource;

public class GstCalculateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView calculate_txt, igst_txt, cgst_sgst_txt;
    private String kiri = "", kanan = "", value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_calculate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GST Calculator");
        init();
        if (savedInstanceState != null)
            calculate_txt.setText(savedInstanceState.getString("calculate_txt"));
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

    private void init() {
        Button one;
        Button two;
        Button three;
        Button forth;
        Button five;
        Button six;
        Button seven;
        Button eight;
        Button nine;
        Button point;
        Button zero;
        Button pluse;
        Button minuse;
        Button multiplaction;
        Button division;
        Button del;
        Button clear;
        Button _equles;

        Button three_gst_plus, five_gst_plus, twelve_gst_plus, eighteen_gst_plus, twentyEight_gst_plus;
        Button three_gst_minus, five_gst_minus, twelve_gst_minus, eighteen_gst_minus, twentyEight_gst_minus;

        calculate_txt = (TextView) findViewById(R.id.textView);
        igst_txt = (TextView) findViewById(R.id.igst_txt);
        cgst_sgst_txt = (TextView) findViewById(R.id.cgst_igst_txt);

        calculate_txt.setTypeface(FontSource.process(R.raw.digital, GstCalculateActivity.this));
        igst_txt.setTypeface(FontSource.process(R.raw.digital, GstCalculateActivity.this));
        cgst_sgst_txt.setTypeface(FontSource.process(R.raw.digital, GstCalculateActivity.this));

        zero = (Button) findViewById(R.id.button0);
        point = (Button) findViewById(R.id.button_point);
        one = (Button) findViewById(R.id.button1);
        two = (Button) findViewById(R.id.button2);
        three = (Button) findViewById(R.id.button3);
        forth = (Button) findViewById(R.id.button4);
        five = (Button) findViewById(R.id.button5);
        six = (Button) findViewById(R.id.button6);
        seven = (Button) findViewById(R.id.button7);
        eight = (Button) findViewById(R.id.button8);
        nine = (Button) findViewById(R.id.button9);
        pluse = (Button) findViewById(R.id.pluse);
        minuse = (Button) findViewById(R.id.minuse);
        multiplaction = (Button) findViewById(R.id.multiplaction);
        division = (Button) findViewById(R.id.division);
        del = (Button) findViewById(R.id.del);
        clear = (Button) findViewById(R.id.clear);
        _equles = (Button) findViewById(R.id._equles);

        three_gst_plus = (Button) findViewById(R.id.three_gst_plus);
        five_gst_plus = (Button) findViewById(R.id.five_gst_plus);
        twelve_gst_plus = (Button) findViewById(R.id.twelve_gst_plus);
        eighteen_gst_plus = (Button) findViewById(R.id.eighteen_gst_plus);
        twentyEight_gst_plus = (Button) findViewById(R.id.twentyEight_gst_plus);

        three_gst_minus = (Button) findViewById(R.id.three_gst_minus);
        five_gst_minus = (Button) findViewById(R.id.five_gst_minus);
        twelve_gst_minus = (Button) findViewById(R.id.twelve_gst_minus);
        eighteen_gst_minus = (Button) findViewById(R.id.eighteen_gst_minus);
        twentyEight_gst_minus = (Button) findViewById(R.id.twentyEight_gst_minus);

        point.setOnClickListener(this);
        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        forth.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        pluse.setOnClickListener(this);
        minuse.setOnClickListener(this);
        multiplaction.setOnClickListener(this);
        division.setOnClickListener(this);
        del.setOnClickListener(this);
        clear.setOnClickListener(this);
        _equles.setOnClickListener(this);

        three_gst_plus.setOnClickListener(this);
        five_gst_plus.setOnClickListener(this);
        twelve_gst_plus.setOnClickListener(this);
        eighteen_gst_plus.setOnClickListener(this);
        twentyEight_gst_plus.setOnClickListener(this);

        three_gst_minus.setOnClickListener(this);
        five_gst_minus.setOnClickListener(this);
        twelve_gst_minus.setOnClickListener(this);
        eighteen_gst_minus.setOnClickListener(this);
        twentyEight_gst_minus.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("calculate_txt", calculate_txt.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_point) {
            try {
                if (calculate_txt.getText().equals("0")) {
                    calculate_txt.setText(calculate_txt.getText() + ".");
                } else {
                    String calVal = calculate_txt.getText().toString().replaceAll("\\+", "/").replaceAll("-", "/").replaceAll("\\*", "/").replaceAll("\\÷", "/");
                    String val = calVal.substring(calVal.lastIndexOf("/") + 1);
                    if (val.contains(".")) {
                        if (val.equals("")) {
                            calculate_txt.setText(calculate_txt.getText() + "0.");
                        }
                    } else {
                        if (val.equals("")) {
                            awal();
                            calculate_txt.setText(calculate_txt.getText() + "0.");
                        } else {
                            awal();
                            calculate_txt.setText(calculate_txt.getText() + ".");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.button0) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "0");
        } else if (v.getId() == R.id.button1) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "1");
        } else if (v.getId() == R.id.button2) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "2");
        } else if (v.getId() == R.id.button3) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "3");
        } else if (v.getId() == R.id.button4) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "4");
        } else if (v.getId() == R.id.button5) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "5");
        } else if (v.getId() == R.id.button6) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "6");
        } else if (v.getId() == R.id.button7) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "7");
        } else if (v.getId() == R.id.button8) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "8");
        } else if (v.getId() == R.id.button9) {
            awal();
            calculate_txt.setText(calculate_txt.getText() + "9");
        } else if (v.getId() == R.id.pluse) {
            if (valid())
                calculate_txt.setText(calculate_txt.getText() + "+");
        } else if (v.getId() == R.id.minuse) {
            if (valid())
                calculate_txt.setText(calculate_txt.getText() + "-");
        } else if (v.getId() == R.id.multiplaction) {
            if (valid())
                calculate_txt.setText(calculate_txt.getText() + "*");
        } else if (v.getId() == R.id.division) {
            if (valid())
                calculate_txt.setText(calculate_txt.getText() + "÷");
        } else if (v.getId() == R.id.del) {
            hapusAngka();
        } else if (v.getId() == R.id.clear) {
            calculate_txt.setText("0");
            igst_txt.setText("");
            cgst_sgst_txt.setText("");
        } else if (v.getId() == R.id._equles) {
            hitung();
        } else if (v.getId() == R.id.three_gst_plus) {
            hitung();
            calculateGstPluse(3);
        } else if (v.getId() == R.id.five_gst_plus) {
            hitung();
            calculateGstPluse(5);
        } else if (v.getId() == R.id.twelve_gst_plus) {
            hitung();
            calculateGstPluse(12);
        } else if (v.getId() == R.id.eighteen_gst_plus) {
            hitung();
            calculateGstPluse(18);
        } else if (v.getId() == R.id.twentyEight_gst_plus) {
            hitung();
            calculateGstPluse(28);
        } else if (v.getId() == R.id.three_gst_minus) {
            hitung();
            calculateGstMinus(3);
        } else if (v.getId() == R.id.five_gst_minus) {
            hitung();
            calculateGstMinus(5);
        } else if (v.getId() == R.id.twelve_gst_minus) {
            hitung();
            calculateGstMinus(12);
        } else if (v.getId() == R.id.eighteen_gst_minus) {
            hitung();
            calculateGstMinus(18);
        } else if (v.getId() == R.id.twentyEight_gst_minus) {
            hitung();
            calculateGstMinus(28);
        }
    }

    void awal() {
        if (calculate_txt.getText().toString().equals("0"))
            calculate_txt.setText("");
    }

    void hapusAngka() {
        int panjang = calculate_txt.getText().toString().length();
        if (panjang > 0) {
            String temp = calculate_txt.getText().toString().substring(0, panjang - 1);
            calculate_txt.setText(temp);
        }
        if (calculate_txt.getText().toString().equals(""))
            calculate_txt.setText("0");
    }

    boolean valid() {
        char akhir = calculate_txt.getText().toString().toCharArray()[calculate_txt.getText().length() - 1];
        return !(akhir == '+' || akhir == '-' || akhir == '*' || akhir == '÷' || calculate_txt.getText().toString().equals("0"));
    }

    void hitung() {
        value = calculate_txt.getText().toString();
        value = value.replace('-', '_');
        if (stringDi(value, 0).equals("_"))
            value = "-" + value.substring(1);

        if (stringDi(value, value.length() - 1).equals("+") || stringDi(value, value.length() - 1).equals("_") || stringDi(value, value.length() - 1).equals("*") || stringDi(value, value.length() - 1).equals("÷"))
            value = value.substring(0, value.length() - 1);

        while (operasi(value, "*", "÷", "+", "_")) ;

        calculate_txt.setText(value);
    }

    String stringDi(String string, int pos) {
        return string.substring(pos, pos + 1);
    }

    boolean operasi(String soal, String op1, String op2, String op3, String op4) {
        boolean ada = false;
        for (int i = 0; i < soal.length(); i++) {
            if (stringDi(soal, i).equals(op1)) {
                ada = true;
                double x = 0l;
                switch (op1) {
                    case "*":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) * Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "÷":
                        if (Double.parseDouble(ambilAngkaKanan(soal, i)) != 0)
                            x = Double.parseDouble(ambilAngkaKiri(soal, i)) / Double.parseDouble(ambilAngkaKanan(soal, i));
                        else
                            x = Long.MAX_VALUE;
                        break;
                    case "+":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) + Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "_":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) - Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                }

                this.value = kiri + x + kanan;
                break;
            } else if (stringDi(soal, i).equals(op2)) {
                ada = true;
                double x = 0l;
                switch (op2) {
                    case "*":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) * Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "÷":
                        if (Double.parseDouble(ambilAngkaKanan(soal, i)) != 0)
                            x = Double.parseDouble(ambilAngkaKiri(soal, i)) / Double.parseDouble(ambilAngkaKanan(soal, i));
                        else
                            x = Long.MAX_VALUE;
                        break;
                    case "+":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) + Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "_":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) - Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                }
                this.value = kiri + x + kanan;
                break;
            } else if (stringDi(soal, i).equals(op3)) {
                ada = true;
                double x = 0l;
                switch (op3) {
                    case "*":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) * Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "÷":
                        if (Double.parseDouble(ambilAngkaKanan(soal, i)) != 0)
                            x = Double.parseDouble(ambilAngkaKiri(soal, i)) / Double.parseDouble(ambilAngkaKanan(soal, i));
                        else
                            x = Long.MAX_VALUE;
                        break;
                    case "+":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) + Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "_":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) - Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                }
                this.value = kiri + x + kanan;
                break;
            } else if (stringDi(soal, i).equals(op4)) {
                ada = true;
                double x = 0l;
                switch (op4) {
                    case "*":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) * Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "÷":
                        if (Double.parseDouble(ambilAngkaKanan(soal, i)) != 0)
                            x = Double.parseDouble(ambilAngkaKiri(soal, i)) / Double.parseDouble(ambilAngkaKanan(soal, i));
                        else
                            x = Long.MAX_VALUE;
                        break;
                    case "+":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) + Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                    case "_":
                        x = Double.parseDouble(ambilAngkaKiri(soal, i)) - Double.parseDouble(ambilAngkaKanan(soal, i));
                        break;
                }
                this.value = kiri + x + kanan;
                break;
            }
        }

        return ada;

    }

    String ambilAngkaKiri(String soal, int pos) {
        String tmp;
        kiri = "";
        int i;
        for (i = pos - 1; i >= 0; i--) {
            if (stringDi(soal, i).equals("+") || stringDi(soal, i).equals("_") || stringDi(soal, i).equals("*") || stringDi(soal, i).equals("÷"))
                break;
        }
        tmp = soal.substring(i + 1, pos);
        kiri = soal.substring(0, i + 1);
        return tmp;
    }

    String ambilAngkaKanan(String soal, int pos) {
        String tmp;
        kanan = "";
        int i;
        for (i = pos + 1; i < soal.length(); i++) {
            if (stringDi(soal, i).equals("+") || stringDi(soal, i).equals("_") || stringDi(soal, i).equals("*") || stringDi(soal, i).equals("÷"))
                break;
        }
        tmp = soal.substring(pos + 1, i);
        kanan = soal.substring(i, soal.length());
        return tmp;
    }

    public void calculateGstPluse(int gst) {
        try {
            if (calculate_txt.getText().toString().equals("")) {

            } else {
                double totalValue = Double.parseDouble("" + calculate_txt.getText().toString());
                double total = (totalValue * gst) / 100;
                calculate_txt.setText("" + GlobalElements.DecimalFormat("" + (totalValue + total)));
                igst_txt.setText("IGST = " + GlobalElements.DecimalFormat("" + total));
                cgst_sgst_txt.setText("CGST/SGST = " + GlobalElements.DecimalFormat("" + (total / 2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateGstMinus(int gst) {
        try {
            if (calculate_txt.getText().toString().equals("")) {

            } else {
                double totalValue = Double.parseDouble("" + calculate_txt.getText().toString());
                double total = (totalValue / (100 + gst)) * gst;
                calculate_txt.setText("" + GlobalElements.DecimalFormat("" + (totalValue - total)));
                igst_txt.setText("IGST = " + GlobalElements.DecimalFormat("" + total));
                cgst_sgst_txt.setText("CGST/SGST = " + GlobalElements.DecimalFormat("" + (total / 2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
