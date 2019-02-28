package com.magesh.rideshare;

public class Ask {
    public String pic, dro, dor, sr;
    public double piclat, piclng, drolat, drolng;

    public Ask(){

    }

    public Ask(String pic, String dro, String dor, String sr, double piclat, double piclng, double drolat, double drolng){
        this.pic = pic;
        this.dro = dro;
        this.dor = dor;
        this.sr = sr;
        this.piclat = piclat;
        this.piclng = piclng;
        this.drolat = drolat;
        this.drolng = drolng;
    }
}
