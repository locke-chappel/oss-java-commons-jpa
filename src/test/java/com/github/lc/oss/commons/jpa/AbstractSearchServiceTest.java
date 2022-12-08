package com.github.lc.oss.commons.jpa;

import java.util.ArrayList;
import java.util.Collection;

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

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;

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

    @SuppressWarnings("unchecked")
    @Test
    public void test_getPage() {
        AbstractQueryBuilder<?> builder = new AbstractQueryBuilder<Jsonable>() {
            @Override
            protected <T> QueryInfo build(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder, boolean forCount) {
                QueryInfo info = new QueryInfo();
                info.setQuery(Mockito.mock(CriteriaQuery.class));
                info.setSelection(Mockito.mock(Selection.class));
                return info;
            }
        };

        TypedQuery<Object> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(this.entityManager.createQuery(ArgumentMatchers.any(CriteriaQuery.class))).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(Long.valueOf(100));

        SearchCriteria criteria = new SearchCriteria(10, 0, new ArrayList<>());

        PagedResult<?> result = this.service.getPage(builder, criteria);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(100l, result.getTotal());
    }
}
