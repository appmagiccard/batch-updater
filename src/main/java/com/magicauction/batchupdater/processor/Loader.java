package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.BulkDataObject;
import com.magicauction.batchupdater.entity.BulkDataResponse;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class Loader {

    private static final Logger log = LoggerFactory.getLogger(Loader.class);
    private static final String PATH_DOWNLOADED = "big_json.json";
    private final ObjectMapper mapper;

    private final RestClient bulkDataClient;
    private final RestClient restClient;

    @Autowired
    public Loader() {
        this.bulkDataClient = RestClient.builder().baseUrl("https://api.scryfall.com/bulk-data").build();
        this.restClient = RestClient.builder().build();
        this.mapper = new ObjectMapper();
    }

    public ArrayList<CardPojo> loadCardsFromJson(Path path){
        ArrayList<CardPojo> cards = new ArrayList<>();
        log.info("Loading from path: {}", path);

        try{
            byte[] in = Files.readAllBytes(path);
            JsonNode arrayNode = mapper.readTree(in);
            if (arrayNode.isArray())
                for(JsonNode node : arrayNode)
                    cards.add(Converter.toCardPojo(node));

            log.info("Cards transformed successfully - result {}",cards);
        }
        catch(Exception e){
            log.error("Error loading from: {} - {}", path, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return cards;
    }

    private Path saveBigJsonToDisk(byte[] response, int size) throws WriteBigJsonToDiskException {
        if(response != null){
            if(size == response.length) {
                log.info("length: {}", response.length);
                try {
                    Path path = Paths.get(PATH_DOWNLOADED);
                    Files.write(path, response);
                    //mapper.writeValue(Paths.get(PATH_DOWNLOADED).toAbsolutePath().toFile(), response);
                    return path;
                } catch (IOException e) {
                    log.error("IOEXCEPTION OCURRED! MSG: {}", e.getLocalizedMessage());
                    throw new WriteBigJsonToDiskException(e.getLocalizedMessage());
                }
            }
            else
                log.info("File size not ok: expected {} - actual {}", size, response.length);
        }
        throw new WriteBigJsonToDiskException("ERROR ON SAVE FILE");
    }

    public Path downloadJson(String collectionToDownload) throws WriteBigJsonToDiskException {
        //call bulk-data
        ArrayList<BulkDataObject> data = bulkDataClient.get().retrieve().toEntity(BulkDataResponse.class).getBody().getData();
        //find collectionToDownload
        BulkDataObject selected = findCollection(collectionToDownload, data);

        log.info("Collection called: {} - Bulk Data retrieved: {} - Collection Selected: {}",collectionToDownload, data, selected);

        //call collectionToDownload
        //byte[] response = {};
        byte[] response = restClient.get().uri(selected.getDownload_uri()).retrieve().body(byte[].class);
        //save to disk

        Path path = saveBigJsonToDisk(response, Integer.parseInt(selected.getSize()));
        //return path
        return path;
    }

    private BulkDataObject findCollection(String collectionToDownload, ArrayList<BulkDataObject> data) {
        return data.stream().filter(o -> o.getType().equals(collectionToDownload)).collect(Collectors.toList()).getFirst();
    }
}
