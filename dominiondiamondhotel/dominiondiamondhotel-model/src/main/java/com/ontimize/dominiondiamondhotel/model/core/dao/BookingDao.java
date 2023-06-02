package com.ontimize.dominiondiamondhotel.model.core.dao;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

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

}
