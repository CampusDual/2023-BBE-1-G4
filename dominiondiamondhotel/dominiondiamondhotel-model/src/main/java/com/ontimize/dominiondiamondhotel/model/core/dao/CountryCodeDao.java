package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "CountryCodeDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/CountryCodeDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class CountryCodeDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_ISO = "iso";
    public static final String ATTR_NAME = "name";
}
