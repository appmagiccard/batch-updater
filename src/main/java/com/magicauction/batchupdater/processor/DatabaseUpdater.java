package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DatabaseUpdater {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUpdater.class);
    private final CardRepository cardRepository;

    @Autowired
    public DatabaseUpdater(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public boolean updateDb(ArrayList<CardPojo> cards) {
        log.info("Starting loading Database with: {}", cards);
        ArrayList<Card> all = (ArrayList<Card>) cardRepository.findAll();
        Map<Boolean, List<CardPojo>> map = cards.stream().collect(Collectors.partitioningBy(nc -> cardExistOnDatabase(nc, all)));

        log.debug("map.true: {}", map.get(Boolean.TRUE));
        log.debug("map.false: {}", map.get(Boolean.FALSE));

        boolean newCardsLoaded = loadNewCards(map.get(Boolean.FALSE));
        boolean updatedExistingCards = updateExistingCards(map.get(Boolean.TRUE));
        return newCardsLoaded && updatedExistingCards;
    }

    private boolean updateExistingCards(List<CardPojo> cards) {
        for (CardPojo card : cards){
            Card oldC = cardRepository.findByMultiverseId(card.multiverseId()).orElseThrow(RuntimeException::new);
            oldC.setPrices(card.prices().toString());
            cardRepository.save(oldC);
        }
        return Boolean.TRUE;
    }

    private boolean loadNewCards(List<CardPojo> cards) {
        for (CardPojo card : cards){
            Card c = toDb(card);
            cardRepository.save(c);
        }
        return Boolean.TRUE;
    }

    private Card toDb(CardPojo card) {
        Card nc = new Card();
        nc.setName(card.name());
        nc.setMultiverseId(card.multiverseId());
        nc.setImgStatus(card.imgStatus());
        nc.setFoil(card.isFoil());
        nc.setSetName(card.setName());
        nc.setPrices(card.prices().toString());
        nc.setRelatedUri(card.relatedUri().toString());
        nc.setImageUri(card.imageUri().toString());
        nc.setPurchaseUri(card.purchaseUri().toString());
        log.debug("Pojo translated: og {} - toDb {}", card, nc);
        return nc;
    }

    private boolean cardExistOnDatabase(CardPojo nc, ArrayList<Card> cards){
        boolean exist = false;
        for (Card card : cards){
            if(!exist)
               exist = card.getMultiverseId().equals(nc.multiverseId());
        }
        return exist;
    }
}
