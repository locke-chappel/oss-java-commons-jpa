package com.github.lc.oss.commons.jpa;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TermTest {
    private enum TestTerms implements SearchTerm {
        Term("termProp", true, true),
        Sortable("sortable", false, true),
        Queryable("queryable", true, false),
        Basic("something", false, false);

        private final String property;
        private final boolean queryable;
        private final boolean sortable;

        private TestTerms(String property, boolean queryable, boolean sortable) {
            this.property = property;
            this.queryable = queryable;
            this.sortable = sortable;
        }

        @Override
        public String getProperty() {
            return this.property;
        }

        @Override
        public boolean isQueryable() {
            return this.queryable;
        }

        @Override
        public boolean isSortable() {
            return this.sortable;
        }
    }

    @Test
    public void test_constructor() {
        Term term = new Term("f", "v");
        Assertions.assertEquals("f", term.getProperty());
        Assertions.assertEquals("v", term.getValue());
        Assertions.assertNull(term.getSort());
    }

    @Test
    public void test_constructor_v2() {
        Term term = new Term("f", 100, SortDirection.Asc);
        Assertions.assertEquals("f", term.getProperty());
        Assertions.assertEquals(100, term.getValue());
        Assertions.assertSame(SortDirection.Asc, term.getSort());
    }

    @Test
    public void test_of_full() {
        Term term = Term.of(TestTerms.Term, "val", SortDirection.Asc);
        Assertions.assertNotNull(term);
        Assertions.assertEquals("val", term.getValue());
        Assertions.assertEquals("termProp", term.getProperty());
        Assertions.assertSame(SortDirection.Asc, term.getSort());
    }

    @Test
    public void test_of_full_v2() {
        Term term = Term.of(TestTerms.Term, true);
        Assertions.assertNotNull(term);
        Assertions.assertEquals(true, term.getValue());
        Assertions.assertEquals("termProp", term.getProperty());
        Assertions.assertNull(term.getSort());
    }

    @Test
    public void test_of_full_v3() {
        Term term = Term.of(TestTerms.Term, true, "dEsC");
        Assertions.assertNotNull(term);
        Assertions.assertEquals(true, term.getValue());
        Assertions.assertEquals("termProp", term.getProperty());
        Assertions.assertEquals(SortDirection.Desc, term.getSort());
    }

    @Test
    public void test_of_basic() {
        Term term = Term.of(TestTerms.Basic, "val", SortDirection.Asc);
        Assertions.assertNull(term);
    }

    @Test
    public void test_of_sortableOnly() {
        Term term = Term.of(TestTerms.Sortable, "val", SortDirection.Desc);
        Assertions.assertNotNull(term);
        Assertions.assertNull(term.getValue());
        Assertions.assertEquals("sortable", term.getProperty());
        Assertions.assertSame(SortDirection.Desc, term.getSort());
    }

    @Test
    public void test_of_queryableOnly() {
        Term term = Term.of(TestTerms.Queryable, 100, SortDirection.Desc);
        Assertions.assertNotNull(term);
        Assertions.assertEquals(100, term.getValue());
        Assertions.assertEquals("queryable", term.getProperty());
        Assertions.assertNull(term.getSort());
    }

    @Test
    public void test_of_list() {
        List<Term> result = Term.of( //
                Term.of(TestTerms.Term, true), //
                Term.of(TestTerms.Basic, 100, SortDirection.Asc), //
                Term.of(TestTerms.Queryable, 100, SortDirection.Desc));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Term t1 = result.get(0);
        Assertions.assertEquals(TestTerms.Term.getProperty(), t1.getProperty());
        Assertions.assertEquals(true, t1.getValue());
        Assertions.assertNull(t1.getSort());

        Term t2 = result.get(1);
        Assertions.assertEquals(TestTerms.Queryable.getProperty(), t2.getProperty());
        Assertions.assertEquals(100, t2.getValue());
        Assertions.assertNull(t2.getSort());
    }
}
