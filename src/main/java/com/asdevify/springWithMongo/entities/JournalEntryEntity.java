package com.asdevify.springWithMongo.entities;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntryEntity {

    @Id
    private ObjectId id;

    private String title;

    private String content;

    private LocalDate date;

    private ObjectId userId;




}
