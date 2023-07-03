package com.ontimize.dominiondiamondhotel.model.core.entity;


public class Headline {
    private String EffectiveDate;
    private int Severity;
    private String Text;
    private String Category;
    private String EndDate;

    public Headline(String effectiveDate, int severity, String text, String category, String endDate) {
        this.EffectiveDate = effectiveDate;
        this.Severity = severity;
        this.Text = text;
        this.Category = category;
        this.EndDate = endDate;
    }

    public String getEffectiveDate() {
        return EffectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.EffectiveDate = effectiveDate;
    }

    public int getSeverity() {
        return Severity;
    }

    public void setSeverity(int severity) {
        this.Severity = severity;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        this.EndDate = endDate;
    }
}
