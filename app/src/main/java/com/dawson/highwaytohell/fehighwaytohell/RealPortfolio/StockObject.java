package com.dawson.highwaytohell.fehighwaytohell.RealPortfolio;

import java.io.Serializable;
import java.sql.Timestamp;

public class StockObject implements Serializable {


    private int id;

    private String company;

    private String ticker;

    private int amount;

    private int current_price;

    private String nativeCurrency;

    private int total;

    private String email;

    private Timestamp purchaseDate;

    private int change;

    private int closeYesterday;

    public StockObject(int id, String company,String ticker,int amount,int currentPrice,String nativeCurrency,int total,String email,Timestamp purchaseDate,int change,int closeYesterday )
    {
        this.amount = amount;
        this.id = id;
        this.company = company;
        this.ticker = ticker;
        this.current_price = currentPrice;
        this.nativeCurrency = nativeCurrency;
        this.total = total;
        this.email = email;
        this.purchaseDate = purchaseDate;
        this.change = change;
        this.closeYesterday = closeYesterday;

    }

    public StockObject() {

    }

    public int getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getTicker() {
        return ticker;
    }

    public int getAmount() {
        return amount;
    }

    public int getCurrent_price() {
        return current_price;
    }

    public String getNativeCurrency() {
        return nativeCurrency;
    }

    public int getTotal() {
        return total;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public int getChange() {
        return change;
    }

    public int getCloseYesterday() {
        return closeYesterday;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCurrent_price(int current_price) {
        this.current_price = current_price;
    }

    public void setNativeCurrency(String nativeCurrency) {
        this.nativeCurrency = nativeCurrency;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public void setCloseYesterday(int closeYesterday) {
        this.closeYesterday = closeYesterday;
    }
}
