package io.github.lc.oss.commons.jpa;

public interface SearchTerm {
    String name();

    String getProperty();

    boolean isQueryable();

    boolean isSortable();
}
