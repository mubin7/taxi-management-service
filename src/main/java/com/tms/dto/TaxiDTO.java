package com.tms.dto;

import com.tms.constant.TaxiStatus;

public class TaxiDTO {
    private String taxiId;
    private TaxiStatus taxiStatus;
    private Double currXPos;
    private Double currYPos;

    public String getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(String taxiId) {
        this.taxiId = taxiId;
    }

    public TaxiStatus getTaxiStatus() {
        return taxiStatus;
    }

    public void setTaxiStatus(TaxiStatus taxiStatus) {
        this.taxiStatus = taxiStatus;
    }

    public Double getCurrXPos() {
        return currXPos;
    }

    public void setCurrXPos(Double currXPos) {
        this.currXPos = currXPos;
    }

    public Double getCurrYPos() {
        return currYPos;
    }

    public void setCurrYPos(Double currYPos) {
        this.currYPos = currYPos;
    }

    @Override
    public String toString() {
        return "TaxiDTO{" +
                "taxiId='" + taxiId + '\'' +
                ", taxiStatus=" + taxiStatus +
                ", xPosition=" + currXPos +
                ", yPosition=" + currYPos +
                '}';
    }
}
