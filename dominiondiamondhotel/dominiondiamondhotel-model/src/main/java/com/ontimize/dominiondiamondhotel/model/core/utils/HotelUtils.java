package com.ontimize.dominiondiamondhotel.model.core.utils;

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

    public static SQLStatementBuilder.BasicExpression notLess(Double min){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(min));
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.MORE_OP, 0);

    }

    public static SQLStatementBuilder.BasicExpression notMore(Double max){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(max));
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.LESS_OP, 10);

    }

    public static SQLStatementBuilder.BasicExpression betweenQualifies(Double min, Double max){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(min));
        SQLStatementBuilder.BasicField attr2 = new SQLStatementBuilder.BasicField(String.valueOf(max));
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.LESS_OP, 10);

    }
}
