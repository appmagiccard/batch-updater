package com.magicauction.batchupdater.entity;

import java.util.ArrayList;

public class ScryfallSetDataResponse extends Response<ScryfallSetPojo>{
    public ScryfallSetDataResponse(ArrayList<ScryfallSetPojo> data, boolean hasMore, String object) {
        super(data, hasMore, object);
    }
}
