package be.kuleuven.softdev.hehuang.pokepelican.objects;

/**
 * Created by shuaigehan on 12/27/2016.
 */
//Simple POJO to hold values of our JSON
public class NewsFeeds implements java.io.Serializable {

    private String imgURL, feedName, content, price, location, userid, username;
    private int itemid;

    /**
     * Instantiates a new News feeds.
     *
     * @param name     the name of the item
     * @param content  the discribtion of item
     * @param imgurl   the url of image
     * @param price    the price of item
     * @param location the location of item
     * @param userid   the userid of who owns the item
     * @param username the username of who owns the item
     * @param itemid   the itemid of the item
     */
    public NewsFeeds(String name, String content, String imgurl, String price, String location, String userid, String username,int itemid) {
        this.feedName = name;
        this.content = content;
        this.imgURL = imgurl;
        this.price = price;
        this.location = location;
        this.userid = userid;
        this.username = username;
        this.itemid = itemid;
    }

    /**
     * Gets content.
     *
     * @return the discribtion
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets img url.
     *
     * @return the img url
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * Gets feed name.
     *
     * @return the feed name
     */
    public String getFeedName() {
        return feedName;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets userid.
     *
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return username;
    }

    /**
     * Gets itemid.
     *
     * @return the itemid
     */
    public int getItemid() {
        return itemid;
    }
}