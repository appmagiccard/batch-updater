package com.magicauction.batchupdater.entity;

import java.util.ArrayList;

public class BulkDataResponse {
    private ArrayList<BulkDataObject> data;
    private boolean hasMore;
    private String object;

    public ArrayList<BulkDataObject> getData() {
        return data;
    }

    public void setData(ArrayList<BulkDataObject> data) {
        this.data = data;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public BulkDataResponse() {
    }

    public BulkDataResponse(ArrayList<BulkDataObject> data, boolean hasMore, String object) {
        this.data = data;
        this.hasMore = hasMore;
        this.object = object;
    }

    @Override
    public String toString() {
        return "BulkDataResponse{" +
                "data=" + data +
                ", hasMore=" + hasMore +
                ", object='" + object + '\'' +
                '}';
    }
}
