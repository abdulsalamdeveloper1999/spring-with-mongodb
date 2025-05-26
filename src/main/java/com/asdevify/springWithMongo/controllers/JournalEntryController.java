package com.asdevify.springWithMongo.controllers;

import java.util.List;
import java.util.Optional;

import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdevify.springWithMongo.entities.JournalEntryEntity;
import com.asdevify.springWithMongo.services.JournalEntryService;
import com.asdevify.springWithMongo.services.UserDetailsImpl;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);
    @Autowired
    private JournalEntryService jEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("health-check")
    private String healthCheck() {
        return "ok";
    }

    @PostMapping("create-entry")
    private ResponseEntity<?> createEntry(@RequestBody JournalEntryEntity jEntity, HttpServletRequest request) {
     

        JournalEntryEntity entry = jEntryService.saveEntry(jEntity);
        if (entry == null) {
            logger.error("Failed to save journal entry for user: {}");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            logger.info("Journal entry saved successfully for user: {}");
            return new ResponseEntity<>(entry, HttpStatus.OK);
        }

    }

    // @GetMapping
    // private List<JournalEntryEntity> getEntries() {
    // return jEntryService.getAll();`Ï
    // }

    
    @GetMapping("get-entries")
    @Operation(summary = "Get all user journals")
    public ResponseEntity<?> getEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
        List<JournalEntryEntity> allEntries = user.getJournalEntryEntities();
        if (allEntries == null || allEntries.isEmpty()) {
            return new ResponseEntity<>("entries not found", HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ObjectId id) {

        try {
            boolean isDeleted = jEntryService.deleteById(id);
            if (isDeleted) {
                return ResponseEntity.ok("Deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   @PutMapping("/{id}")
private ResponseEntity<?> update(@PathVariable ObjectId id,
                                 @RequestBody JournalEntryEntity entry) {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        System.out.println(user.getJournalEntryEntities());

        boolean ownsEntry = user.getJournalEntryEntities()
                .stream()
                .anyMatch(e -> e.getId().equals(id));

        if (!ownsEntry) {
            return new ResponseEntity<>("Journal entry not found for this user", HttpStatus.NOT_FOUND);
        }

        JournalEntryEntity updatedEntry = jEntryService.updateEntry(id, entry);
        return ResponseEntity.ok(updatedEntry);

    } catch (Exception e) {
        return new ResponseEntity<>("Error updating entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @GetMapping("find-entry/{id}")  
    public ResponseEntity<?> findEntity(@PathVariable("id") String entryId) throws Exception{
      try {
          ObjectId id=new ObjectId(entryId);
        JournalEntryEntity entry = jEntryService.findEntry(id);
        if (entry==null) {
             return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
        }

         return new ResponseEntity<>(entry,HttpStatus.OK);
      } catch (Exception e) {
             return new ResponseEntity<>("Error: "+e.getMessage(),HttpStatus.NOT_FOUND);
      }
    }
 


}
