package com.magicauction.batchupdater.entity;

import java.util.ArrayList;

public class BulkDataResponse extends Response<BulkDataObject> {

    public BulkDataResponse(ArrayList<BulkDataObject> data, boolean hasMore, String object) {
        super(data, hasMore, object);
    }
}
