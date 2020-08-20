package com.feemoo.fmapp.network.model.history;

public class FilesModel extends HistoryModel{

    /**
     * id : 3754773
     * fid : 2713480
     * intime : 2020.04.10 11:22:13
     * name : 归档_2.zip
     * ext : zip
     * size : 1.12 G
     */

    private String id;
    private String fid;
    private String intime;
    private String name;
    private String ext;
    private String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
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
}
