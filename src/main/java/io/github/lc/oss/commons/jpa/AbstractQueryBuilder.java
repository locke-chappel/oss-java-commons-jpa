package io.github.lc.oss.commons.jpa;

import java.util.ArrayList;

import io.github.lc.oss.commons.serialization.Jsonable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public abstract class AbstractQueryBuilder<Type extends Jsonable> {
    public CriteriaQuery<Type> forData(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        QueryInfo info = this.build(searchCriteria, criteriaBuilder, false);
        CriteriaQuery<Type> query = info.getQuery();
        query.select(info.getSelection());
        return query;
    }

    public CriteriaQuery<Long> forCount(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        QueryInfo info = this.build(searchCriteria, criteriaBuilder, true);
        CriteriaQuery<Long> query = info.getQuery();
        query.orderBy(new ArrayList<>());
        query.select(info.getSelection());
        return query;
    }

    protected abstract <T> QueryInfo build(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder, boolean forCount);
}
