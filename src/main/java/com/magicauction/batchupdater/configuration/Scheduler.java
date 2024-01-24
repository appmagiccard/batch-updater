package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.ScryfallSetPojo;
import com.magicauction.batchupdater.exceptions.RestCallException;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import com.magicauction.batchupdater.processor.BulkGetBulkSaveDatabaseUpdater;
import com.magicauction.batchupdater.processor.Downloader;
import com.magicauction.batchupdater.processor.Loader;
import com.magicauction.batchupdater.processor.SetDatabaseUpdater;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class Scheduler {

    private final Loader loader;
    private final BulkGetBulkSaveDatabaseUpdater bulkGetBulkSaveDatabaseUpdater;
    private final Downloader downloader;
    private static final String COLLECTION_TO_DOWNLOAD = "default_cards";
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final HikariDataSource hikariDataSource;
    private final SetDatabaseUpdater setDatabaseUpdater;

    @Autowired
    public Scheduler(Loader loader, BulkGetBulkSaveDatabaseUpdater bulkGetBulkSaveDatabaseUpdater, Downloader downloader, HikariDataSource hikariDataSource, SetDatabaseUpdater setDatabaseUpdater) {
        this.loader = loader;
        this.bulkGetBulkSaveDatabaseUpdater = bulkGetBulkSaveDatabaseUpdater;
        this.downloader = downloader;
        this.hikariDataSource = hikariDataSource;
        this.setDatabaseUpdater = setDatabaseUpdater;
    }

    @Scheduled(cron = "${cron-cards}")
    public void scheduledProcess() throws WriteBigJsonToDiskException {
        Date start =  new Date();
        log.info("Started process on Date [{}]",start);
        boolean resultCards = false;
        boolean resultSets = false;
        try{
        resultSets = updateSets();
        resultCards = processAsyncWithExecutorService();
        }catch (RuntimeException e){
            log.error("Run time error -> MSG[{}]", e.getLocalizedMessage());
        }catch (Exception e){
            log.error("checked error -> MSG[{}]", e.getLocalizedMessage());
        }
        Date end =  new Date();
        Long elapsed = (end.toInstant().getEpochSecond() - start.toInstant().getEpochSecond());
        log.info("Process finished succesfully? result: {} - Date [{}] - Elapsed [{}]", resultCards && resultSets, end, elapsed);
    }


    private boolean updateSets() throws RestCallException {
        List<ScryfallSetPojo> setPojos = downloader.downloadAllSets();
        return setDatabaseUpdater.update(setPojos);
    }

    public boolean processAsyncWithExecutorService() throws WriteBigJsonToDiskException, RestCallException {
        List<Path> pathsToJson = downloader.downloadJson(COLLECTION_TO_DOWNLOAD);
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<Callable<Void>> callables = pathsToJson.stream().map(path -> (Callable<Void>)() -> {
            ArrayList<CardPojo> cards = loader.loadCardsFromJson(path);
            bulkGetBulkSaveDatabaseUpdater.update(cards);
            log.info("finished with file: {}",path);
            return null;
        }).collect(Collectors.toList());
        try{
            executorService.invokeAll(callables);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !callables.isEmpty();
    }
}
