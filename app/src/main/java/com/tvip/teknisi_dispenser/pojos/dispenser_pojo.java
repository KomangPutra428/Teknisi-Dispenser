package com.tvip.teknisi_dispenser.pojos;

public class dispenser_pojo {
    private String szSerialNumber;
    private String szRentCalcId;

    private String szOrderItemTypeId;
    private String condition;

    public dispenser_pojo(String szSerialNumber, String szRentCalcId, String szOrderItemTypeId, String condition) {
        this.szSerialNumber = szSerialNumber;
        this.szRentCalcId = szRentCalcId;
        this.szOrderItemTypeId = szOrderItemTypeId;
        this.condition =  condition;
    }

    public String getSzSerialNumber() {
        return szSerialNumber;
    }

    public String getSzRentCalcId() {
        return szRentCalcId;
    }

    public String getSzOrderItemTypeId() {
        return szOrderItemTypeId;
    }

    public String getCondition() {
        return condition;
    }
}
