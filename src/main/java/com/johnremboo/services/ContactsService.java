package com.johnremboo.services;

import com.johnremboo.repositories.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsService {

    @Autowired
    ContactsRepository contactsRepository;

    public List findByNameRegex(String regex) {
        return contactsRepository.findByNameRegex(regex);
    }
}
