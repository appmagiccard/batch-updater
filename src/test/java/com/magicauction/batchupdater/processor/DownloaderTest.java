package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.ScryfallSetPojo;
import com.magicauction.batchupdater.exceptions.RestCallException;
import com.magicauction.batchupdater.exceptions.WriteBigJsonToDiskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DownloaderTest {

    Downloader downloader;
    private static final String COLLECTION_TO_DOWNLOAD = "default_cards";

    @Mock
    Loader loader;

    @Spy RestClient bulkDataClient;
    @Spy RestClient restClient;

    @BeforeEach
    void init(){
        downloader = new Downloader(loader);
    }

    @Test void downloadJson_whenIsOk() throws WriteBigJsonToDiskException, RestCallException {
        List<Path> paths = downloader.downloadJson(COLLECTION_TO_DOWNLOAD);
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
    }

    @Test void downloadSets_whenIsOk() throws RestCallException {
        List<ScryfallSetPojo> paths = downloader.downloadAllSets();
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
    }

}