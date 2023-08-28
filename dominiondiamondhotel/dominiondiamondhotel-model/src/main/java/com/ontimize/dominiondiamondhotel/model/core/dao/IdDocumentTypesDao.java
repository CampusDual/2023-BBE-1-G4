package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "IdDocumentTypesDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/IdDocumentTypesDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class IdDocumentTypesDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_IDTYPE = "idtype_id";
}
