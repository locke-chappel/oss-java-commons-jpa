module com.github.lc.oss.commons.jpa {
    requires com.github.lc.oss.commons.serialization;
    requires com.github.lc.oss.commons.util;

    requires transitive jakarta.persistence;
    requires org.hibernate.orm.core;

    exports com.github.lc.oss.commons.jpa;
}
