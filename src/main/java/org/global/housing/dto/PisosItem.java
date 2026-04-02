package org.global.housing.dto;

public class PisosItem {
    private String title;
    private String link;
    private String price;

    public PisosItem() {}

    public PisosItem(String title, String link, String price) {
        this.title = title;
        this.link = link;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

