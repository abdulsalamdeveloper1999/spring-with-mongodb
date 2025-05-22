package com.asdevify.springWithMongo.entities;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection   = "journal_config")
@Getter
@Setter
@NoArgsConstructor
public class JournalConfigEntity {

    String key;
    String value;
}
