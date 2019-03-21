package com.magesh.rideshare;

public class availableoffers {

    String oid, rname, start, end, cob, sa;

    public availableoffers(String oid, String rname, String start, String end, String cob, String sa) {
        this.oid = oid;
        this.rname = rname;
        this.start = start;
        this.end = end;
        this.cob = cob;
        this.sa = sa;
    }

    //getter

    public String getOid() { return oid; }

    public String getRname() {
        return rname;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getCob() {
        return cob;
    }

    public String getSa() {
        return sa;
    }

    //setter

    public void setOid(String oid) { this.oid = oid; }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setCob(String cob) {
        this.cob = cob;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }
}
