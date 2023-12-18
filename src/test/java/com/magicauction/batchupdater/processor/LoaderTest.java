package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.CardPojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LoaderTest {

    private Loader loader;
    private static final String JSON_PATH = "test_3cards.json";
    private static final String JSON_PATH_WITH_ERRORS = "test_3cards_with_errors.json";

    @BeforeEach void init_tests(){
        loader = new Loader();
    }

    @Test void load_whenIsOk(){
        ArrayList<CardPojo> cardPojos = loader.loadCardsFromJson(JSON_PATH);
        assertFalse(cardPojos.isEmpty());
    }
    @Test void load_whenIsNotOk(){
        assertThrows(RuntimeException.class, () -> loader.loadCardsFromJson(JSON_PATH_WITH_ERRORS));
    }

}