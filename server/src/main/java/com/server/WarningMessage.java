package com.server;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class WarningMessage {
    private String nickname;
    private double latitude;
    private double longitude;
    private ZonedDateTime sent;
    private String dangertype;
    private String areacode;
    private String phonenumber;
    
   



    public WarningMessage(String nickname, double latitude, double longitude, ZonedDateTime sent,  String dangertype) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sent = sent;
        this.dangertype = dangertype;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ZonedDateTime getSent(){
        return sent;
    }

    public String getDangertype() {
        return dangertype;
    }

    public void setDangertype(String dangertype) {
        this.dangertype = dangertype;
    }

    
        
    public void setSent(long epoch) {
        sent = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
        }

    public long dateAsInt() {
        return sent.toInstant().toEpochMilli();
        }

    }  

    

