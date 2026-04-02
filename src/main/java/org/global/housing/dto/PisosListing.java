package org.global.housing.dto;

import java.util.List;

public class PisosListing {
    private String id;
    private String link;
    private String price;
    private String priceDrop;
    private String title;
    private String subtitle;
    private String bedrooms;
    private String bathrooms;
    private String area;
    private String description;
    private Integer photosCount;
    private Boolean hasVideo;
    private String image;
    private String agencyLogo;
    private String geoLatitude;
    private String geoLongitude;
    private String favoriteId;
    private String dataAdPrice;
    private List<String> images; // lista de URLs del carrusel

    public PisosListing() {}

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getPriceDrop() { return priceDrop; }
    public void setPriceDrop(String priceDrop) { this.priceDrop = priceDrop; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getBedrooms() { return bedrooms; }
    public void setBedrooms(String bedrooms) { this.bedrooms = bedrooms; }
    public String getBathrooms() { return bathrooms; }
    public void setBathrooms(String bathrooms) { this.bathrooms = bathrooms; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPhotosCount() { return photosCount; }
    public void setPhotosCount(Integer photosCount) { this.photosCount = photosCount; }
    public Boolean getHasVideo() { return hasVideo; }
    public void setHasVideo(Boolean hasVideo) { this.hasVideo = hasVideo; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getAgencyLogo() { return agencyLogo; }
    public void setAgencyLogo(String agencyLogo) { this.agencyLogo = agencyLogo; }
    public String getGeoLatitude() { return geoLatitude; }
    public void setGeoLatitude(String geoLatitude) { this.geoLatitude = geoLatitude; }
    public String getGeoLongitude() { return geoLongitude; }
    public void setGeoLongitude(String geoLongitude) { this.geoLongitude = geoLongitude; }
    public String getFavoriteId() { return favoriteId; }
    public void setFavoriteId(String favoriteId) { this.favoriteId = favoriteId; }
    public String getDataAdPrice() { return dataAdPrice; }
    public void setDataAdPrice(String dataAdPrice) { this.dataAdPrice = dataAdPrice; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
