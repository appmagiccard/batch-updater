package com.magicauction.batchupdater.entity;

import java.util.HashMap;

public record CardPojo(
        String name,
        String multiverseId,
        String imgStatus,
        boolean isFoil,
        String setName,
        HashMap<String, Float> prices,
        HashMap<String, String> imageUri,
        HashMap<String, String> relatedUri,
        HashMap<String, String> purchaseUri
) {
}