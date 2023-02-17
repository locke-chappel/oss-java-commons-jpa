package io.github.lc.oss.commons.jpa;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SortDirectionTest {
    @Test
    public void test_caching() {
        SortDirection[] values = SortDirection.values();
        Set<SortDirection> all = SortDirection.all();
        Assertions.assertEquals(values.length, all.size());
        Assertions.assertSame(all, SortDirection.all());
        Arrays.stream(values).forEach(v -> Assertions.assertTrue(all.contains(v)));
        all.stream().forEach(a -> {
            Assertions.assertTrue(SortDirection.hasName(a.name().toUpperCase()));
            Assertions.assertSame(a, SortDirection.byName(a.name()));
            Assertions.assertSame(a, SortDirection.tryParse(a.name().toLowerCase()));
        });
    }
}
