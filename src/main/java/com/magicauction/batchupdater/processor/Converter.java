package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.PriceMap;
import com.magicauction.batchupdater.entity.MagicSet;
import com.magicauction.batchupdater.entity.ScryfallSetPojo;
import com.magicauction.batchupdater.entity.UriMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public abstract class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    public static ScryfallSetPojo toSetPojo(JsonNode json){
        String name = json.findValue("name").asText();
        String scryfallId =json.findValue("id").asText();
        String code = json.findValue("code").asText();
        String releasedAt = json.findValue("code").asText();
        String setType= json.findValue("code").asText();
        Integer cardCount= json.findValue("code").asInt();
        String parentSetCode= json.findValue("code").asText();
        boolean isDigital= json.findValue("code").asBoolean();
        String searchUri = json.findValue("code").asText();

        return new ScryfallSetPojo(
                name,
                scryfallId,
                code,
                releasedAt,
                setType,
                cardCount,
                parentSetCode,
                isDigital,
                searchUri
                );
    }

    public static CardPojo toCardPojo(JsonNode json){
        String name = json.findValue("name").asText();
        String scryfallId = json.findValue("id").asText();
        String imgStatus = json.findValue("image_status").asText();
        String setName = json.findValue("set").asText();
        boolean isFoil = json.findValue("foil").asBoolean();
        PriceMap prices = new PriceMap();
        UriMap related_uris = new UriMap();
        UriMap purchase_uris = new UriMap();
        UriMap images_uris = new UriMap();

        try{
            related_uris = toUriMap(json.findPath("related_uris"));
            purchase_uris = toUriMap(json.findPath("purchase_uris"));
            images_uris = toUriMap(json.findPath("image_uris"));
            prices = toPriceMap(json.findPath("prices"));
        }catch (JsonProcessingException ex){
            log.error("error on json parsing - msg: {}", ex.getMessage());
        }

        return new CardPojo(
                name,
                scryfallId,
                imgStatus,
                isFoil,
                setName,
                null,
                prices,
                images_uris,
                related_uris,
                purchase_uris
        );
    }

    public static MagicSet toEntity(ScryfallSetPojo set){
        MagicSet ms = new MagicSet();
        ms.setName(set.name());
        ms.setScryfallId(set.id());
        ms.setCode(set.code());
        ms.setReleasedAt(set.released_at());
        ms.setSetType(set.set_type());
        ms.setCardCount(set.card_count());
        ms.setParentSetCode(set.parent_set_code());
        ms.setDigital(set.digital());
        ms.setSearchUri(set.search_uri());
        return ms;
    }

    public static Card toEntity(CardPojo card, MagicSet set) {
        Card nc = new Card();
        nc.setName(card.name());
        nc.setScryfallId(card.scryfallId());
        nc.setImgStatus(card.imgStatus());
        nc.setFoil(card.isFoil());
        nc.setMagicSet(set);
        nc.setPrices(card.prices().toString());
        nc.setRelatedUri(card.relatedUri().toString());
        nc.setImageUri(card.imageUri().toString());
        nc.setPurchaseUri(card.purchaseUri().toString());
        nc.setLastModification(new Date());
        log.debug("Pojo translated: og {} - toDb {}", card, nc);
        return nc;
    }

    private static UriMap toUriMap(JsonNode jsonUris) throws JsonProcessingException {
        UriMap map = new UriMap();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonUris.fields();
        while (fields.hasNext()){
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(entry.getKey(), entry.getValue().asText());
        }
        return map;
    }

    private static PriceMap toPriceMap(JsonNode prices) throws JsonProcessingException {
        PriceMap map = new PriceMap();
        Iterator<Map.Entry<String, JsonNode>> fields = prices.fields();
        while (fields.hasNext()){
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(entry.getKey(), toFloat(entry.getValue()));
        }
        return map;
    }

    private static Float toFloat(JsonNode value){
        return Float.valueOf(value.isNull() ? "0" : value.asText());
    }

}
