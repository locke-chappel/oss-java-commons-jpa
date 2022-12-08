package com.github.lc.oss.commons.jpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.lc.oss.commons.serialization.Jsonable;
import com.github.lc.oss.commons.serialization.PagedResult;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;

public abstract class AbstractSearchService {
    private static final char SQL_ESCAPE_CHAR = '\\';
    private static final List<String> SQL_TO_ESCAPE = Collections.unmodifiableList(Arrays.asList( //
            "\\", //
            "_", //
            "%" //
    ));
    private static final Map<String, String> SQL_USER_WILDCARDS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("*", "%");
        SQL_USER_WILDCARDS = Collections.unmodifiableMap(map);
    }

    protected static boolean ALLOW_WILDCARDS = true;
    protected static boolean BLOCK_WILDCARDS = false;
    protected static boolean CASE_SENSITIVE = true;
    protected static boolean CASE_INSENSITIVE = false;

    @PersistenceContext
    private EntityManager entityManager;

    protected <Type extends Jsonable> PagedResult<Type> getPage(AbstractQueryBuilder<Type> queryBuilder, SearchCriteria searchCriteria) {
        CriteriaBuilder cb = this.getCriteriaBuilder();
        CriteriaQuery<Type> dataQuery = queryBuilder.forData(searchCriteria, cb);
        CriteriaQuery<Long> countQuery = queryBuilder.forCount(searchCriteria, cb);

        TypedQuery<Type> typedDataQuery = this.getEntityManager().createQuery(dataQuery);
        typedDataQuery.setFirstResult(searchCriteria.getPageSize() * searchCriteria.getPageNumber());
        typedDataQuery.setMaxResults(searchCriteria.getPageSize());
        List<Type> result = typedDataQuery.getResultList();

        TypedQuery<Long> typedCountQuery = this.getEntityManager().createQuery(countQuery);
        long total = typedCountQuery.getSingleResult().longValue();

        PagedResult<Type> results = new PagedResult<>();
        results.setData(result);
        results.setTotal(total);
        return results;
    }

    protected Predicate iEquals(Expression<String> path, String value) {
        CriteriaBuilder cb = this.getCriteriaBuilder();
        Expression<String> what = cb.lower(path);
        String find = value.toLowerCase();
        return cb.equal(what, find);
    }

    protected Predicate iStartsWith(Expression<String> path, String value) {
        return this.like(path, value, AbstractSearchService.CASE_INSENSITIVE, AbstractSearchService.BLOCK_WILDCARDS, false);
    }

    protected Predicate iStartsWithWild(Expression<String> path, String value) {
        return this.like(path, value, AbstractSearchService.CASE_INSENSITIVE, AbstractSearchService.ALLOW_WILDCARDS, false);
    }

    protected Predicate iLike(Expression<String> path, String value) {
        return this.like(path, value, AbstractSearchService.CASE_INSENSITIVE, AbstractSearchService.BLOCK_WILDCARDS, true);
    }

    protected Predicate iLikeWild(Expression<String> path, String value) {
        return this.like(path, value, AbstractSearchService.CASE_INSENSITIVE, AbstractSearchService.ALLOW_WILDCARDS, true);
    }

    protected Predicate like(Expression<String> path, String value, boolean caseSensitive, boolean allowWildcard, boolean contains) {
        CriteriaBuilder cb = this.getCriteriaBuilder();
        Expression<String> what = caseSensitive ? path : cb.lower(path);
        String find = caseSensitive ? value : value.toLowerCase();
        find = this.escape(find, allowWildcard);
        if (contains) {
            find = "%" + find;
        }
        find += "%";
        return cb.like(what, find, this.getSqlEscapeChar());
    }

    protected <Type> Order orderBy(Expression<Type> path, SortDirection sort) {
        return sort == SortDirection.Desc ? this.getCriteriaBuilder().desc(path) : this.getCriteriaBuilder().asc(path);
    }

    protected Order iOrderBy(Expression<String> path, SortDirection sort) {
        CriteriaBuilder cb = this.getCriteriaBuilder();
        return sort == SortDirection.Desc ? cb.desc(cb.lower(path)) : cb.asc(cb.lower(path));
    }

    protected <Type> void orderBy(CriteriaQuery<Type> query, Collection<Order> orders) {
        query.orderBy(orders.toArray(new Order[orders.size()]));
    }

    protected <Type> void where(CriteriaQuery<Type> query, Collection<Predicate> wheres) {
        query.where(wheres.toArray(new Predicate[wheres.size()]));
    }

    protected String escape(String str) {
        return this.escape(str, false);
    }

    protected String escape(String str, boolean mapWildcards) {
        String s = str;
        char esc = this.getSqlEscapeChar();
        List<String> toEscape = this.getToEscape();
        for (String find : toEscape) {
            s = s.replace(find, esc + find);
        }

        if (mapWildcards) {
            Map<String, String> wildCards = this.getUserWildcards();
            for (Entry<String, String> entry : wildCards.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
        }
        return s;
    }

    protected char getSqlEscapeChar() {
        return AbstractSearchService.SQL_ESCAPE_CHAR;
    }

    protected List<String> getToEscape() {
        return AbstractSearchService.SQL_TO_ESCAPE;
    }

    protected Map<String, String> getUserWildcards() {
        return AbstractSearchService.SQL_USER_WILDCARDS;
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return this.getEntityManager().getCriteriaBuilder();
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
