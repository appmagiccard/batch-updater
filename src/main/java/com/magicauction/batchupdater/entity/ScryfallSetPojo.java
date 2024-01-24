package com.magicauction.batchupdater.entity;

public record ScryfallSetPojo(
        String name,
        String id, //THIS HOLDS THE SCRYFALL UUID
        String code,
        String released_at,
        String set_type,
        Integer card_count,
        String parent_set_code,
        boolean digital,
        String search_uri
) {
}
