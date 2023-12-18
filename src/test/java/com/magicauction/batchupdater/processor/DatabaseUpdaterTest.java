package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class DatabaseUpdaterTest {

    @InjectMocks private final DatabaseUpdater databaseUpdater;
    @Mock private final CardRepository cardRepository;

    DatabaseUpdaterTest(DatabaseUpdater databaseUpdater, CardRepository cardRepository) {
        this.databaseUpdater = databaseUpdater;
        this.cardRepository = cardRepository;
    }

    @Test void updateDB_whenIsOk(){}
    @Test void updateDB_whenIsNotOk(){}
}