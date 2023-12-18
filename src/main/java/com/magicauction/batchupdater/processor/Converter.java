package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.PriceMap;
import com.magicauction.batchupdater.entity.UriMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Component
public class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    public CardPojo toCardPojo(JsonNode json){
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
                prices,
                images_uris,
                related_uris,
                purchase_uris
        );
    }

    private UriMap toUriMap(JsonNode jsonUris) throws JsonProcessingException {
        UriMap map = new UriMap();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonUris.fields();
        while (fields.hasNext()){
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(entry.getKey(), entry.getValue().asText());
        }
        return map;
    }

    private PriceMap toPriceMap(JsonNode prices) throws JsonProcessingException {
        PriceMap map = new PriceMap();
        Iterator<Map.Entry<String, JsonNode>> fields = prices.fields();
        while (fields.hasNext()){
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(entry.getKey(), toFloat(entry.getValue()));
        }
        return map;
    }

    private Float toFloat(JsonNode value){
        return Float.valueOf(value.isNull() ? "0" : value.asText());
    }
}
