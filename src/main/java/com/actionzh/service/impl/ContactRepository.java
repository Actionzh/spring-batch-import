package com.actionzh.service.impl;

import com.actionzh.batch.dto.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(transactionManager = "multiTenantTransactionManager", rollbackFor = Exception.class, readOnly = true)
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {

    @Query("select contact from Contact contact where contact.id in ?1")
    List<Contact> findAllByIdIn(List<Long> ids);

}