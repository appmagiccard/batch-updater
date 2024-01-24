package com.magicauction.batchupdater.processor;

import com.magicauction.batchupdater.entity.MagicSet;
import com.magicauction.batchupdater.entity.ScryfallSetPojo;
import com.magicauction.batchupdater.entity.repository.SetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetDatabaseUpdater {

    private final SetRepository setRepository;

    @Autowired
    public SetDatabaseUpdater(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    public boolean update(List<ScryfallSetPojo> sets){
        List<String> allFromDb = setRepository.findAll().stream().map(MagicSet::getScryfallId).collect(Collectors.toList());
        List<MagicSet> sets1 = sets.stream().filter(s -> !allFromDb.contains(s.id())).map(Converter::toEntity).collect(Collectors.toList());
        setRepository.saveAll(sets1);
        return true;
    }
}
