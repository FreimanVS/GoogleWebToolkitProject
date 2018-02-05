package com.mySampleApplication.shared.factory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAFactory {
    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    private static EntityManagerFactory emf;
    public static EntityManagerFactory get() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }
    public static void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
