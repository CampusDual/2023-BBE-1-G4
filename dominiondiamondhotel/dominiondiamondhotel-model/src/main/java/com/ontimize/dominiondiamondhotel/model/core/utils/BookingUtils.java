package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class BookingUtils {
    private BookingUtils() {

    }

    public static boolean calificationCheck(int calification) {
        return calification > 0 && calification <= 10;
    }

    public static SQLStatementBuilder.BasicExpression meanNotNull(){
        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(BookingDao.ATTR_MEAN);
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.NOT_NULL_OP, null);
    }
}
