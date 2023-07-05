package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "ProductDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/ProductDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class ProductDao extends OntimizeJdbcDaoSupport {

    public static final String ATTR_ID = "id";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_DESCRIPTION = "description";
    public static final String ATTR_PRODUCTTYPE_ID = "producttype_id";
    public static final String ATTR_PRICE = "price";

    public static List<String> getColumns(){
        List<String> columns = new ArrayList<>();
        for (Field f : ProductDao.class.getDeclaredFields()) {
            String field = f.toString().substring(f.toString().indexOf("_") + 1).toLowerCase();
            if (!field.equals("default")) {
                columns.add(field);
            }
        }
        return columns;
    }

}
