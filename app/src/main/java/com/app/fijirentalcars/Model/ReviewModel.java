package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewModel {

    @SerializedName("wpcr3_review_ip")
    @Expose
    private String wpcr3ReviewIp;
    @SerializedName("wpcr3_review_post")
    @Expose
    private String wpcr3ReviewPost;
    @SerializedName("wpcr3_review_name")
    @Expose
    private String wpcr3ReviewName;
    @SerializedName("wpcr3_review_email")
    @Expose
    private String wpcr3ReviewEmail;
    @SerializedName("wpcr3_review_rating")
    @Expose
    private String wpcr3ReviewRating;
    @SerializedName("wpcr3_review_title")
    @Expose
    private String wpcr3ReviewTitle;
    @SerializedName("wpcr3_review_website")
    @Expose
    private String wpcr3ReviewWebsite;
    @SerializedName("_edit_lock")
    @Expose
    private String editLock;
    @SerializedName("_edit_last")
    @Expose
    private String editLast;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stars")
    @Expose
    private String stars;
    @SerializedName("wpcr3_review_admin_response")
    @Expose
    private String wpcr3ReviewAdminResponse;
    @SerializedName("wpcr3_custom_fields")
    @Expose
    private List<String> wpcr3CustomFields = null;

    @SerializedName("user")
    @Expose
    private PartnerDetail user;

    public String getWpcr3ReviewIp() {
        return wpcr3ReviewIp;
    }

    public void setWpcr3ReviewIp(String wpcr3ReviewIp) {
        this.wpcr3ReviewIp = wpcr3ReviewIp;
    }

    public String getWpcr3ReviewPost() {
        return wpcr3ReviewPost;
    }

    public void setWpcr3ReviewPost(String wpcr3ReviewPost) {
        this.wpcr3ReviewPost = wpcr3ReviewPost;
    }

    public String getWpcr3ReviewName() {
        return wpcr3ReviewName;
    }

    public void setWpcr3ReviewName(String wpcr3ReviewName) {
        this.wpcr3ReviewName = wpcr3ReviewName;
    }

    public String getWpcr3ReviewEmail() {
        return wpcr3ReviewEmail;
    }

    public void setWpcr3ReviewEmail(String wpcr3ReviewEmail) {
        this.wpcr3ReviewEmail = wpcr3ReviewEmail;
    }

    public String getWpcr3ReviewRating() {
        return wpcr3ReviewRating;
    }

    public void setWpcr3ReviewRating(String wpcr3ReviewRating) {
        this.wpcr3ReviewRating = wpcr3ReviewRating;
    }

    public String getWpcr3ReviewTitle() {
        return wpcr3ReviewTitle;
    }

    public void setWpcr3ReviewTitle(String wpcr3ReviewTitle) {
        this.wpcr3ReviewTitle = wpcr3ReviewTitle;
    }

    public String getWpcr3ReviewWebsite() {
        return wpcr3ReviewWebsite;
    }

    public void setWpcr3ReviewWebsite(String wpcr3ReviewWebsite) {
        this.wpcr3ReviewWebsite = wpcr3ReviewWebsite;
    }

    public String getEditLock() {
        return editLock;
    }

    public void setEditLock(String editLock) {
        this.editLock = editLock;
    }

    public String getEditLast() {
        return editLast;
    }

    public void setEditLast(String editLast) {
        this.editLast = editLast;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getWpcr3ReviewAdminResponse() {
        return wpcr3ReviewAdminResponse;
    }

    public void setWpcr3ReviewAdminResponse(String wpcr3ReviewAdminResponse) {
        this.wpcr3ReviewAdminResponse = wpcr3ReviewAdminResponse;
    }

    public List<String> getWpcr3CustomFields() {
        return wpcr3CustomFields;
    }

    public void setWpcr3CustomFields(List<String> wpcr3CustomFields) {
        this.wpcr3CustomFields = wpcr3CustomFields;
    }

    public PartnerDetail getUser() {
        return user;
    }

    public void setUser(PartnerDetail user) {
        this.user = user;
    }

}
