package com.tms.dto;

import com.tms.constant.TaxiStatus;

public class TaxiDTO {
    private String taxiId;
    private TaxiStatus taxiStatus;
    private Double xPosition;
    private Double yPosition;

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

    public Double getxPosition() {
        return xPosition;
    }

    public void setxPosition(Double xPosition) {
        this.xPosition = xPosition;
    }

    public Double getyPosition() {
        return yPosition;
    }

    public void setyPosition(Double yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public String toString() {
        return "TaxiDTO{" +
                "taxiId='" + taxiId + '\'' +
                ", taxiStatus=" + taxiStatus +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                '}';
    }
}
