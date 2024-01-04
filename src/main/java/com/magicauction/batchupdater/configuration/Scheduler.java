package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.exceptions.RestCallException;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import com.magicauction.batchupdater.processor.BulkGetBulkSaveDatabaseUpdater;
import com.magicauction.batchupdater.processor.BulkSaveDatabaseUpdater;
import com.magicauction.batchupdater.processor.Downloader;
import com.magicauction.batchupdater.processor.Loader;
import com.magicauction.batchupdater.processor.SingleCardAsyncDatabaseUpdater;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class Scheduler {

    private final Loader loader;
    private final BulkGetBulkSaveDatabaseUpdater bulkGetBulkSaveDatabaseUpdater;
    private final Downloader downloader;
    private final Executor taskExecutor;
    private static final String COLLECTION_TO_DOWNLOAD = "default_cards";
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final HikariDataSource hikariDataSource;

    @Autowired
    public Scheduler(Loader loader, BulkGetBulkSaveDatabaseUpdater bulkGetBulkSaveDatabaseUpdater, Downloader downloader, Executor taskExecutor, HikariDataSource hikariDataSource) {
        this.loader = loader;
        this.bulkGetBulkSaveDatabaseUpdater = bulkGetBulkSaveDatabaseUpdater;
        this.downloader = downloader;
        this.taskExecutor = taskExecutor;
        this.hikariDataSource = hikariDataSource;
    }

    @Scheduled(cron = "${cron-string}")
    public void scheduledProcess() throws WriteBigJsonToDiskException {
        Date start =  new Date();
        log.info("Started process on Date [{}]",start);
        boolean result = false;
        try{
        result = processAsyncWithExecutorService();
        }catch (RuntimeException e){
            log.error("Run time error -> MSG[{}]", e.getLocalizedMessage());
        }catch (Exception e){
            log.error("checked error -> MSG[{}]", e.getLocalizedMessage());
        }
        Date end =  new Date();
        Long elapsed = (end.toInstant().getEpochSecond() - start.toInstant().getEpochSecond());
        log.info("Process finished succesfully? result: {} - Date [{}] - Elapsed [{}]", result, end, elapsed);

    }

    public boolean processAsyncWithCompletableFutures() throws WriteBigJsonToDiskException, RestCallException {
        List<Path> pathsToJson = downloader.downloadJson(COLLECTION_TO_DOWNLOAD);
        ArrayList<CompletableFuture<Boolean>> futures = new ArrayList<>();

        for(Path path : pathsToJson){
            ArrayList<CardPojo> cards = loader.loadCardsFromJson(path);
            CompletableFuture<Boolean> pathCompletableFuture =
                    CompletableFuture.supplyAsync(() -> bulkGetBulkSaveDatabaseUpdater.update(cards), taskExecutor);
            futures.add(pathCompletableFuture);
        }
        List<Boolean> completedFutures = futures.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
        log.debug("Futuros Completados: {}", completedFutures);
        return !completedFutures.isEmpty();
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

    private boolean processWithBulkFinderAndBulkUpdater() throws WriteBigJsonToDiskException, RestCallException {
        List<Path> pathsToJson = downloader.downloadJson(COLLECTION_TO_DOWNLOAD);
        ArrayList<Boolean> partials = new ArrayList<>();
        for(Path path : pathsToJson){
            ArrayList<CardPojo> cards = loader.loadCardsFromJson(path);
            partials.add(bulkGetBulkSaveDatabaseUpdater.update(cards));
            log.info("finished with file: {}",path);
        }
        return partials.stream().reduce((one, two)->one&&two).orElse(false);
    }
}
