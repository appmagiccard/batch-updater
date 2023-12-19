package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.processor.Converter;
import com.magicauction.batchupdater.processor.DatabaseUpdater;
import com.magicauction.batchupdater.processor.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class Initializer {

    private final Loader loader;
    private final DatabaseUpdater updater;
    private static final String JSON_PATH = "test_3cards.json";
    private static final Logger log = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    public Initializer(Loader loader, DatabaseUpdater updater) {
        this.loader = loader;
        this.updater = updater;
    }

    //TODO: VA A ESTAR DIFICIL DE TESTEAR
    @Bean
    CommandLineRunner init() {
        loader.downloadJson("default_cards");
        ArrayList<CardPojo> cards = loader.loadCardsFromJson(JSON_PATH);
        boolean result = updater.updateDb(cards);
        return args -> log.info("Process finished succesfully? result: {}", result);
    }
}
