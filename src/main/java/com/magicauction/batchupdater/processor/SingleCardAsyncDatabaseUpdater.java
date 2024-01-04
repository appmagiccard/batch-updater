package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SingleCardAsyncDatabaseUpdater implements IDatabaseUpdater{

    private static final Logger log = LoggerFactory.getLogger(SingleCardAsyncDatabaseUpdater.class);
    private final CardRepository cardRepository;
    private final Executor taskExecutor;
    private final HikariDataSource hikariDataSource;

    public SingleCardAsyncDatabaseUpdater(CardRepository cardRepository, Executor taskExecutor, HikariDataSource hikariDataSource) {
        this.cardRepository = cardRepository;
        this.taskExecutor = taskExecutor;
        this.hikariDataSource = hikariDataSource;
    }


    @Override
    public boolean update(List<CardPojo> cardPojoList) {
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<Callable<Void>> callables = cardPojoList.stream().map(c -> (Callable<Void>)() -> {
            updateOneCard(c);
            return null;
        }).collect(Collectors.toList());
        try{
            executorService.invokeAll(callables);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !callables.isEmpty();
    }

    //@Async
    private CardPojo updateOneCard(CardPojo card) {
        Optional<Card> oldCOpt = cardRepository.findByScryfallId(card.scryfallId());
        log.debug("<------?!??! ~~eXeC7t0r: {}~~ ?!??!------>", Thread.currentThread().getName());
        if (oldCOpt.isPresent()){
            //update existing card
            Card oldC = oldCOpt.get();
            oldC.setPrices(card.prices().toString());
            oldC.setLastModification(new Date());
            cardRepository.save(oldC);
            log.debug("CARD IS PRESENT: [{}] - [{}]", oldC.getName(), oldC.getScryfallId());
        }else{
            //add new card
            cardRepository.save(Converter.toEntity(card));
            log.debug("CARD NOT IS PRESENT: [{}] - [{}]", card.name(), card.scryfallId());
        }
        log.info("Update finished for: {}", card.name());
        return card;
    }
}
