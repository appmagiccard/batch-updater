package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.processor.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class Initializer {

    private final Loader loader;

    @Autowired
    public Initializer(Loader loader) {
        this.loader = loader;
    }

    @Bean
    CommandLineRunner init() {
        ArrayList<CardPojo> cards = loader.loadCardsFromJson();
        return args -> {};
    }
}
