package com.asdevify.springWithMongo.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.asdevify.springWithMongo.entities.JournalConfigEntity;

@Repository
public interface JournalConfigRepo extends MongoRepository<JournalConfigEntity,ObjectId> {
    
}
