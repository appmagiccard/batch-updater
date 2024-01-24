package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.MagicSet;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import com.magicauction.batchupdater.entity.repository.SetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BulkGetBulkSaveDatabaseUpdater implements IDatabaseUpdater{

    private static final Logger log = LoggerFactory.getLogger(BulkGetBulkSaveDatabaseUpdater.class);
    private final CardRepository cardRepository;
    private final SetRepository setRepository;

    @Autowired
    public BulkGetBulkSaveDatabaseUpdater(CardRepository cardRepository, SetRepository setRepository) {
        this.cardRepository = cardRepository;
        this.setRepository = setRepository;
    }

    //findAllByScryfallId
    @Override
    public boolean update(List<CardPojo> cardPojoList) {
        List<String> scryfallIds = cardPojoList.stream().map(CardPojo::scryfallId).collect(Collectors.toList());
        List<Card> allByScryfallId = cardRepository.findAllByScryfallIdIn(scryfallIds);
        List<Card> result = cardPojoList.stream().map(newCard -> findCardAndMatch(newCard, allByScryfallId)).collect(Collectors.toList());
        cardRepository.saveAll(result);
        return true;
    }

    private Card findCardAndMatch(CardPojo newCard, List<Card> allByScryfallId) {

        Optional<Card> oldCOpt = allByScryfallId.stream().filter(oldc -> oldc.getScryfallId().equals(newCard.scryfallId())).findFirst();
        Card card;
        if (oldCOpt.isPresent()){
            //update existing card
            Card oldC = oldCOpt.get();
            oldC.setPrices(newCard.prices().toString());
            oldC.setLastModification(new Date());
            card = oldC;
            log.debug("CARD IS PRESENT: [{}] - [{}]", oldC.getName(), oldC.getScryfallId());
        }else{
            //add new card
            Optional<MagicSet> byCode = setRepository.findByCode(newCard.setName());
            card = Converter.toEntity(newCard, byCode.orElse(null));
            log.debug("CARD NOT IS PRESENT: [{}] - [{}]", newCard.name(), newCard.scryfallId());
        }
        log.debug("Update finished for: {}", newCard.name());
        return card;
    }

}
