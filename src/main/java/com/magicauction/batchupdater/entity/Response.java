package com.magicauction.batchupdater.entity;

import java.util.ArrayList;

public class Response<T> {
    private ArrayList<T> data;
    private boolean hasMore;
    private String object;

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                ", hasMore=" + hasMore +
                ", object='" + object + '\'' +
                '}';
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
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

    public Response(ArrayList<T> data, boolean hasMore, String object) {
        this.data = data;
        this.hasMore = hasMore;
        this.object = object;
    }
}
