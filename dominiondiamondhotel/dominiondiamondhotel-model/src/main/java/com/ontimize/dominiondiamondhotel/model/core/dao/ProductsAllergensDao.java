package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "ProductsAllergensDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/ProductsAllergensDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class ProductsAllergensDao extends OntimizeJdbcDaoSupport {

    public static final String ATTR_ID = "id";
    public static final String ATTR_PRODUCT_ID = "product_id";
    public static final String ATTR_ALLERGEN_ID = "allergen_id";

}
