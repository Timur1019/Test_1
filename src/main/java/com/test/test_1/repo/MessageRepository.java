package com.test.test_1.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class MessageRepository {
    private final SessionFactory sessionFactory;

    public MessageRepository() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/minecraft");
        properties.setProperty("hibernate.connection.username", "username");
        properties.setProperty("hibernate.connection.password", "password");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(MessageEntity.class);

        this.sessionFactory = configuration.buildSessionFactory();
    }

    public void saveMessage(MessageEntity message) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(message);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}