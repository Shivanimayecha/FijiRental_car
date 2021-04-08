package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsuranceModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("host_take")
    @Expose
    private String hostTake;
    @SerializedName("deductible")
    @Expose
    private String deductible;
    @SerializedName("loss_of_hosting_income")
    @Expose
    private String lossOfHostingIncome;
    @SerializedName("liability_coverage")
    @Expose
    private String liabilityCoverage;
    @SerializedName("courtesy_car")
    @Expose
    private String courtesyCar;
    @SerializedName("learn_more")
    @Expose
    private String learnMore;
    @SerializedName("more_options")
    @Expose
    private String moreOptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getHostTake() {
        return hostTake;
    }

    public void setHostTake(String hostTake) {
        this.hostTake = hostTake;
    }

    public String getDeductible() {
        return deductible;
    }

    public void setDeductible(String deductible) {
        this.deductible = deductible;
    }

    public String getLossOfHostingIncome() {
        return lossOfHostingIncome;
    }

    public void setLossOfHostingIncome(String lossOfHostingIncome) {
        this.lossOfHostingIncome = lossOfHostingIncome;
    }

    public String getLiabilityCoverage() {
        return liabilityCoverage;
    }

    public void setLiabilityCoverage(String liabilityCoverage) {
        this.liabilityCoverage = liabilityCoverage;
    }

    public String getCourtesyCar() {
        return courtesyCar;
    }

    public void setCourtesyCar(String courtesyCar) {
        this.courtesyCar = courtesyCar;
    }

    public String getLearnMore() {
        return learnMore;
    }

    public void setLearnMore(String learnMore) {
        this.learnMore = learnMore;
    }

    public String getMoreOptions() {
        return moreOptions;
    }

    public void setMoreOptions(String moreOptions) {
        this.moreOptions = moreOptions;
    }

}
