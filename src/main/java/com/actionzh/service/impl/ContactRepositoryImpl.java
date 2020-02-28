package com.actionzh.service.impl;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Transactional(transactionManager = "multiTenantTransactionManager", rollbackFor = Exception.class, readOnly = true)
public class ContactRepositoryImpl implements ContactRepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactRepositoryImpl.class);

    private static final String condSearch = " and (UPPER(c.name) like UPPER(:search) or UPPER(c.email) like UPPER(:search) or UPPER(c.mobile_phone) like UPPER(:search) or UPPER(c.company) like UPPER(:search) or UPPER(c.nickname) like UPPER(:search))";

    @Override
    public SqlQueryProvider getQueryProvider(Pageable pageable, Long groupId, String searchTerm, Integer anonymous) {
        StringBuilder selectSb = new StringBuilder("SELECT c.* ");

        StringBuilder fromSb = new StringBuilder();
        fromSb.append(" FROM contact c ");
        StringBuilder whereSb = new StringBuilder(" WHERE c.merged_id is null ");

        if (anonymous == 1 || anonymous == 0) {
            whereSb.append(" AND c.is_anonymous = ").append(anonymous).append(" ");
        }

        if (StringUtils.isNotBlank(searchTerm)) {
            whereSb.append(condSearch);
        }

        StringBuilder sortSb = buildSortClaus(pageable);

        StringBuilder limitSb = new StringBuilder();
        if (pageable != null) {
            limitSb.append(buildLimitClause(pageable.getOffset(), pageable.getPageSize()));
        }

        Map<String, Object> parameterValues = new HashMap<>();
        if (groupId != null) {
            parameterValues.put("groupId", groupId);
        }
        if (StringUtils.isNotBlank(searchTerm)) {
            parameterValues.put("search", "%" + searchTerm + "%");
        }

        return SqlQueryProvider.builder()
                .selectClause(selectSb.toString())
                .fromClause(fromSb.toString())
                .whereClause(whereSb.toString())
                .sortClause(sortSb.toString())
                .limitClause(limitSb.toString())
                .parameterValues(parameterValues)
                .build();
    }

    private String buildLimitClause(long offset, int pageSize) {
        return " LIMIT " + offset + "," + pageSize;
    }

    private StringBuilder buildSortClaus(Pageable pageable) {
        String sortField = "last_updated";
        String direction = "DESC";
        Sort.Order order = getOrder(pageable);
        if (order != null) {
            sortField = order.getProperty();
            direction = order.getDirection().name();
        }
        sortField = underScore(sortField);
        StringBuilder sortSb = new StringBuilder(" ORDER BY c.").append(sortField).append(" ").append(direction).append(" ");
        return sortSb;
    }

    private Sort.Order getOrder(Pageable pageable) {
        Sort.Order order = null;
        if (pageable != null && pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        return order;
    }

    private String underScore(String string) {
        if (StringUtils.isBlank(string)) {
            return string;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }


}
