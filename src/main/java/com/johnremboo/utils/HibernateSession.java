package com.johnremboo.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class HibernateSession {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateSession(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null)
            throw new NullPointerException("Class is not hibernate factory");

        sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }
}