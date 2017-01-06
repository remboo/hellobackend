package com.johnremboo.repositories;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgreSql9Dialect extends PostgreSQL94Dialect {

    public CustomPostgreSql9Dialect() {
        super();
        registerFunction("regexp", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, "(?1 ~ ?2)"));
    }
}
