package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.PriceMap;
import com.magicauction.batchupdater.entity.UriMap;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUpdaterTest {

    private static final String SCRYFALL_1 = "1";
    private static final String SCRYFALL_2 = "2";
    private static final String SCRYFALL_3 = "3";
    private DatabaseUpdater databaseUpdater;
    @Mock private CardRepository cardRepository;

    @BeforeEach
    void init_tests(){
        databaseUpdater = new DatabaseUpdater(cardRepository);
    }

    @Test
    void updateDB_whenIsNotOk(){
        assertThrows(RuntimeException.class, () -> databaseUpdater.updateDb(cardPojoList()));
    }
    @Test void updateDB_whenIsOk(){
        when(cardRepository.findAll()).thenReturn(cardList());
        when(cardRepository.findByScryfallId(SCRYFALL_1)).thenReturn(Optional.of(card(SCRYFALL_1)));
        when(cardRepository.findByScryfallId(SCRYFALL_2)).thenReturn(Optional.of(card(SCRYFALL_2)));
        when(cardRepository.findByScryfallId(SCRYFALL_3)).thenReturn(Optional.of(card(SCRYFALL_3)));
        boolean b = databaseUpdater.updateDb(cardPojoList());
        assertTrue(b);
    }

    private ArrayList<CardPojo> cardPojoList() {
        ArrayList<CardPojo> list = new ArrayList<>();
        list.add(cardPojo(SCRYFALL_1));
        list.add(cardPojo(SCRYFALL_2));
        list.add(cardPojo(SCRYFALL_3));
        list.add(cardPojo("4"));
        return list;
    }

    private CardPojo cardPojo(String scryfallId) {
        return new CardPojo(
                "name"+" "+scryfallId,
                scryfallId,
                "status"+" "+scryfallId,
                true,
                "SetName"+" "+scryfallId,
                new PriceMap(),
                new UriMap(),
                new UriMap(),
                new UriMap()
        );
    }

    private List<Card> cardList() {
        List<Card> list = new ArrayList<>();
        list.add(card(SCRYFALL_1));
        list.add(card(SCRYFALL_2));
        list.add(card(SCRYFALL_3));
        return list;
    }

    private Card card(String scryfallId) {
        Card card = new Card();
        card.setId(Long.valueOf(scryfallId));
        card.setScryfallId(scryfallId);
        card.setName("name "+scryfallId);
        card.setSetName("setName "+scryfallId);
        card.setImgStatus("status "+scryfallId);
        card.setFoil(true);
        card.setPrices("prices "+scryfallId);
        card.setImageUri("imgUri "+scryfallId);
        card.setPurchaseUri("purchase "+scryfallId);
        card.setRelatedUri("relUri "+scryfallId);
        return card;
    }


}