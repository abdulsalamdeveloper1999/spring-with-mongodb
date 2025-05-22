package com.asdevify.springWithMongo.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asdevify.springWithMongo.entities.JournalConfigEntity;
import com.asdevify.springWithMongo.repositories.JournalConfigRepo;

import jakarta.annotation.PostConstruct;

@Component
public class AppCache {

  public  Map<String, String> APP_CACHE = new HashMap<>();

 @Autowired
  private JournalConfigRepo configRepo;




    @PostConstruct
    public void init() {
        List<JournalConfigEntity> all = configRepo.findAll();

        for (JournalConfigEntity configEntity : all) {
            APP_CACHE.put(configEntity.getKey(), configEntity.getValue());
        }
    }

}
