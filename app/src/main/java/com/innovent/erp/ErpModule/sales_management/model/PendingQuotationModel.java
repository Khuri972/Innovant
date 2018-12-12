package com.innovent.erp.ErpModule.sales_management.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingQuotationModel implements Serializable{

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public ArrayList<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<ProductModel> productModels) {
        this.productModels = productModels;
    }

    String quotationNo;
    ArrayList<ProductModel> productModels = new ArrayList<>();

}
