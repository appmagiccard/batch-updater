package com.magicauction.batchupdater.entity;

import jakarta.persistence.Entity;

import java.util.HashMap;

public record CardPojo(
        String name,
        String scryfallId,
        String imgStatus,
        boolean isFoil,
        String setName,
        HashMap<String, Float> prices,
        HashMap<String, String> imageUri,
        HashMap<String, String> relatedUri,
        HashMap<String, String> purchaseUri
) {
}
