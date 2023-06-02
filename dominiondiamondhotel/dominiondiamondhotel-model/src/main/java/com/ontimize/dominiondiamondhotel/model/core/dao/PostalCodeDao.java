package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "PostalCodeDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/PostalCodeDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class PostalCodeDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_ZIP = "zip";
}
