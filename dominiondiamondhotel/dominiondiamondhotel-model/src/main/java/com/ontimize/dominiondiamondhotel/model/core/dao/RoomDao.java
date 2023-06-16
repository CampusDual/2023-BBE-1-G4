package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "RoomDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/RoomDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class RoomDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_NUMBER = "number";
    public static final String ATTR_HOTEL_ID = "hotel_id";
    public static final String ATTR_STATE_ID = "state_id";

    public static List<String> getColumns(){
        List<String> columns = new ArrayList<>();
        for (Field f : RoomDao.class.getDeclaredFields()) {
            String field = f.toString().substring(f.toString().indexOf("_") + 1).toLowerCase();
            if (!field.equals("default")) {
                columns.add(field);
            }
        }
        return columns;
    }
}
