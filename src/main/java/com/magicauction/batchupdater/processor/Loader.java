package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.BulkDataObject;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.BulkDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class Loader {

    private static final Logger log = LoggerFactory.getLogger(Loader.class);
    private final ObjectMapper mapper;

    private final RestClient bulkDataClient;
    private final RestClient restClient;

    @Autowired
    public Loader() {
        this.bulkDataClient = RestClient.builder().baseUrl("https://api.scryfall.com/bulk-data").build();
        this.restClient = RestClient.builder().build();
        this.mapper = new ObjectMapper();
    }

    public ArrayList<CardPojo> loadCardsFromJson(String path){
        ArrayList<CardPojo> cards = new ArrayList<>();
        log.info("Loading from path: {}", path);

        try(InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream(path)){
            JsonNode arrayNode = mapper.readTree(in);
            if (arrayNode.isArray())
                for(JsonNode node : arrayNode)
                    cards.add(Converter.toCardPojo(node));

            log.info("Cards transformed successfully - result {}",cards);
        }
        catch(Exception e){
            log.error("Error loading from: {}", path);
            throw new RuntimeException(e);
        }
        return cards;
    }

    public String downloadJson(String collectionToDownload){
        //call bulk-data
        ArrayList<BulkDataObject> data = bulkDataClient.get().retrieve().toEntity(BulkDataResponse.class).getBody().getData();
        //find collectionToDownload
        BulkDataObject selected = findCollection(collectionToDownload, data);

        log.info("Collection called: {} - Bulk Data retrieved: {} - Collection Selected: {}",collectionToDownload, data, selected);

        //call collectionToDownload
        byte[] response = restClient.get().uri(selected.getDownload_uri()).retrieve().body(byte[].class);
        log.info("length: {}", response.length);
        //find path to downloaded json
        //return path
        return "WIP";
    }

    private BulkDataObject findCollection(String collectionToDownload, ArrayList<BulkDataObject> data) {
        return data.stream().filter(o -> o.getType().equals(collectionToDownload)).collect(Collectors.toList()).getFirst();
    }
}
