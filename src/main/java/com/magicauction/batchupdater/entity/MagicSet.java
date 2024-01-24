package com.magicauction.batchupdater.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name="MAGIC_SETS")
public class MagicSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String scryfallId;
    private String code;
    private String releasedAt;
    private String setType;
    private Integer cardCount;
    private String parentSetCode;
    private boolean isDigital;
    private String searchUri;

    @Override
    public String toString() {
        return "MagicSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", id='" + scryfallId + '\'' +
                ", code='" + code + '\'' +
                ", releasedAt='" + releasedAt + '\'' +
                ", setType='" + setType + '\'' +
                ", cardCount=" + cardCount +
                ", parentSetCode='" + parentSetCode + '\'' +
                ", isDigital=" + isDigital +
                ", searchUri='" + searchUri + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MagicSet set = (MagicSet) o;
        return isDigital == set.isDigital && Objects.equals(id, set.id) && Objects.equals(name, set.name) && Objects.equals(scryfallId, set.scryfallId) && Objects.equals(code, set.code) && Objects.equals(releasedAt, set.releasedAt) && Objects.equals(setType, set.setType) && Objects.equals(cardCount, set.cardCount) && Objects.equals(parentSetCode, set.parentSetCode) && Objects.equals(searchUri, set.searchUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scryfallId, code, releasedAt, setType, cardCount, parentSetCode, isDigital, searchUri);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(String releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public String getParentSetCode() {
        return parentSetCode;
    }

    public void setParentSetCode(String parentSetCode) {
        this.parentSetCode = parentSetCode;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public String getSearchUri() {
        return searchUri;
    }

    public void setSearchUri(String searchUri) {
        this.searchUri = searchUri;
    }

    public MagicSet(String name, String scryfallId, String code, String releasedAt, String setType, Integer cardCount, String parentSetCode, boolean isDigital, String searchUri) {
        this.name = name;
        this.scryfallId = scryfallId;
        this.code = code;
        this.releasedAt = releasedAt;
        this.setType = setType;
        this.cardCount = cardCount;
        this.parentSetCode = parentSetCode;
        this.isDigital = isDigital;
        this.searchUri = searchUri;
    }

    public MagicSet(Long id, String name, String scryfallId, String code, String releasedAt, String setType, Integer cardCount, String parentSetCode, boolean isDigital, String searchUri) {
        this.id = id;
        this.name = name;
        this.scryfallId = scryfallId;
        this.code = code;
        this.releasedAt = releasedAt;
        this.setType = setType;
        this.cardCount = cardCount;
        this.parentSetCode = parentSetCode;
        this.isDigital = isDigital;
        this.searchUri = searchUri;
    }

    public MagicSet() {
    }
}
