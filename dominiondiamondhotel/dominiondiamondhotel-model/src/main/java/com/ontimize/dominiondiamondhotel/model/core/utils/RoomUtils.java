package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class RoomUtils {
    private RoomUtils() {

    }

    public static SQLStatementBuilder.BasicExpression searchByStatus(int status) {
        SQLStatementBuilder.BasicField attrStateId = new SQLStatementBuilder.BasicField(RoomDao.ATTR_STATE_ID);
        return new SQLStatementBuilder.BasicExpression(attrStateId, SQLStatementBuilder.BasicOperator.EQUAL_OP, status);
    }

    public static SQLStatementBuilder.BasicExpression searchByHotelId(int id) {
        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(RoomDao.ATTR_HOTEL_ID);
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.EQUAL_OP, id);
    }
}
