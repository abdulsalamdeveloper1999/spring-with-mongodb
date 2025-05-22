package com.asdevify.springWithMongo.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.asdevify.springWithMongo.entities.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.asdevify.springWithMongo.entities.JournalEntryEntity;
import com.asdevify.springWithMongo.repositories.JournalEntryRepo;
import com.asdevify.springWithMongo.repositories.UserRepo;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo jEntryRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public JournalEntryEntity saveEntry(JournalEntryEntity jEntity) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = auth.getName();

            UserEntity user = userService.findByUsername(username);

            if (!user.getUsername().equals(username)) {
                throw new IllegalAccessException("You are not authorized to delete this user's data");
            }
            jEntity.setDate(LocalDate.now());
            JournalEntryEntity entry = jEntryRepo.save(jEntity);
            user.getJournalEntryEntities().add(entry);
            userRepo.save(user);
            return entry;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<JournalEntryEntity> getAllUserEntries(ObjectId userId) {
        UserEntity user = userService.findById(userId);
        if (user != null) {
            jEntryRepo.findAllById(Collections.singleton(user.getId()));
        }
        return jEntryRepo.findAll();
    }

    public Optional<JournalEntryEntity> findEntry(ObjectId id) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new IllegalAccessException("User not found");
        }
        return jEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id) throws IllegalAccessException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        UserEntity user = userService.findByUsername(name);

        if (!user.getUsername().equals(name)) {
            throw new IllegalAccessException("You are not authorized to delete this user's data");
        }

        boolean isRemoved = user.getJournalEntryEntities()
                .removeIf(entry -> entry.getId().equals(id));

        if (isRemoved) {
            jEntryRepo.deleteById(id);
            userRepo.save(user);
            return true;
        }

        return false;
    }

    public JournalEntryEntity updateEntry(ObjectId id, JournalEntryEntity newEntry) {
    return jEntryRepo.findById(id).map(oldEntry -> {
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            oldEntry.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            oldEntry.setContent(newEntry.getContent());
        }
        return jEntryRepo.save(oldEntry);
    }).orElse(null); // or throw an exception if entry not found
}


}
