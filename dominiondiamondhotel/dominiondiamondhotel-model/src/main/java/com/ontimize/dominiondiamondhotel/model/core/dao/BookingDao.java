package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "BookingDao")
@Lazy
@ConfigurationFile(
        configurationFile = "dao/BookingDao.xml",
        configurationFilePlaceholder = "dao/placeholders.properties"
)
public class BookingDao extends OntimizeJdbcDaoSupport {
    public static final String ATTR_ID = "id";
    public static final String ATTR_ENTRY_DATE = "entry_date";
    public static final String ATTR_EXIT_DATE = "exit_date";
    public static final String ATTR_HOTEL_ID = "hotel_id";
    public static final String ATTR_CUSTOMER_ID = "customer_id";
    public static final String ATTR_CHECK_IN = "check_in";
    public static final String ATTR_CHECK_OUT = "check_out";
    public static final String ATTR_ROOM_ID = "room_id";
    public static final String ATTR_CLEANING = "cleaning";
    public static final String ATTR_FACILITIES = "facilities";
    public static final String ATTR_PRICEQUALITY = "pricequality";
    public static final String ATTR_COMM = "comm";
    public static final String ATTR_MEAN = "mean";
    public static final String ATTR_EXPENSES = "expenses";

    public static List<String> getColumns(){
        List<String> columns = new ArrayList<>();
        for (Field f : BookingDao.class.getDeclaredFields()) {
            String field = f.toString().substring(f.toString().indexOf("_") + 1).toLowerCase();
            if (!field.equals("default")) {
                columns.add(field);
            }
        }
        return columns;
    }
}
