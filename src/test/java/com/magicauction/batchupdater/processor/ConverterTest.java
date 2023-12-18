package com.magicauction.batchupdater.processor;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ConverterTest {

    @InjectMocks private final Converter converter;


    ConverterTest(Converter converter) {
        this.converter = converter;
    }

    @Test void convertToCardPojo_whenIsOk(){}
    @Test void convertToCardPojo_whenIsNotOk(){}

}