package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class Loader {

    private static final Logger log = LoggerFactory.getLogger(Loader.class);
    private static final String PATH_DOWNLOADED = "F:\\AuctionHouseMagic\\jsons\\%s_%s.json";

    private final ObjectMapper mapper;

    @Autowired
    public Loader() {
        this.mapper = new ObjectMapper();
    }

    public ArrayList<CardPojo> loadCardsFromJson(Path path){
        ArrayList<CardPojo> cards = new ArrayList<>();
        log.info("Loading from path: {}", path);

        try{
            String in = Files.readString(path);
            JsonNode arrayNode = mapper.readTree(in);
            if (arrayNode.isArray())
                for(JsonNode node : arrayNode)
                    cards.add(Converter.toCardPojo(node));

            log.debug("Cards transformed successfully - result {}",cards);
            log.info("Deleting file: {}", path);
            Files.delete(path);
        }
        catch(Exception e){
            log.error("Error loading from: {} - {}", path, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return cards;
    }

    public Path saveSmallerJsonToDisk(List<HashMap<String, Object>> list, int index) throws WriteBigJsonToDiskException {
        if(list == null || list.isEmpty() )
            throw new WriteBigJsonToDiskException("No Elements to Add to Paths!");

        log.debug("working with valid list: {}", list);
        Path path;
        try {
            path = Paths.get(buildPath(index));
            log.info("Creating file: {}", path.toAbsolutePath());
            String listAsString = mapper.writeValueAsString(list);
            BufferedWriter fw = new BufferedWriter(new FileWriter(path.toFile()));
            fw.write(listAsString);
            fw.flush();
            fw.close();
            return path;
        } catch (IOException e) {
            log.error("IOEXCEPTION OCURRED! MSG: {}", e.getLocalizedMessage());
            throw new WriteBigJsonToDiskException(e.getLocalizedMessage());
        }
    }

    private String buildPath(int i){
        return String.format(PATH_DOWNLOADED, i,UUID.randomUUID());
    }
}
