package com.actionzh.service.impl;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class SqlQueryProvider {

    private String selectClause;
    private String fromClause;
    private String whereClause;
    private String sortClause;
    private String limitClause;

    @Builder.Default
    private Map<String, Object> parameterValues = new HashMap<>();

    public String toQuerySql() {
        return selectClause + fromClause + whereClause + sortClause + limitClause;
    }

    public String toCountSql() {
        return "SELECT count(1) FROM (" + selectClause + fromClause + whereClause + ") AS TEMP";
    }
}
