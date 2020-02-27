package com.actionzh.config;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;

public class MultiTenantJpaTransactionManager extends JpaTransactionManager {

    public MultiTenantJpaTransactionManager() {
        logger.info("MultiTenantJpaTransactionManager - Creation");
    }

    @Override
    public void doBegin(Object transaction, final TransactionDefinition definition) {
        logger.debug("MultiTenantJpaTransactionManager.doBegin()");
        super.doBegin(transaction, definition);
        EntityManagerHolder entityManagerHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(getEntityManagerFactory());
        EntityManager entityManager = entityManagerHolder.getEntityManager();
    }
}
