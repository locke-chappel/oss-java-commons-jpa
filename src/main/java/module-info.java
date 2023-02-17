module io.github.lc.oss.commons.jpa {
    requires io.github.lc.oss.commons.serialization;
    requires io.github.lc.oss.commons.util;

    requires transitive jakarta.persistence;
    requires org.hibernate.orm.core;

    exports io.github.lc.oss.commons.jpa;
}
