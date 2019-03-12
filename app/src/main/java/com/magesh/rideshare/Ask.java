package com.magesh.rideshare;

public class Ask {
    public String uid, pic, dro, dor, sr;
    public double piclat, piclng, drolat, drolng;

    public Ask(){

    }

    public Ask(String uid, String pic, String dro, String dor, String sr, double piclat, double piclng, double drolat, double drolng){
        this.uid = uid;
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
