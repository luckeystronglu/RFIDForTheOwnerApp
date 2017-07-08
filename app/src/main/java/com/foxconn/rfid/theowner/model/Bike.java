package com.foxconn.rfid.theowner.model;

import java.io.Serializable;

/**
 * Created by F1027734 on 2016-12-12.
 */

public class Bike implements Serializable {
    public long id = 0L;
    public String plateNumber = "";
    public String brandModel = "";
    public String color = "";
    public String photo = "";

    public String protestValue = "";
    public long purchaseDate = 0L;
    public double purchasePrice = 0D;
    public long areaId = 0L;
    public String areaName = "";
    public String usageNature = "";
    public int loadNumber = 0;
    public long userId = 0L;
    public boolean isEnable = false;
    public long cardId = 0L;
    public long cardNo = 0L;
    public String status = "";
    public String lostStatus = "";
    public String payoutStatus = "";
    public long createBy = 0L;
    public long creatDate = 0L;
    public long updateBy = 0L;
    public long udpateDate = 0L;
    public String remarks = "";
    public boolean delFlag = false;
    public String insurance = "";
    public long lostEndDate = 0;
    public int cardBatteryStatus = 0;
//    public String isProtect = "";
//
//    public String getIsProtect() {
//        return isProtect;
//    }
//
//    public void setIsProtect(String isProtect) {
//        this.isProtect = isProtect;
//    }

    public String getProtestValue() {
        return protestValue;
    }

    public void setProtestValue(String protestValue) {
        this.protestValue = protestValue;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public long getLostEndDate() {
        return lostEndDate;
    }

    public void setLostEndDate(long lostEndDate) {
        this.lostEndDate = lostEndDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getUsageNature() {
        return usageNature;
    }

    public void setUsageNature(String usageNature) {
        this.usageNature = usageNature;
    }

    public int getLoadNumber() {
        return loadNumber;
    }

    public void setLoadNumber(int loadNumber) {
        this.loadNumber = loadNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLostStatus() {
        return lostStatus;
    }

    public void setLostStatus(String lostStatus) {
        this.lostStatus = lostStatus;
    }

    public String getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(String payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }

    public long getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(long creatDate) {
        this.creatDate = creatDate;
    }

    public long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(long updateBy) {
        this.updateBy = updateBy;
    }

    public long getUdpateDate() {
        return udpateDate;
    }

    public void setUdpateDate(long udpateDate) {
        this.udpateDate = udpateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public long getCardNo() {
        return cardNo;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardBatteryStatus() {
        return cardBatteryStatus;
    }

    public void setCardBatteryStatus(int cardBatteryStatus) {
        this.cardBatteryStatus = cardBatteryStatus;
    }
}
