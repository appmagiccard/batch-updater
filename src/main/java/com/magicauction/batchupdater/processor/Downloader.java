package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.BulkDataObject;
import com.magicauction.batchupdater.entity.BulkDataResponse;
import com.magicauction.batchupdater.exceptions.RestCallException;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Downloader {

    private static final Logger log = LoggerFactory.getLogger(Downloader.class);
    private static final int N_SIZE = 1000;
    private final RestClient bulkDataClient;
    private final RestClient restClient;
    private final Loader loader;


    @Autowired
    public Downloader(Loader loader) {
        this.loader = loader;
        this.bulkDataClient = RestClient.builder().baseUrl("https://api.scryfall.com/bulk-data").build();
        this.restClient = RestClient.builder().build();
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
        ParameterizedTypeReference<ArrayList<HashMap<String, Object>>> typeListOfObject = new ParameterizedTypeReference<>() {};
        ArrayList<HashMap<String, Object>> response = restClient.get().uri(selected.getDownload_uri()).retrieve().body(typeListOfObject);
        if(response == null)
            throw new RestCallException();


        //save to disk
        List<List<HashMap<String, Object>>> lists = ListUtils.nSizeParts(response, N_SIZE);
        List<Path> paths = new ArrayList<>();
        for (List<HashMap<String, Object>> list: lists) {
            int indexOf = lists.indexOf(list);
            paths.add(loader.saveSmallerJsonToDisk(list, indexOf));
        }
        //return path
        return paths;
    }

    private BulkDataObject findCollection(String collectionToDownload, ArrayList<BulkDataObject> data) {
        return data.stream().filter(o -> o.getType().equals(collectionToDownload)).collect(Collectors.toList()).getFirst();
    }
}
