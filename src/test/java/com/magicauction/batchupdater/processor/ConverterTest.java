package com.magicauction.batchupdater.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicauction.batchupdater.entity.CardPojo;
import com.magicauction.batchupdater.entity.PriceMap;
import com.magicauction.batchupdater.entity.UriMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ConverterTest {

    private static final String JSON_TO_TEST = "{\n" +
            "\t\t\"object\": \"card\",\n" +
            "\t\t\"id\": \"86bf43b1-8d4e-4759-bb2d-0b2e03ba7012\",\n" +
            "\t\t\"oracle_id\": \"0004ebd0-dfd6-4276-b4a6-de0003e94237\",\n" +
            "\t\t\"multiverse_ids\": [\n" +
            "\t\t\t15862\n" +
            "\t\t],\n" +
            "\t\t\"mtgo_id\": 15870,\n" +
            "\t\t\"mtgo_foil_id\": 15871,\n" +
            "\t\t\"tcgplayer_id\": 3094,\n" +
            "\t\t\"cardmarket_id\": 3081,\n" +
            "\t\t\"name\": \"Static Orb\",\n" +
            "\t\t\"lang\": \"en\",\n" +
            "\t\t\"released_at\": \"2001-04-11\",\n" +
            "\t\t\"uri\": \"https://api.scryfall.com/cards/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012\",\n" +
            "\t\t\"scryfall_uri\": \"https://scryfall.com/card/7ed/319/static-orb?utm_source=api\",\n" +
            "\t\t\"layout\": \"normal\",\n" +
            "\t\t\"highres_image\": true,\n" +
            "\t\t\"image_status\": \"highres_scan\",\n" +
            "\t\t\"image_uris\": {\n" +
            "\t\t\t\"small\": \"https://cards.scryfall.io/small/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.jpg?1562242171\",\n" +
            "\t\t\t\"normal\": \"https://cards.scryfall.io/normal/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.jpg?1562242171\",\n" +
            "\t\t\t\"large\": \"https://cards.scryfall.io/large/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.jpg?1562242171\",\n" +
            "\t\t\t\"png\": \"https://cards.scryfall.io/png/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.png?1562242171\",\n" +
            "\t\t\t\"art_crop\": \"https://cards.scryfall.io/art_crop/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.jpg?1562242171\",\n" +
            "\t\t\t\"border_crop\": \"https://cards.scryfall.io/border_crop/front/8/6/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012.jpg?1562242171\"\n" +
            "\t\t},\n" +
            "\t\t\"mana_cost\": \"{3}\",\n" +
            "\t\t\"cmc\": 3.0,\n" +
            "\t\t\"type_line\": \"Artifact\",\n" +
            "\t\t\"oracle_text\": \"As long as Static Orb is untapped, players can't untap more than two permanents during their untap steps.\",\n" +
            "\t\t\"colors\": [],\n" +
            "\t\t\"color_identity\": [],\n" +
            "\t\t\"keywords\": [],\n" +
            "\t\t\"legalities\": {\n" +
            "\t\t\t\"standard\": \"not_legal\",\n" +
            "\t\t\t\"future\": \"not_legal\",\n" +
            "\t\t\t\"historic\": \"not_legal\",\n" +
            "\t\t\t\"timeless\": \"not_legal\",\n" +
            "\t\t\t\"gladiator\": \"not_legal\",\n" +
            "\t\t\t\"pioneer\": \"not_legal\",\n" +
            "\t\t\t\"explorer\": \"not_legal\",\n" +
            "\t\t\t\"modern\": \"not_legal\",\n" +
            "\t\t\t\"legacy\": \"legal\",\n" +
            "\t\t\t\"pauper\": \"not_legal\",\n" +
            "\t\t\t\"vintage\": \"legal\",\n" +
            "\t\t\t\"penny\": \"not_legal\",\n" +
            "\t\t\t\"commander\": \"legal\",\n" +
            "\t\t\t\"oathbreaker\": \"legal\",\n" +
            "\t\t\t\"brawl\": \"not_legal\",\n" +
            "\t\t\t\"historicbrawl\": \"not_legal\",\n" +
            "\t\t\t\"alchemy\": \"not_legal\",\n" +
            "\t\t\t\"paupercommander\": \"not_legal\",\n" +
            "\t\t\t\"duel\": \"legal\",\n" +
            "\t\t\t\"oldschool\": \"not_legal\",\n" +
            "\t\t\t\"premodern\": \"legal\",\n" +
            "\t\t\t\"predh\": \"legal\"\n" +
            "\t\t},\n" +
            "\t\t\"games\": [\n" +
            "\t\t\t\"paper\",\n" +
            "\t\t\t\"mtgo\"\n" +
            "\t\t],\n" +
            "\t\t\"reserved\": false,\n" +
            "\t\t\"foil\": false,\n" +
            "\t\t\"nonfoil\": true,\n" +
            "\t\t\"finishes\": [\n" +
            "\t\t\t\"nonfoil\"\n" +
            "\t\t],\n" +
            "\t\t\"oversized\": false,\n" +
            "\t\t\"promo\": false,\n" +
            "\t\t\"reprint\": true,\n" +
            "\t\t\"variation\": false,\n" +
            "\t\t\"set_id\": \"230f38aa-9511-4db8-a3aa-aeddbc3f7bb9\",\n" +
            "\t\t\"set\": \"7ed\",\n" +
            "\t\t\"set_name\": \"Seventh Edition\",\n" +
            "\t\t\"set_type\": \"core\",\n" +
            "\t\t\"set_uri\": \"https://api.scryfall.com/sets/230f38aa-9511-4db8-a3aa-aeddbc3f7bb9\",\n" +
            "\t\t\"set_search_uri\": \"https://api.scryfall.com/cards/search?order=set&q=e%3A7ed&unique=prints\",\n" +
            "\t\t\"scryfall_set_uri\": \"https://scryfall.com/sets/7ed?utm_source=api\",\n" +
            "\t\t\"rulings_uri\": \"https://api.scryfall.com/cards/86bf43b1-8d4e-4759-bb2d-0b2e03ba7012/rulings\",\n" +
            "\t\t\"prints_search_uri\": \"https://api.scryfall.com/cards/search?order=released&q=oracleid%3A0004ebd0-dfd6-4276-b4a6-de0003e94237&unique=prints\",\n" +
            "\t\t\"collector_number\": \"319\",\n" +
            "\t\t\"digital\": false,\n" +
            "\t\t\"rarity\": \"rare\",\n" +
            "\t\t\"flavor_text\": \"The warriors fought against the paralyzing waves until even their thoughts froze in place.\",\n" +
            "\t\t\"card_back_id\": \"0aeebaf5-8c7d-4636-9e82-8c27447861f7\",\n" +
            "\t\t\"artist\": \"Terese Nielsen\",\n" +
            "\t\t\"artist_ids\": [\n" +
            "\t\t\t\"eb55171c-2342-45f4-a503-2d5a75baf752\"\n" +
            "\t\t],\n" +
            "\t\t\"illustration_id\": \"6f8b3b2c-252f-4f95-b621-712c82be38b5\",\n" +
            "\t\t\"border_color\": \"white\",\n" +
            "\t\t\"frame\": \"1997\",\n" +
            "\t\t\"full_art\": false,\n" +
            "\t\t\"textless\": false,\n" +
            "\t\t\"booster\": true,\n" +
            "\t\t\"story_spotlight\": false,\n" +
            "\t\t\"edhrec_rank\": 3466,\n" +
            "\t\t\"prices\": {\n" +
            "\t\t\t\"usd\": \"18.83\",\n" +
            "\t\t\t\"usd_foil\": null,\n" +
            "\t\t\t\"usd_etched\": null,\n" +
            "\t\t\t\"eur\": \"10.28\",\n" +
            "\t\t\t\"eur_foil\": null,\n" +
            "\t\t\t\"tix\": \"0.12\"\n" +
            "\t\t},\n" +
            "\t\t\"related_uris\": {\n" +
            "\t\t\t\"gatherer\": \"https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=15862&printed=false\",\n" +
            "\t\t\t\"tcgplayer_infinite_articles\": \"https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&trafcat=infinite&u=https%3A%2F%2Finfinite.tcgplayer.com%2Fsearch%3FcontentMode%3Darticle%26game%3Dmagic%26partner%3Dscryfall%26q%3DStatic%2BOrb\",\n" +
            "\t\t\t\"tcgplayer_infinite_decks\": \"https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&trafcat=infinite&u=https%3A%2F%2Finfinite.tcgplayer.com%2Fsearch%3FcontentMode%3Ddeck%26game%3Dmagic%26partner%3Dscryfall%26q%3DStatic%2BOrb\",\n" +
            "\t\t\t\"edhrec\": \"https://edhrec.com/route/?cc=Static+Orb\"\n" +
            "\t\t},\n" +
            "\t\t\"purchase_uris\": {\n" +
            "\t\t\t\"tcgplayer\": \"https://tcgplayer.pxf.io/c/4931599/1830156/21018?subId1=api&u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F3094%3Fpage%3D1\",\n" +
            "\t\t\t\"cardmarket\": \"https://www.cardmarket.com/en/Magic/Products/Search?referrer=scryfall&searchString=Static+Orb&utm_campaign=card_prices&utm_medium=text&utm_source=scryfall\",\n" +
            "\t\t\t\"cardhoarder\": \"https://www.cardhoarder.com/cards/15870?affiliate_id=scryfall&ref=card-profile&utm_campaign=affiliate&utm_medium=card&utm_source=scryfall\"\n" +
            "\t\t}\n" +
            "\t}";

    private final ObjectMapper mapper = new ObjectMapper();


    @Test void convertToCardPojo_whenIsOk() throws JsonProcessingException {
        CardPojo expected = new CardPojo(
                "Static Orb",
                "86bf43b1-8d4e-4759-bb2d-0b2e03ba7012",
                "highres_scan",
                false,
                "7ed",
                new PriceMap(),
                new UriMap(),
                new UriMap(),
                new UriMap()
        );
        CardPojo actual = Converter.toCardPojo(jsonCard());
        assertEquals(expected, actual);
    }


    @Test void convertToCardPojo_whenIsOk_ButNotEquals() throws JsonProcessingException {
        CardPojo unexpected = new CardPojo(
                "Static Orb",
                "86bf43b1-8d4e-4759-bb2d-altered",
                "highres_scan",
                false,
                "7ed",
                new PriceMap(),
                new UriMap(),
                new UriMap(),
                new UriMap()
        );
        CardPojo actual = Converter.toCardPojo(jsonCard());
        assertNotEquals(unexpected, actual);
    }

    private JsonNode jsonCard() throws JsonProcessingException {
        return mapper.readTree(JSON_TO_TEST);
    }

}