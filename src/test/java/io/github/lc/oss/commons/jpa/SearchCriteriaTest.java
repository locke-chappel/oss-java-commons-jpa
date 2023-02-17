package io.github.lc.oss.commons.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchCriteriaTest {
    @Test
    public void test_constructor() {
        SearchCriteria sc = new SearchCriteria(7, 3, (Term) null);

        Assertions.assertEquals(7, sc.getPageSize());
        Assertions.assertEquals(3, sc.getPageNumber());
        Assertions.assertNotNull(sc.getSearchTerms());
        Assertions.assertTrue(sc.getSearchTerms().isEmpty());
    }
}
