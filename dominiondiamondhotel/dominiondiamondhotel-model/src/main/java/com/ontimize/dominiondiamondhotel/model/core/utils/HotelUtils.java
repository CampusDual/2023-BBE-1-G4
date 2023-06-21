package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class HotelUtils {
    private HotelUtils() {

    }

    public static SQLStatementBuilder.BasicExpression searchBy(SQLStatementBuilder.Operator op, String searchBy, String data) {
        if (op == SQLStatementBuilder.BasicOperator.LIKE_OP) {
            SQLStatementBuilder.BasicField pvName = new SQLStatementBuilder.BasicField("'%" + data.toLowerCase() + "%'");
            SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField("lower(" + searchBy + ")");
            return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.LIKE_OP, pvName);
        } else if (op == SQLStatementBuilder.BasicOperator.EQUAL_OP) {
            SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(searchBy);
            return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.EQUAL_OP, Integer.parseInt(data));
        }
        return null;
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
