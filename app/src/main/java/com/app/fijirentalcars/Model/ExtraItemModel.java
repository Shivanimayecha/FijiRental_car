package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExtraItemModel implements Serializable {
    @SerializedName("extra_id")
    @Expose
    private Integer extraId;
    @SerializedName("extra_sku")
    @Expose
    private String extraSku;
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("extra_name")
    @Expose
    private String extraName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_type")
    @Expose
    private Integer priceType;
    @SerializedName("fixed_rental_deposit")
    @Expose
    private String fixedRentalDeposit;
    @SerializedName("units_in_stock")
    @Expose
    private Integer unitsInStock;
    @SerializedName("max_units_per_booking")
    @Expose
    private Integer maxUnitsPerBooking;
    @SerializedName("options_display_mode")
    @Expose
    private String optionsDisplayMode;
    @SerializedName("options_measurement_unit")
    @Expose
    private String optionsMeasurementUnit;
    @SerializedName("blog_id")
    @Expose
    private Integer blogId;
    @SerializedName("extra_main_id")
    @Expose
    private Integer extraMainId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getExtraId() {
        return extraId;
    }

    public void setExtraId(Integer extraId) {
        this.extraId = extraId;
    }

    public String getExtraSku() {
        return extraSku;
    }

    public void setExtraSku(String extraSku) {
        this.extraSku = extraSku;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getFixedRentalDeposit() {
        return fixedRentalDeposit;
    }

    public void setFixedRentalDeposit(String fixedRentalDeposit) {
        this.fixedRentalDeposit = fixedRentalDeposit;
    }

    public Integer getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(Integer unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public Integer getMaxUnitsPerBooking() {
        return maxUnitsPerBooking;
    }

    public void setMaxUnitsPerBooking(Integer maxUnitsPerBooking) {
        this.maxUnitsPerBooking = maxUnitsPerBooking;
    }

    public String getOptionsDisplayMode() {
        return optionsDisplayMode;
    }

    public void setOptionsDisplayMode(String optionsDisplayMode) {
        this.optionsDisplayMode = optionsDisplayMode;
    }

    public String getOptionsMeasurementUnit() {
        return optionsMeasurementUnit;
    }

    public void setOptionsMeasurementUnit(String optionsMeasurementUnit) {
        this.optionsMeasurementUnit = optionsMeasurementUnit;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getExtraMainId() {
        return extraMainId;
    }

    public void setExtraMainId(Integer extraMainId) {
        this.extraMainId = extraMainId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ExtraItemModel{" +
                "extraId=" + extraId +
                ", extraSku='" + extraSku + '\'' +
                ", partnerId=" + partnerId +
                ", itemId=" + itemId +
                ", extraName='" + extraName + '\'' +
                ", price='" + price + '\'' +
                ", priceType=" + priceType +
                ", fixedRentalDeposit='" + fixedRentalDeposit + '\'' +
                ", unitsInStock=" + unitsInStock +
                ", maxUnitsPerBooking=" + maxUnitsPerBooking +
                ", optionsDisplayMode='" + optionsDisplayMode + '\'' +
                ", optionsMeasurementUnit='" + optionsMeasurementUnit + '\'' +
                ", blogId=" + blogId +
                ", extraMainId=" + extraMainId +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
