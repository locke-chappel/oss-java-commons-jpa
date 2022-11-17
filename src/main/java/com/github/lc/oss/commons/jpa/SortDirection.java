package com.github.lc.oss.commons.jpa;

import java.util.Set;

import com.github.lc.oss.commons.util.TypedEnumCache;

public enum SortDirection {
    Asc,
    Desc;

    private static final TypedEnumCache<SortDirection, SortDirection> CACHE = new TypedEnumCache<>(SortDirection.class, false);

    public static Set<SortDirection> all() {
        return SortDirection.CACHE.values();
    }

    public static SortDirection byName(String name) {
        return SortDirection.CACHE.byName(name);
    }

    public static boolean hasName(String name) {
        return SortDirection.CACHE.hasName(name);
    }

    public static SortDirection tryParse(String name) {
        return SortDirection.CACHE.tryParse(name);
    }
}
