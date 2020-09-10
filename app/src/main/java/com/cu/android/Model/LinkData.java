package com.cu.android.Model;

public class LinkData {
    String id,name,link,favorite,category;

    public LinkData() {
    }

    public LinkData(String id,String name,String link, String favorite, String category) {
        this.id=id;
        this.name=name;
        this.link = link;
        this.favorite = favorite;
        this.category=category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
