package com.zillious.corporate_website.portal.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityMg {
    private static final EntityManagerFactory emFactory;
    static {
        emFactory = Persistence.createEntityManagerFactory("com.zillious.corporate_website.portal.dao.impl");
    }

    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public static void close() {
        emFactory.close();
    }
}
