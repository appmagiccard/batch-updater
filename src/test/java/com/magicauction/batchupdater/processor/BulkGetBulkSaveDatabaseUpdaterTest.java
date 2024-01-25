package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.Card;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.MagicSet;
import com.magicauction.batchupdater.entity.repository.CardRepository;
import com.magicauction.batchupdater.entity.repository.SetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkGetBulkSaveDatabaseUpdaterTest {

    BulkGetBulkSaveDatabaseUpdater updater;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private SetRepository setRepository;

    @BeforeEach
    void init(){
        updater = new BulkGetBulkSaveDatabaseUpdater(cardRepository, setRepository);
    }

    @Test
    void test_whenOneUpdateAndOneInsert(){
        when(cardRepository.findAllByScryfallIdIn(any())).thenReturn(oldCards());
        List<CardPojo> cards = cards();
        boolean update = updater.update(cards);
        assertTrue(update);
    }

    private List<Card> oldCards() {
        ArrayList<Card> cards = new ArrayList<>();
        Card c = new Card();
        c.setRelatedUri("{tcgplayer_infinite_decks=https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&trafcat=infinite&u=https%3A%2F%2Finfinite.tcgplayer.com%2Fsearch%3FcontentMode%3Ddeck%26game%3Dmagic%26partner%3Dscryfall%26q%3DFeral%2BInvocation, gatherer=https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=489379&printed=false, edhrec=https://edhrec.com/route/?cc=Feral+Invocation, tcgplayer_infinite_articles=https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&trafcat=infinite&u=https%3A%2F%2Finfinite.tcgplayer.com%2Fsearch%3FcontentMode%3Darticle%26game%3Dmagic%26partner%3Dscryfall%26q%3DFeral%2BInvocation}");
        c.setPrices("{usd_foil=0.0, usd_etched=0.0, tix=0.0, eur=0.12, usd=0.02, eur_foil=0.0}");
        c.setPurchaseUri("{cardmarket=https://www.cardmarket.com/en/Magic/Products/Search?referrer=scryfall&searchString=Feral+Invocation&utm_campaign=card_prices&utm_medium=text&utm_source=scryfall, cardhoarder=https://www.cardhoarder.com/cards?affiliate_id=scryfall&data%5Bsearch%5D=Feral+Invocation&ref=card-profile&utm_campaign=affiliate&utm_medium=card&utm_source=scryfall, tcgplayer=https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F216455%3Fpage%3D1}");
        c.setImageUri("{small=https://cards.scryfall.io/small/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.jpg?1601082585, normal=https://cards.scryfall.io/normal/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.jpg?1601082585, large=https://cards.scryfall.io/large/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.jpg?1601082585, png=https://cards.scryfall.io/png/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.png?1601082585, border_crop=https://cards.scryfall.io/border_crop/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.jpg?1601082585, art_crop=https://cards.scryfall.io/art_crop/front/1/9/190ad379-1a0f-4598-b5b1-453955846597.jpg?1601082585}");
        c.setImgStatus("highres_scan");
        c.setName("Feral Invocation");
        c.setId(1L);
        c.setFoil(true);
        c.setScryfallId("190ad379-1a0f-4598-b5b1-453955846597");
        c.setMagicSet(new MagicSet());
        c.setLastModification(new Date());
        cards.add(c);
        return cards;
    }

    private List<CardPojo> cards() {
        ArrayList<CardPojo> cards = new ArrayList<>();
        CardPojo c1 = new CardPojo(
                "Feral Invocation",
                "190ad379-1a0f-4598-b5b1-453955846597",
                "highres_scan",
                false,
                "Jumpstart",
                new Date(),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0)
        );
        CardPojo c2 = new CardPojo(
                "Fury Sliver",
                "0000579f-7b35-4ed3-b44c-db2a538066fe",
                "highres_scan",
                false,
                "Time Spiral",
                new Date(),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0)
        );
        cards.add(c1);
        cards.add(c2);
        return cards;
    }
}