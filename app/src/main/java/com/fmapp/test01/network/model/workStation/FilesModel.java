package com.fmapp.test01.network.model.workStation;

public class FilesModel extends workStationModel {

    /**
     * id : 1029786
     * name : AZNQ0825.mp4.zip
     * ext : zip
     * size : 51.12 M
     * intime : 2020.08.03 09:19:44
     * basename : AZNQ0825.mp4
     * basesize : 53607899
     */

    private String id;
    private String name;
    private String ext;
    private String size;
    private String intime;
    private String basename;
    private String basesize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getBasesize() {
        return basesize;
    }

    public void setBasesize(String basesize) {
        this.basesize = basesize;
    }
}
