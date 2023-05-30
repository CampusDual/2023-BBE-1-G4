package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "StateDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/StateDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class StateDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_STATE = "state";
}
