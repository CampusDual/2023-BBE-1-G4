package com.ontimize.dominiondiamondhotel.model.core.entity;


public class Headline {
    private String effectiveDate;
    private int severity;
    private String text;
    private String category;
    private String endDate;

    public Headline(String effectiveDate, int severity, String text, String category, String endDate) {
        this.effectiveDate = effectiveDate;
        this.severity = severity;
        this.text = text;
        this.category = category;
        this.endDate = endDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
