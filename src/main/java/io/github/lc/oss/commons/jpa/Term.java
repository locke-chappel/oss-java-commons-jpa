package io.github.lc.oss.commons.jpa;

import java.util.ArrayList;
import java.util.List;

public class Term {
    private final String property;
    private final Object value;
    private final SortDirection sort;

    public static Term of(SearchTerm searchTerm, Object value) {
        return Term.of(searchTerm, value, (SortDirection) null);
    }

    public static Term of(SearchTerm searchTerm, Object value, String sort) {
        return Term.of(searchTerm, value, SortDirection.tryParse(sort));
    }

    public static Term of(SearchTerm searchTerm, Object value, SortDirection sort) {
        Object v = searchTerm.isQueryable() ? value : null;
        SortDirection s = searchTerm.isSortable() ? sort : null;
        if (v == null && s == null) {
            return null;
        }
        return new Term(searchTerm.getProperty(), v, s);
    }

    public static List<Term> of(Term... terms) {
        List<Term> l = new ArrayList<>();
        for (Term t : terms) {
            if (t != null) {
                l.add(t);
            }
        }
        return l;
    }

    public Term(String property, Object value) {
        this(property, value, null);
    }

    public Term(String property, Object value, SortDirection sort) {
        this.property = property;
        this.value = value;
        this.sort = sort;
    }

    public String getProperty() {
        return this.property;
    }

    public Object getValue() {
        return this.value;
    }

    public SortDirection getSort() {
        return this.sort;
    }
}
