package com.magesh.rideshare;

public class Offer {
    public String uid, ori, des, dor, dep, sa, car;
    public double orilat, orilng, deslat, deslng;

    public Offer(){

    }

    public Offer(String uid, String ori, String des, String dor, String dep, String sa, String car, double orilat, double orilng, double deslat, double deslng){
        this.uid = uid;
        this.ori = ori;
        this.des = des;
        this.dor = dor;
        this.dep = dep;
        this.sa = sa;
        this.car = car;
        this.orilat = orilat;
        this.orilng = orilng;
        this.deslat = deslat;
        this.deslng = deslng;
    }
}
