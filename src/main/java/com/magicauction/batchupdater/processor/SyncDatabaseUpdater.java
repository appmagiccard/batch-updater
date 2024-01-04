package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SyncDatabaseUpdater implements IDatabaseUpdater{

    private static final Logger log = LoggerFactory.getLogger(BulkSaveDatabaseUpdater.class);
    private final CardRepository cardRepository;

    public SyncDatabaseUpdater(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public boolean update(List<CardPojo> cardPojoList) {
        List<CardPojo> res = new ArrayList<>();
        for (CardPojo card : cardPojoList){
            CardPojo cardPojo = updateOneCard(card);
            res.add(cardPojo);
        }
        return !res.isEmpty();
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
            cardRepository.save(Converter.toEntity(card));
            log.debug("CARD NOT IS PRESENT: [{}] - [{}]", card.name(), card.scryfallId());
        }
        log.debug("Update finished for: {}", card.name());
        return card;
    }
}
