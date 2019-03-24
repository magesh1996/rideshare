package com.magesh.rideshare;

public class upcoming {

    String ori, des, dor, sa, offorreq, offorreqid;

    public upcoming() {
    }

    public upcoming(String ori, String des, String dor, String sa, String offorreq, String offorreqid){

        this.ori = ori;
        this.des = des;
        this.dor = dor;
        this.sa = sa;
        this.offorreq = offorreq;
        this.offorreqid = offorreqid;

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

    public String getOfforreq() {
        return offorreq;
    }

    public String getOfforreqid() {
        return offorreqid;
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

    public void setOfforreq(String offorreq) {
        this.offorreq = offorreq;
    }

    public void setOfforreqid(String offorreqid) {
        this.offorreqid = offorreqid;
    }
}
