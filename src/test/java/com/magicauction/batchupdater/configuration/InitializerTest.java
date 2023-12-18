package com.magicauction.batchupdater.configuration;

import com.magicauction.batchupdater.processor.DatabaseUpdater;
import com.magicauction.batchupdater.processor.Loader;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class InitializerTest {
    //TODO: VA A ESTAR DIFICIL DE TESTEAR

    @InjectMocks private final Initializer initializer;
    @Mock private final Loader loader;
    @Mock private final DatabaseUpdater updater;

    InitializerTest(Initializer initializer, Loader loader, DatabaseUpdater updater) {
        this.initializer = initializer;
        this.loader = loader;
        this.updater = updater;
    }
}