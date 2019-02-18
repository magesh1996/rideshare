package com.magesh.rideshare;

public class Offer {
    public String ori, des, dor, dep, sa, car, orilatlng, deslatlng;

    public Offer(){

    }

    public Offer(String ori, String des, String dor, String dep, String sa, String car, String orilatlng, String deslatlng){
        this.ori = ori;
        this.des = des;
        this.dor = dor;
        this.dep = dep;
        this.sa = sa;
        this.car = car;
        this.orilatlng = orilatlng;
        this.deslatlng = deslatlng;
    }
}
