package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.CardPojo;

import java.util.List;

public interface IDatabaseUpdater {
    public boolean update(List<CardPojo> cardPojoList);
}
