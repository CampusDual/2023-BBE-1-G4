package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "CustomerDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/CustomerDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class CustomerDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_LASTNAME1 = "lastname1";
    public static final String ATTR_LASTNAME2 = "lastname2";
    public static final String ATTR_PHONE = "phone";
    public static final String ATTR_DNI = "dni";
    public static final String ATTR_EMAIL = "mail";
}
