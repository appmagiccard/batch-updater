package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.MagicSet;
import com.magicauction.batchupdater.entity.ScryfallSetPojo;
import com.magicauction.batchupdater.entity.repository.SetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SetDatabaseUpdaterTest {

    @InjectMocks
    private SetDatabaseUpdater updater;

    @Mock
    private SetRepository repository;

    @BeforeEach
    void init(){
        updater = new SetDatabaseUpdater(repository);
    }

    @Test void updateSets_whenIsOk(){
        List<MagicSet> mss = new ArrayList<>();
        mss.add(ms(1L));
        mss.add(ms(2L));
        mss.add(ms(3L));
        when(repository.findAll()).thenReturn(mss);
        List<ScryfallSetPojo> setsInput = new ArrayList<>();
        setsInput.add(msU(1L));
        setsInput.add(msU(2L));
        setsInput.add(msU(3L));
        setsInput.add(msU(4L));
        boolean actual = updater.update(setsInput);
        assertTrue(actual);
    }

    private ScryfallSetPojo msU(long i) {
        return new ScryfallSetPojo(
                "Set "+i,
                "scryfall id: "+i,
                "Code"+i,
                new Date().toString(),
                "type: "+i,
                Math.toIntExact(i * 100L),
                null,
                i >= 1,
                "uri: "+i
        );
    }

    private MagicSet ms(Long i) {
        MagicSet ms = new MagicSet();
        ms.setId(i);
        ms.setName("Set "+i);
        ms.setCardCount(Math.toIntExact(i * 100L));
        ms.setSetType("type: "+i);
        ms.setDigital(i >= 1);
        ms.setReleasedAt(new Date().toString());
        ms.setSearchUri("uri: "+i);
        ms.setScryfallId("scryfall id: "+i);
        ms.setCode("Code"+i);
        ms.setParentSetCode(null);
        return ms;
    }

}