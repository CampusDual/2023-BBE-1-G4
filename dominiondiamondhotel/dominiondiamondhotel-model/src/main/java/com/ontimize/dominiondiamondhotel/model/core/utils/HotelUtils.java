package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class HotelUtils {
    private HotelUtils() {

    }

    public static SQLStatementBuilder.BasicExpression moreThan(Double min){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(min));
        SQLStatementBuilder.BasicField rating = new SQLStatementBuilder.BasicField(HotelDao.ATTR_RATING);
        return new SQLStatementBuilder.BasicExpression(rating, SQLStatementBuilder.BasicOperator.MORE_EQUAL_OP, attr);

    }

    public static SQLStatementBuilder.BasicExpression lessThan(Double max){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(max));
        SQLStatementBuilder.BasicField rating = new SQLStatementBuilder.BasicField(HotelDao.ATTR_RATING);
        return new SQLStatementBuilder.BasicExpression(rating, SQLStatementBuilder.BasicOperator.LESS_EQUAL_OP, attr);

    }

    public static SQLStatementBuilder.BasicExpression andExpression(SQLStatementBuilder.BasicExpression moreThan, SQLStatementBuilder.BasicExpression lessThan){

        return  new SQLStatementBuilder.BasicExpression(moreThan, SQLStatementBuilder.BasicOperator.AND_OP, lessThan);

    }

}
