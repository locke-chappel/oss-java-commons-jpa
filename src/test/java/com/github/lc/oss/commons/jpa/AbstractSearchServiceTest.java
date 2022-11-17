package com.github.lc.oss.commons.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.hibernate.query.criteria.internal.JoinImplementor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.lc.oss.commons.serialization.Jsonable;
import com.github.lc.oss.commons.serialization.PagedResult;
import com.github.lc.oss.commons.testing.AbstractMockTest;

public class AbstractSearchServiceTest extends AbstractMockTest {
    private static class TestService extends AbstractSearchService {

    }

    private EntityManager entityManager = Mockito.mock(EntityManager.class);
    private CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
    private AbstractSearchService service;

    @BeforeEach
    public void init() {
        this.service = new TestService();
        this.setField("entityManager", this.entityManager, this.service);
    }

    @Test
    public void test_getCriteriaBuilder() {
        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);

        CriteriaBuilder result = this.service.getCriteriaBuilder();
        Assertions.assertSame(this.criteriaBuilder, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_count_query_coverage() {
        CriteriaQuery<Jsonable> query = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> countQuery = Mockito.mock(CriteriaQuery.class);
        Root<Jsonable> root = Mockito.mock(Root.class);
        Root<Jsonable> countRoot = Mockito.mock(Root.class);
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);

        Join<Jsonable, ?> join = Mockito.mock(Join.class);
        JoinImplementor<Object, Object> countJoin = Mockito.mock(JoinImplementor.class);
        Attribute<? super Jsonable, ?> att = Mockito.mock(Attribute.class);
        Set<Join<Jsonable, ?>> joins = new HashSet<>();
        joins.add(join);

        JoinImplementor<Jsonable, ?> fetch = Mockito.mock(JoinImplementor.class);
        Set<Fetch<Jsonable, ?>> fetches = new HashSet<>();
        fetches.add(fetch);

        Join<Object, ?> fJoin = Mockito.mock(Join.class);
        Attribute<? super Jsonable, ?> fAtt = Mockito.mock(Attribute.class);
        Set<Join<Object, ?>> fetchJoins = new HashSet<>();
        fetchJoins.add(fJoin);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        Mockito.doAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Jsonable.class;
            }
        }).when(root).getJavaType();
        Mockito.when(countQuery.from(Jsonable.class)).thenReturn(countRoot);

        Mockito.when(root.getJoins()).thenReturn(joins);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(join).getAttribute();
        Mockito.when(countRoot.join(att.getName(), join.getJoinType())).thenReturn(countJoin);

        Mockito.when(root.getFetches()).thenReturn(fetches);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(fetch).getAttribute();
        Mockito.when(countRoot.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);
        Mockito.doAnswer(new Answer<Set<Join<Object, ?>>>() {
            @Override
            public Set<Join<Object, ?>> answer(InvocationOnMock invocation) throws Throwable {
                return fetchJoins;
            }
        }).when(fetch).getJoins();
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return fAtt;
            }
        }).when(fJoin).getAttribute();
        Mockito.when(countJoin.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);

        Mockito.when(query.isDistinct()).thenReturn(false);
        Mockito.when(countQuery.distinct(false)).thenReturn(countQuery);
        Mockito.when(this.entityManager.createQuery(countQuery)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(Long.valueOf(0));
        Mockito.when(query.getRestriction()).thenReturn(Mockito.mock(Predicate.class));

        long result = this.service.count(query, root);
        Assertions.assertEquals(0, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_count_query_coverage_noWhere() {
        CriteriaQuery<Jsonable> query = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> countQuery = Mockito.mock(CriteriaQuery.class);
        Root<Jsonable> root = Mockito.mock(Root.class);
        Root<Jsonable> countRoot = Mockito.mock(Root.class);
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);

        Join<Jsonable, ?> join = Mockito.mock(Join.class);
        JoinImplementor<Object, Object> countJoin = Mockito.mock(JoinImplementor.class);
        Attribute<? super Jsonable, ?> att = Mockito.mock(Attribute.class);
        Set<Join<Jsonable, ?>> joins = new HashSet<>();
        joins.add(join);

        JoinImplementor<Jsonable, ?> fetch = Mockito.mock(JoinImplementor.class);
        Set<Fetch<Jsonable, ?>> fetches = new HashSet<>();
        fetches.add(fetch);

        Join<Object, ?> fJoin = Mockito.mock(Join.class);
        Attribute<? super Jsonable, ?> fAtt = Mockito.mock(Attribute.class);
        Set<Join<Object, ?>> fetchJoins = new HashSet<>();
        fetchJoins.add(fJoin);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        Mockito.doAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Jsonable.class;
            }
        }).when(root).getJavaType();
        Mockito.when(countQuery.from(Jsonable.class)).thenReturn(countRoot);

        Mockito.when(root.getJoins()).thenReturn(joins);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(join).getAttribute();
        Mockito.when(countRoot.join(att.getName(), join.getJoinType())).thenReturn(countJoin);

        Mockito.when(root.getFetches()).thenReturn(fetches);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(fetch).getAttribute();
        Mockito.when(countRoot.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);
        Mockito.doAnswer(new Answer<Set<Join<Object, ?>>>() {
            @Override
            public Set<Join<Object, ?>> answer(InvocationOnMock invocation) throws Throwable {
                return fetchJoins;
            }
        }).when(fetch).getJoins();
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return fAtt;
            }
        }).when(fJoin).getAttribute();
        Mockito.when(countJoin.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);

        Mockito.when(query.isDistinct()).thenReturn(false);
        Mockito.when(countQuery.distinct(false)).thenReturn(countQuery);
        Mockito.when(this.entityManager.createQuery(countQuery)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(Long.valueOf(0));

        long result = this.service.count(query, root);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void test_orderBy_empty() {
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);

        Collection<Order> orders = new ArrayList<>();

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertEquals(0, invocation.getArguments().length);
                return null;
            }
        }).when(query).orderBy((Order[]) ArgumentMatchers.any());

        this.service.orderBy(query, orders);
    }

    @Test
    public void test_orderBy() {
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);

        Collection<Order> orders = new ArrayList<>();
        orders.add(Mockito.mock(Order.class));
        orders.add(Mockito.mock(Order.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertEquals(2, invocation.getArguments().length);
                return null;
            }
        }).when(query).orderBy((Order[]) ArgumentMatchers.any());

        this.service.orderBy(query, orders);
    }

    @Test
    public void test_orderBy_create_dsc() {
        Path<?> path = Mockito.mock(Path.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.desc(path)).thenReturn(order);

        Order result = this.service.orderBy(path, SortDirection.Desc);
        Assertions.assertSame(order, result);
    }

    @Test
    public void test_orderBy_create_asc() {
        Path<?> path = Mockito.mock(Path.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.asc(path)).thenReturn(order);

        Order result = this.service.orderBy(path, SortDirection.Asc);
        Assertions.assertSame(order, result);
    }

    @Test
    public void test_iOrderBy_create_dsc() {
        @SuppressWarnings("unchecked")
        Path<String> path = Mockito.mock(Path.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(path);
        Mockito.when(this.criteriaBuilder.desc(path)).thenReturn(order);

        Order result = this.service.iOrderBy(path, SortDirection.Desc);
        Assertions.assertSame(order, result);
    }

    @Test
    public void test_iOrderBy_create_asc() {
        @SuppressWarnings("unchecked")
        Path<String> path = Mockito.mock(Path.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(path);
        Mockito.when(this.criteriaBuilder.asc(path)).thenReturn(order);

        Order result = this.service.iOrderBy(path, SortDirection.Asc);
        Assertions.assertSame(order, result);
    }

    @Test
    public void test_where_empty() {
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);

        Collection<Predicate> wheres = new ArrayList<>();

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertEquals(0, invocation.getArguments().length);
                return null;
            }
        }).when(query).where((Predicate[]) ArgumentMatchers.any());

        this.service.where(query, wheres);
    }

    @Test
    public void test_where() {
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);

        Collection<Predicate> wheres = new ArrayList<>();
        wheres.add(Mockito.mock(Predicate.class));
        wheres.add(Mockito.mock(Predicate.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertEquals(2, invocation.getArguments().length);
                return null;
            }
        }).when(query).where((Predicate[]) ArgumentMatchers.any());

        this.service.where(query, wheres);
    }

    @Test
    public void test_escape() {
        String result = this.service.escape(" _%\\*\t");
        Assertions.assertEquals(" \\_\\%\\\\*\t", result);
    }

    @Test
    public void test_escape_mapWildcards() {
        String result = this.service.escape(" _%\\*\t", true);
        Assertions.assertEquals(" \\_\\%\\\\%\t", result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_iStartsWith() {
        Path<String> path = Mockito.mock(Path.class);
        Path<String> pathLower = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(pathLower);
        Mockito.when(this.criteriaBuilder.like(pathLower, "a*b%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.iStartsWith(path, "A*B");
        Assertions.assertSame(predicate, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_iStartsWith_withWildcards() {
        Path<String> path = Mockito.mock(Path.class);
        Path<String> pathLower = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(pathLower);
        Mockito.when(this.criteriaBuilder.like(pathLower, "a%b%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.iStartsWithWild(path, "A*B");
        Assertions.assertSame(predicate, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_iLike() {
        Path<String> path = Mockito.mock(Path.class);
        Path<String> pathLower = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(pathLower);
        Mockito.when(this.criteriaBuilder.like(pathLower, "%a\\_*b%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.iLike(path, "A_*B");
        Assertions.assertSame(predicate, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_iLike_withWildcards() {
        Path<String> path = Mockito.mock(Path.class);
        Path<String> pathLower = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(pathLower);
        Mockito.when(this.criteriaBuilder.like(pathLower, "%a\\_%b%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.iLikeWild(path, "A_*B");
        Assertions.assertSame(predicate, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_like_startsWith_caseSensitive() {
        Path<String> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.like(path, "A\\_*B%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.like(path, "A_*B", true, false, false);
        Assertions.assertSame(predicate, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_like_caseSensitive_withWildcards() {
        Path<String> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.like(path, "%A\\_%B%", this.service.getSqlEscapeChar())).thenReturn(predicate);

        Predicate result = this.service.like(path, "A_*B", true, true, true);
        Assertions.assertSame(predicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_getPage() {
        CriteriaQuery<Jsonable> query = Mockito.mock(CriteriaQuery.class);
        TypedQuery<Jsonable> typedQuery = Mockito.mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = Mockito.mock(CriteriaQuery.class);
        Root<Jsonable> root = Mockito.mock(Root.class);
        Root<Jsonable> countRoot = Mockito.mock(Root.class);
        TypedQuery<Long> countTypedQuery = Mockito.mock(TypedQuery.class);

        Join<Jsonable, ?> join = Mockito.mock(Join.class);
        JoinImplementor<Object, Object> countJoin = Mockito.mock(JoinImplementor.class);
        Attribute<? super Jsonable, ?> att = Mockito.mock(Attribute.class);
        Set<Join<Jsonable, ?>> joins = new HashSet<>();
        joins.add(join);

        JoinImplementor<Jsonable, ?> fetch = Mockito.mock(JoinImplementor.class);
        Set<Fetch<Jsonable, ?>> fetches = new HashSet<>();
        fetches.add(fetch);

        Join<Object, ?> fJoin = Mockito.mock(Join.class);
        Attribute<? super Jsonable, ?> fAtt = Mockito.mock(Attribute.class);
        Set<Join<Object, ?>> fetchJoins = new HashSet<>();
        fetchJoins.add(fJoin);

        Mockito.when(this.entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        Mockito.doAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Jsonable.class;
            }
        }).when(root).getJavaType();
        Mockito.when(countQuery.from(Jsonable.class)).thenReturn(countRoot);

        Mockito.when(root.getJoins()).thenReturn(joins);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(join).getAttribute();
        Mockito.when(countRoot.join(att.getName(), join.getJoinType())).thenReturn(countJoin);

        Mockito.when(root.getFetches()).thenReturn(fetches);
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return att;
            }
        }).when(fetch).getAttribute();
        Mockito.when(countRoot.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);
        Mockito.doAnswer(new Answer<Set<Join<Object, ?>>>() {
            @Override
            public Set<Join<Object, ?>> answer(InvocationOnMock invocation) throws Throwable {
                return fetchJoins;
            }
        }).when(fetch).getJoins();
        Mockito.doAnswer(new Answer<Attribute<?, ?>>() {
            @Override
            public Attribute<? super Jsonable, ?> answer(InvocationOnMock invocation) throws Throwable {
                return fAtt;
            }
        }).when(fJoin).getAttribute();
        Mockito.when(countJoin.join(att.getName(), fetch.getJoinType())).thenReturn(countJoin);

        Mockito.when(query.isDistinct()).thenReturn(false);
        Mockito.when(countQuery.distinct(false)).thenReturn(countQuery);
        Mockito.when(this.entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        Mockito.when(countTypedQuery.getSingleResult()).thenReturn(Long.valueOf(77));

        PagedResult<Jsonable> result = this.service.getPage(query, root, 7, 3);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(77, result.getTotal());
        Assertions.assertNotNull(result.getData());
        Assertions.assertTrue(result.getData().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_iEquals() {
        Path<String> path = Mockito.mock(Path.class);
        Path<String> pathLower = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(this.entityManager.getCriteriaBuilder()).thenReturn(this.criteriaBuilder);
        Mockito.when(this.criteriaBuilder.lower(path)).thenReturn(pathLower);
        Mockito.when(this.criteriaBuilder.equal(pathLower, "a_b")).thenReturn(predicate);

        Predicate result = this.service.iEquals(path, "A_B");
        Assertions.assertSame(predicate, result);
    }
}
