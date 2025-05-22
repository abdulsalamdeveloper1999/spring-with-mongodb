package com.asdevify.springWithMongo.repositories;



import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.asdevify.springWithMongo.entities.JournalEntryEntity;
@Repository
public interface JournalEntryRepo extends MongoRepository<JournalEntryEntity,ObjectId>{
    
}
