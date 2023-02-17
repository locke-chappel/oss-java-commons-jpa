package io.github.lc.oss.commons.jpa;

import java.util.Collections;
import java.util.List;

public class SearchCriteria {
    private final int pageSize;
    private final int pageNumber;
    private final List<Term> searchTerms;

    public SearchCriteria(int pageSize, int pageNumber, Term... terms) {
        this(pageSize, pageNumber, Term.of(terms));
    }

    public SearchCriteria(int pageSize, int pageNumber, List<Term> terms) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.searchTerms = Collections.unmodifiableList(terms);
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public List<Term> getSearchTerms() {
        return this.searchTerms;
    }
}
