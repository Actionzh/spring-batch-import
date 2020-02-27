package com.actionzh.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

/**
 * Created by fdlessard on 17-03-02.
 */
@Import(JpaAutoConfiguration.class)
public class JpaTenantAwareConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaTenantAwareConfiguration.class);


    /**
     * replaced by SimpleBatchConfiguration.transactionManager
     *
     * @param entityManagerFactory
     * @return
     */
    @Bean("multiTenantTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        LOGGER.info("PersistenceConfiguration.transactionManager()");
        MultiTenantJpaTransactionManager transactionManager = new MultiTenantJpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
