package com.johnremboo.repositories;

import com.johnremboo.entities.Contact;
import com.johnremboo.utils.HibernateSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactsRepository {

    @Autowired
    HibernateSession session;

    public List findByNameRegex(String filter) {
        try (Session currentSession = session.getSession()) {
            Criteria criteria = currentSession.createCriteria(Contact.class);
            criteria.add(Restrictions.sqlRestriction("name ~ '^(?!" + filter + ")'"));
            return criteria.list();
        }
    }

    public void addContact(Contact contact) {
        try (Session currentSession = session.getSession()) {
            currentSession.save(contact);
        }
    }


}

