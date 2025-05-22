package com.asdevify.springWithMongo.repositories;

import com.asdevify.springWithMongo.entities.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends MongoRepository<UserEntity, ObjectId> {


    UserEntity findByUsername(String username);
}
