package com.actionzh.service.impl;

import org.springframework.data.domain.Pageable;

/**
 * Created by fy on 06/05/2017.
 */
public interface ContactRepositoryCustom {


    SqlQueryProvider getQueryProvider(Pageable pageable, Long groupId, String searchTerm, Integer anonymous);

}