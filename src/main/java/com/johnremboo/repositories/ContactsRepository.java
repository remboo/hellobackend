package com.johnremboo.repositories;

import com.johnremboo.entities.Contact;
import com.johnremboo.utils.HibernateSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactsRepository {

    @Autowired
    HibernateSession session;

    public List findByNameRegex(String filter) {
        try (Session currentSession = session.getSession()) {
            Query query = currentSession.createQuery("from Contact c where regexp(c.name, \'" + filter + "\') = false");
            return query.list();
        }
    }

    public void addContact(Contact contact) {
        try (Session currentSession = session.getSession()) {
            currentSession.save(contact);
        }
    }
}

