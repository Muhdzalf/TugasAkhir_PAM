package com.example.projectakhirpam;

import java.io.Serializable;

public class DataHewan  implements Serializable {

    private String kode;
    private String nama;
    private String ras;
    private String notlp;
    private String key;


    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKode(){
        return kode;
    }
    public void setKode(){
        this.kode = kode;
    }
    public String getNama(){
        return nama;
    }
    public void setNama(){
        this.nama = nama;
    }
    public String getRas(){
        return nama;
    }
    public void setRas(){
        this.ras = ras;
    }
    public String getNotlp(){
        return notlp;
    }
    public void setNotlp(){
        this.notlp = notlp;
    }

    public DataHewan(){

    }
    public DataHewan(String Kode, String Nama, String Ras, String Tlp) {

        this.kode = Kode;
        this.nama = Nama;
        this.ras = Ras;
        this.notlp = Tlp;
    }


}
