package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import com.magicauction.batchupdater.processor.DatabaseUpdater;
import com.magicauction.batchupdater.processor.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

@Component
public class Scheduler {

    private final Loader loader;
    private final DatabaseUpdater updater;
    private static final String COLLECTION_TO_DOWNLOAD = "default_cards";
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    public Scheduler(Loader loader, DatabaseUpdater updater) {
        this.loader = loader;
        this.updater = updater;
    }

    @Scheduled(cron = "${cron-string}")
    public void scheduledProcess() throws WriteBigJsonToDiskException {
        Date start =  new Date();
        log.info("Started process on Date [{}]",start);
        boolean result = process();
        Date end =  new Date();
        Long elapsed = (end.toInstant().getEpochSecond() - start.toInstant().getEpochSecond());
        log.info("Process finished succesfully? result: {} - Date [{}] - Elapsed [{}]", result, end, elapsed);

    }

    private boolean process() throws WriteBigJsonToDiskException {
        Path pathToJson = loader.downloadJson(COLLECTION_TO_DOWNLOAD);
        ArrayList<CardPojo> cards = loader.loadCardsFromJson(pathToJson);
        return updater.updateDb(cards);
    }
}
