package io.github.lc.oss.commons.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionRollbackExceptionTest {
    @Test
    public void test_constructor_v1() {
        TransactionRollbackException ex = new TransactionRollbackException();
        Assertions.assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void test_constructor_v2() {
        TransactionRollbackException ex = new TransactionRollbackException("");
        Assertions.assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void test_constructor_v3() {
        TransactionRollbackException ex = new TransactionRollbackException(new RuntimeException());
        Assertions.assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void test_constructor_v4() {
        TransactionRollbackException ex = new TransactionRollbackException("", new RuntimeException());
        Assertions.assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void test_constructor_v5() {
        TransactionRollbackException ex = new TransactionRollbackException("", new RuntimeException(), false, false);
        Assertions.assertTrue(ex instanceof RuntimeException);
    }
}
