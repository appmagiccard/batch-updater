package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.BulkDataObject;
import com.magicauction.batchupdater.entity.BulkDataResponse;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.exceptions.RestCallException;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Loader {

    private static final Logger log = LoggerFactory.getLogger(Loader.class);
    private static final String PATH_DOWNLOADED = "big_json.json";
    private static final int N_SIZE = 1000;
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

            log.debug("Cards transformed successfully - result {}",cards);
        }
        catch(Exception e){
            log.error("Error loading from: {} - {}", path, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return cards;
    }

    private Path saveSmallerJsonToDisk(List<HashMap<String, Object>> list) throws WriteBigJsonToDiskException {
        if(list == null || list.isEmpty() )
            throw new WriteBigJsonToDiskException("No Elements to Add to Paths!");

        log.info("working with valid list: {}", list);
        Path path;
        try {
            String name = UUID.randomUUID().toString().concat("_").concat(PATH_DOWNLOADED);
            log.info("Creating file: {}", name);
            path = Paths.get(name);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(list);
            byte[] bytes = bos.toByteArray();
            Files.write(path, bytes);
            return path;
        } catch (IOException e) {
            log.error("IOEXCEPTION OCURRED! MSG: {}", e.getLocalizedMessage());
            throw new WriteBigJsonToDiskException(e.getLocalizedMessage());
        }
    }

    private Path saveBigJsonToDisk(byte[] response, int size) throws WriteBigJsonToDiskException {
        if(response != null){
            if(size == response.length) {
                log.info("Download Successfully! length: {}", response.length);
                try {
                    Path path = Paths.get(PATH_DOWNLOADED);
                    Files.write(path, response);
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

    public List<Path> downloadJson(String collectionToDownload) throws WriteBigJsonToDiskException, RestCallException {
        //call bulk-data
        ArrayList<BulkDataObject> data = bulkDataClient.get().retrieve().toEntity(BulkDataResponse.class).getBody().getData();
        //find collectionToDownload
        BulkDataObject selected = findCollection(collectionToDownload, data);

        log.info("Collection called: {} - Bulk Data retrieved: {} - Collection Selected: {}",collectionToDownload, data, selected);

        //call collectionToDownload

        /*
        * TODO: ACA HAY QUE PROBAR REACTIVE/WEBFLUX
        * Con reactive podriamos hacer el update entero de un solo tiron!
        */
        //byte[] response = restClient.get().uri(selected.getDownload_uri()).retrieve().body(byte[].class);
        ParameterizedTypeReference<ArrayList<HashMap<String, Object>>> typeListOfObject = new ParameterizedTypeReference<>() {};
        ArrayList<HashMap<String, Object>> response = restClient.get().uri(selected.getDownload_uri()).retrieve().body(typeListOfObject);
        if(response == null)
            throw new RestCallException();


        //save to disk
        //Path path = saveBigJsonToDisk(response, Integer.parseInt(selected.getSize()));
        List<List<HashMap<String, Object>>> lists = ListUtils.nSizeParts(response, N_SIZE);
        List<Path> paths = new ArrayList<>();
        for (List<HashMap<String, Object>> list:
                lists) {
            paths.add(saveSmallerJsonToDisk(list));
        }
        //return path
        return paths;
    }



    private BulkDataObject findCollection(String collectionToDownload, ArrayList<BulkDataObject> data) {
        return data.stream().filter(o -> o.getType().equals(collectionToDownload)).collect(Collectors.toList()).getFirst();
    }
}
