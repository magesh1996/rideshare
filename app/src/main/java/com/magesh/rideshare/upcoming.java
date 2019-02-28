package com.magesh.rideshare;

public class upcoming {

    String ori, des, dor, sa;

    public upcoming() {
    }

    public upcoming(String ori, String des, String dor, String sa){

        this.ori = ori;
        this.des = des;
        this.dor = dor;
        this.sa = sa;

    }

    //getter

    public String getOri() {
        return ori;
    }

    public String getDes() {
        return des;
    }

    public String getDor() {
        return dor;
    }

    public String getSa() {
        return sa;
    }

    //setter


    public void setOri(String ori) {
        this.ori = ori;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }
}
