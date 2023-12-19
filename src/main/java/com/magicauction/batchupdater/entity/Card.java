package com.magicauction.batchupdater.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Card {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String scryfallId;

    private String imgStatus;

    private boolean isFoil;

    private String setName;

    @Column(name = "prices", columnDefinition = "longtext")
    private String prices;
    @Column(name = "image_uri", columnDefinition = "longtext")
    private String imageUri;
    @Column(name = "related_uri", columnDefinition = "longtext")
    private String relatedUri;
    @Column(name = "purchase_uri", columnDefinition = "longtext")
    private String purchaseUri;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scryfallId='" + scryfallId + '\'' +
                ", imgStatus='" + imgStatus + '\'' +
                ", isFoil=" + isFoil +
                ", setName='" + setName + '\'' +
                ", prices=" + prices +
                ", imageUri=" + imageUri +
                ", relatedUri=" + relatedUri +
                ", purchaseUri=" + purchaseUri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return isFoil == card.isFoil && Objects.equals(id, card.id) && Objects.equals(name, card.name) && Objects.equals(scryfallId, card.scryfallId) && Objects.equals(imgStatus, card.imgStatus) && Objects.equals(setName, card.setName) && Objects.equals(prices, card.prices) && Objects.equals(imageUri, card.imageUri) && Objects.equals(relatedUri, card.relatedUri) && Objects.equals(purchaseUri, card.purchaseUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scryfallId, imgStatus, isFoil, setName, prices, imageUri, relatedUri, purchaseUri);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScryfallId() {
        return scryfallId;
    }

    public void setScryfallId(String scryfallId) {
        this.scryfallId = scryfallId;
    }

    public String getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(String imgStatus) {
        this.imgStatus = imgStatus;
    }

    public boolean isFoil() {
        return isFoil;
    }

    public void setFoil(boolean foil) {
        isFoil = foil;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRelatedUri() {
        return relatedUri;
    }

    public void setRelatedUri(String relatedUri) {
        this.relatedUri = relatedUri;
    }

    public String getPurchaseUri() {
        return purchaseUri;
    }

    public void setPurchaseUri(String purchaseUri) {
        this.purchaseUri = purchaseUri;
    }

    public Card(Long id, String name, String scryfallId, String imgStatus, boolean isFoil, String setName, String prices, String imageUri, String relatedUri, String purchaseUri) {
        this.id = id;
        this.name = name;
        this.scryfallId = scryfallId;
        this.imgStatus = imgStatus;
        this.isFoil = isFoil;
        this.setName = setName;
        this.prices = prices;
        this.imageUri = imageUri;
        this.relatedUri = relatedUri;
        this.purchaseUri = purchaseUri;
    }

    public Card() {
    }
}
