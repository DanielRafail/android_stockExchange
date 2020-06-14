package com.dawson.highwaytohell.fehighwaytohell.Hints;

/**
 * This is a data class for hint and source.
 */
public class HintBean {
 private String Text;
 private String Url;

    public HintBean(String text, String url) {
        Text = text;
        Url = url;
    }

    public HintBean() {
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
