package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class BulkSaveDatabaseUpdater {

    private static final Logger log = LoggerFactory.getLogger(BulkSaveDatabaseUpdater.class);
    private final CardRepository cardRepository;
    private final Executor taskExecutor;
    private final HikariDataSource hikariDataSource;

    @Autowired
    public BulkSaveDatabaseUpdater(CardRepository cardRepository, Executor taskExecutor, HikariDataSource hikariDataSource) {
        this.cardRepository = cardRepository;
        this.taskExecutor = taskExecutor;
        this.hikariDataSource = hikariDataSource;
    }

    public boolean updateDbAsync(ArrayList<CardPojo> cards) {
        ArrayList<CompletableFuture<CardPojo>> futures = new ArrayList<>();
        for(CardPojo c : cards){
            CompletableFuture<CardPojo> cardPojoCompletableFuture =
                    CompletableFuture.supplyAsync(() -> updateOneCard(c), taskExecutor);
            futures.add(cardPojoCompletableFuture);
        }
        List<CardPojo> completedFutures = futures.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
        log.debug("Futuros Completados: {}", completedFutures);
        return !completedFutures.isEmpty();
    }




    public boolean updateDb(ArrayList<CardPojo> cards) {
        List<CardPojo> res = new ArrayList<>();
        for (CardPojo card : cards){
            CardPojo cardPojo = updateOneCard(card);
            res.add(cardPojo);
        }
        return !res.isEmpty();
    }

    public boolean update(ArrayList<CardPojo> cards) {
        List<Card> res = new ArrayList<>();
        for (CardPojo card : cards){
            Card cardRes = updateOneCardWithNoSave(card);
            res.add(cardRes);
        }
        cardRepository.saveAll(res);
        log.info("Save all: {}", res);
        return !res.isEmpty();
    }


    private Card updateOneCardWithNoSave(CardPojo cardPojo){
        Card cardRes;
        Optional<Card> oldCOpt = cardRepository.findByScryfallId(cardPojo.scryfallId());
        if (oldCOpt.isPresent()){
            //update existing card
            Card oldC = oldCOpt.get();
            oldC.setPrices(cardPojo.prices().toString());
            oldC.setLastModification(new Date());
            cardRes = oldC;
            log.debug("CARD IS PRESENT: [{}] - [{}]", oldC.getName(), oldC.getScryfallId());
        }else{
            //add new card
            cardRes = Converter.toEntity(cardPojo, null);
            log.debug("CARD NOT IS PRESENT: [{}] - [{}]", cardPojo.name(), cardPojo.scryfallId());
        }
        log.info("Update finished for: {}", cardPojo.name());
        return cardRes;
    }

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
            cardRepository.save(Converter.toEntity(card, null));
            log.debug("CARD NOT IS PRESENT: [{}] - [{}]", card.name(), card.scryfallId());
        }
        log.debug("Update finished for: {}", card.name());
        return card;
    }

}
