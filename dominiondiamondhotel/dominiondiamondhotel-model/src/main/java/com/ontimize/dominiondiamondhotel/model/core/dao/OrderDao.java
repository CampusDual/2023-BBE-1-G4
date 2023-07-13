package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository(value = "OrderDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/OrderDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class OrderDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_BOOKING_ID = "booking_id";
    public static final String ATTR_PRODUCT_ID = "product_id";
    public static final String ATTR_PRODUCTS_LIST = "products_list";
    public static final String ATTR_CHECKED= "checked";

}
