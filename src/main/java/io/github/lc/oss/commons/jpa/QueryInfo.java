package io.github.lc.oss.commons.jpa;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Selection;

@SuppressWarnings("unchecked")
public class QueryInfo {
    private Selection<?> selection;
    private CriteriaQuery<?> query;

    public <T> Selection<T> getSelection() {
        return (Selection<T>) this.selection;
    }

    public void setSelection(Selection<?> selection) {
        this.selection = selection;
    }

    public <T> CriteriaQuery<T> getQuery() {
        return (CriteriaQuery<T>) this.query;
    }

    public void setQuery(CriteriaQuery<?> query) {
        this.query = query;
    }
}
