package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.CardPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;

@Component
public class Loader {

    private static final Logger log = LoggerFactory.getLogger(Loader.class);
    private final Converter converter;
    private final ObjectMapper mapper;

    @Autowired
    public Loader(Converter converter) {
        this.converter = converter;
        this.mapper = new ObjectMapper();
    }

    public ArrayList<CardPojo> loadCardsFromJson(){
        ArrayList<CardPojo> cards = new ArrayList<>();
        String path = "test_3cards.json";
        log.info("Loading from path: {}", path);

        try(InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream(path)){
            JsonNode arrayNode = mapper.readTree(in);
            if (arrayNode.isArray())
                for(JsonNode node : arrayNode)
                    cards.add(converter.toCardPojo(node));

            log.info("Cards transformed successfully - result {}",cards);
        }
        catch(Exception e){
            log.error("Error loading from: {}", path);
            throw new RuntimeException(e);
        }
        return cards;
    }
}
