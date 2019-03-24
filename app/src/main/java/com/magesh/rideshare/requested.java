package com.magesh.rideshare;

public class requested {

    String uid, pic, dro, sr;

    public requested(String uid, String pic, String dro, String sr) {
        this.uid = uid;
        this.pic = pic;
        this.dro = dro;
        this.sr = sr;
    }

    //getter

    public String getUid() {
        return uid;
    }

    public String getPic() {
        return pic;
    }

    public String getDro() {
        return dro;
    }

    public String getSr() {
        return sr;
    }

    //setter


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setDro(String dro) {
        this.dro = dro;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }
}
