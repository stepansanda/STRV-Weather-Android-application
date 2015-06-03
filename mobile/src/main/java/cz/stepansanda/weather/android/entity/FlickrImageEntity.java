package cz.stepansanda.weather.android.entity;

/**
 * Created by a111 on 29.05.15.
 */
public class FlickrImageEntity {

    private String mId;
    private String mOwner;
    private String mSecret;
    private String mServer;


    /**
     * Empty constructor
     */
    public FlickrImageEntity() {

    }


    public FlickrImageEntity(String id, String owner, String secret, String server) {
        mId = id;
        mOwner = owner;
        mSecret = secret;
        mServer = server;
    }


    public String getId() {
        return mId;
    }


    public void setId(String id) {
        mId = id;
    }


    public String getOwner() {
        return mOwner;
    }


    public void setOwner(String owner) {
        mOwner = owner;
    }


    public String getSecret() {
        return mSecret;
    }


    public void setSecret(String secret) {
        mSecret = secret;
    }


    public String getServer() {
        return mServer;
    }


    public void setServer(String server) {
        mServer = server;
    }
}
