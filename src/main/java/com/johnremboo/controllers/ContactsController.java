package com.johnremboo.controllers;

import com.johnremboo.services.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@RestController
public class ContactsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactsController.class);

    @Autowired
    ContactsService contactService;

    @RequestMapping(value="/contacts",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity findByNameRegex(@RequestParam("nameFilter") String filter) {

        try {
            Pattern.compile(filter);
        } catch (PatternSyntaxException e) {
            logger.info("Invalid regex: " + filter);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid regex: " + filter);
        }

        List contacts = contactService.findByNameRegex(filter);

        if(contacts != null && contacts.size() > 0) {
            logger.info(contacts.size() + " contacts are found with " + filter + " regex in the database!");
            return ResponseEntity.status(HttpStatus.OK).body(contacts);
        }

        logger.info("No contacts are found with " + filter + " regex");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
