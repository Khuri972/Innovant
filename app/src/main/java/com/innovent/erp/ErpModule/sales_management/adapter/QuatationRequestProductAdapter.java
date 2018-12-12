package com.innovent.erp.ErpModule.sales_management.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.R;
import com.innovent.erp.custom.DigitsInputFilter;
import com.innovent.erp.netUtils.MyPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class QuatationRequestProductAdapter extends RecyclerView.Adapter<QuatationRequestProductAdapter.ViewHolder> {

    private ArrayList<ProductModel> data;
    private Context context;
    MyPreferences myPreferences;

    public interface Intercommunicator {
        public void UpdateQty();
    }

    public void ChangeData()
    {
        try {
            Intercommunicator intercommunicator = (Intercommunicator) context;
            intercommunicator.UpdateQty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QuatationRequestProductAdapter(Context context, ArrayList<ProductModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sales_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.salesProductDesc.setText("" + data.get(i).getName());
        viewHolder.salesProductHsn.setText("" + data.get(i).getHsnCode());
        viewHolder.salesProductUnit.setText("" + data.get(i).getUnit());
        viewHolder.salesProductPrice.setText("" + data.get(i).getSell_price());

        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());

        viewHolder.salesProductQty.setTag("" + data.get(i).getId());
        viewHolder.salesProductDisctount.setTag("" + data.get(i).getId());
        viewHolder.salesProductPrice.setTag("" + data.get(i).getId());

        viewHolder.salesProductPrice.setFilters(new InputFilter[]{new DigitsInputFilter(5, 2)});

        if (data.get(i).getQty() == 0) {
            viewHolder.salesProductQty.setText("");
            viewHolder.salesProductAmnt.setText("");
        } else {
            viewHolder.salesProductQty.setText("" + data.get(i).getQty());
            data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
            if (data.get(i).getGstType() == 0) {  // 0 = gujrat
                data.get(i).setCgst(data.get(i).getGst() / 2);
                data.get(i).setSgst(data.get(i).getGst() / 2);
                double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
            } else {
                data.get(i).setIgst(data.get(i).getGst());
                double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
            }
            viewHolder.salesProductAmnt.setText(""+data.get(i).getAmount());
            viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
        }

        if (data.get(i).getDiscount() == 0) {
            viewHolder.salesProductDisctount.setText("");
            data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
        } else {
            viewHolder.salesProductDisctount.setText("" + data.get(i).getDiscount());
            data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
            data.get(i).setAmount((data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100)));

            if (data.get(i).getGstType() == 0) {
                data.get(i).setCgst(data.get(i).getGst() / 2);
                data.get(i).setSgst(data.get(i).getGst() / 2);
                double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst())/100;
                data.get(i).setNetAmount(data.get(i).getAmount()+netAmount);
            } else {
                data.get(i).setIgst(data.get(i).getGst());
                double netAmount = data.get(i).getAmount() * data.get(i).getIgst()/100;
                data.get(i).setNetAmount(data.get(i).getAmount()+netAmount);
            }
            viewHolder.salesProductAmnt.setText(""+data.get(i).getAmount());
            viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
        }

        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());

        viewHolder.salesProductQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (viewHolder.salesProductQty.hasFocus()) {
                        if (viewHolder.salesProductQty.getTag().equals("" + data.get(i).getId())) {
                            try {
                                if (s.toString().length() > 0) {
                                    data.get(i).setQty(Integer.parseInt("" + viewHolder.salesProductQty.getText().toString()));
                                    data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());

                                    if (data.get(i).getDiscount() == 0) {
                                        data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    } else {
                                        data.get(i).setAmount(data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100));
                                    }

                                    if (data.get(i).getGstType() == 0) {
                                        data.get(i).setCgst(data.get(i).getGst() / 2);
                                        data.get(i).setSgst(data.get(i).getGst() / 2);
                                        double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    } else {
                                        data.get(i).setIgst(data.get(i).getGst());
                                        double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    }

                                    viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                    viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                    viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                    viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                    viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                } else {

                                    data.get(i).setDiscount(0);
                                    data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    //data.get(i).setNetAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                    if (data.get(i).getGstType() == 0) {
                                        data.get(i).setCgst(data.get(i).getGst() / 2);
                                        data.get(i).setSgst(data.get(i).getGst() / 2);
                                        double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    } else {
                                        data.get(i).setIgst(data.get(i).getGst());
                                        double netAmount = data.get(i).getNetAmount() * data.get(i).getIgst() / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    }
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                    viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                    viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                    viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                    viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                    viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                }
                                Intercommunicator intercommunicator = (Intercommunicator) context;
                                intercommunicator.UpdateQty();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        viewHolder.salesProductDisctount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (viewHolder.salesProductDisctount.hasFocus()) {
                        if (viewHolder.salesProductDisctount.getTag().equals("" + data.get(i).getId())) {
                            try {
                                if (s.toString().length() > 0) {
                                    data.get(i).setDiscount(Integer.parseInt("" + viewHolder.salesProductDisctount.getText().toString()));
                                    data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    viewHolder.salesProductAmnt.setText("" + (data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100)));

                                    if (data.get(i).getDiscount() == 0) {
                                        data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    } else {
                                        data.get(i).setAmount(data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100));
                                    }

                                    if (data.get(i).getGstType() == 0) {
                                        data.get(i).setCgst(data.get(i).getGst() / 2);
                                        data.get(i).setSgst(data.get(i).getGst() / 2);
                                        double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    } else {
                                        data.get(i).setIgst(data.get(i).getGst());
                                        double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    }

                                    viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                    viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                    viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                    viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                    viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                } else {

                                    data.get(i).setDiscount(0);
                                    data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    //data.get(i).setNetAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                    if (data.get(i).getGstType() == 0) {
                                        data.get(i).setCgst(data.get(i).getGst() / 2);
                                        data.get(i).setSgst(data.get(i).getGst() / 2);
                                        double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    } else {
                                        data.get(i).setIgst(data.get(i).getGst());
                                        double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                                        data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                    }
                                    viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                    viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                    viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                    viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                    viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                }
                                Intercommunicator intercommunicator = (Intercommunicator) context;
                                intercommunicator.UpdateQty();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        viewHolder.salesProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (viewHolder.salesProductPrice.hasFocus()) {
                        if (viewHolder.salesProductPrice.getTag().equals("" + data.get(i).getId())) {
                            try {
                                if (s.toString().length() > 0) {
                                    data.get(i).setSell_price(Double.parseDouble("" + viewHolder.salesProductPrice.getText().toString()));
                                    if (data.get(i).getDiscount() == 0) {
                                        data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                        if (data.get(i).getGstType() == 0) {
                                            data.get(i).setCgst(data.get(i).getGst() / 2);
                                            data.get(i).setSgst(data.get(i).getGst() / 2);
                                            double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        } else {
                                            data.get(i).setIgst(data.get(i).getGst());
                                            double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        }
                                        viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                        viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                    } else {
                                        //viewHolder.salesProductDisctount.setText("" + data.get(i).getDiscount());
                                        //data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                        data.get(i).setAmount(data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100));

                                        if (data.get(i).getGstType() == 0) {
                                            data.get(i).setCgst(data.get(i).getGst() / 2);
                                            data.get(i).setSgst(data.get(i).getGst() / 2);
                                            double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        } else {
                                            data.get(i).setIgst(data.get(i).getGst());
                                            double netAmount = data.get(i).getNetAmount() * data.get(i).getIgst() / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        }

                                        viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                        viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                    }
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                } else {
                                    data.get(i).setSell_price(0);
                                    if (data.get(i).getDiscount() == 0) {
                                        viewHolder.salesProductDisctount.setText("0");
                                        data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                        //data.get(i).setNetAmount(data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100));

                                        if (data.get(i).getGstType() == 0) {
                                            data.get(i).setCgst(data.get(i).getGst() / 2);
                                            data.get(i).setSgst(data.get(i).getGst() / 2);
                                            double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                            data.get(i).setNetAmount(+data.get(i).getAmount() + netAmount);
                                        } else {
                                            data.get(i).setIgst(data.get(i).getGst());
                                            double netAmount = data.get(i).getNetAmount() * data.get(i).getIgst() / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        }
                                        viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                        viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                    } else {
                                        viewHolder.salesProductDisctount.setText("" + data.get(i).getDiscount());
                                        //data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                                        data.get(i).setAmount(data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100));

                                        if (data.get(i).getGstType() == 0) {
                                            data.get(i).setCgst(data.get(i).getGst() / 2);
                                            data.get(i).setSgst(data.get(i).getGst() / 2);
                                            double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                                            data.get(i).setNetAmount(+data.get(i).getAmount() + netAmount);
                                        } else {
                                            data.get(i).setIgst(data.get(i).getGst());
                                            double netAmount = data.get(i).getNetAmount() * data.get(i).getIgst() / 100;
                                            data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                                        }
                                        viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                                        viewHolder.salesProductGst.setText("" + data.get(i).getGst());
                                        viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
                                        viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
                                        viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
                                        viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
                                    }
                                    viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());

                                }
                                Intercommunicator intercommunicator = (Intercommunicator) context;
                                intercommunicator.UpdateQty();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog buildInfosDialog;
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Are you sure want delete this item");

                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                data.remove(i);
                                notifyDataSetChanged();
                                Intercommunicator intercommunicator = (Intercommunicator) context;
                                intercommunicator.UpdateQty();
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                            }
                        });
                buildInfosDialog = alertDialog2.create();
                buildInfosDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sales_product_desc)
        TextView salesProductDesc;
        @BindView(R.id.sales_product_hsn)
        TextView salesProductHsn;
        @BindView(R.id.sales_product_unit)
        TextView salesProductUnit;
        @BindView(R.id.sales_product_qty)
        EditText salesProductQty;
        @BindView(R.id.sales_product_price)
        EditText salesProductPrice;
        @BindView(R.id.sales_product_disctount)
        EditText salesProductDisctount;
        @BindView(R.id.sales_product_amnt)
        TextView salesProductAmnt;
        @BindView(R.id.sales_product_gst)
        TextView salesProductGst;
        @BindView(R.id.sales_product_cgst)
        TextView salesProductCgst;
        @BindView(R.id.sales_product_sgst)
        TextView salesProductSgst;
        @BindView(R.id.sales_product_igst)
        TextView salesProductIgst;
        @BindView(R.id.sales_product_nat_amt)
        TextView salesProductNatAmt;
        @BindView(R.id.sales_product_delete)
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
