package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "HistRoomDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/HistRoomDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class HistRoomDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_CHANGE_DATE = "change_date";
    public static final String ATTR_ROOM_ID = "room_id";
    public static final String ATTR_STATE_ID = "state_id";

    public static List<String> getColumns(){
        List<String> columns = new ArrayList<>();
        for (Field f : HistRoomDao.class.getDeclaredFields()) {
            String field = f.toString().substring(f.toString().indexOf("_") + 1).toLowerCase();
            if (!field.equals("default")) {
                columns.add(field);
            }
        }
        return columns;
    }
}
