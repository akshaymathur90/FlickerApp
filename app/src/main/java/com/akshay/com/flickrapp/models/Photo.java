package com.akshay.com.flickrapp.models;

import com.akshay.com.flickrapp.database.FlickrDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by akshaymathur on 10/2/17.
 */
@Table(database = FlickrDatabase.class)
public class Photo extends BaseModel{

    @Column
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @Column
    @SerializedName("owner")
    @Expose
    private String owner;
    @Column
    @SerializedName("secret")
    @Expose
    private String secret;
    @Column
    @SerializedName("server")
    @Expose
    private String server;
    @Column
    @SerializedName("farm")
    @Expose
    private Integer farm;
    @Column
    @SerializedName("title")
    @Expose
    private String title;
    @Column
    @SerializedName("ispublic")
    @Expose
    private Integer ispublic;
    @Column
    @SerializedName("isfriend")
    @Expose
    private Integer isfriend;
    @Column
    @SerializedName("isfamily")
    @Expose
    private Integer isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

}
