package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class DatabaseUpdater {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUpdater.class);
    private final CardRepository cardRepository;
    private final Executor taskExecutor;

    @Autowired
    public DatabaseUpdater(CardRepository cardRepository, Executor taskExecutor) {
        this.cardRepository = cardRepository;
        this.taskExecutor = taskExecutor;
    }

    public boolean updateDb(ArrayList<CardPojo> cards) {
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

    @Async
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
