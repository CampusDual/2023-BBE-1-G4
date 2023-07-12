package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.jee.common.db.SQLStatementBuilder;

public class BasicExpressionUtils {
    private BasicExpressionUtils() {

    }

    public static SQLStatementBuilder.BasicExpression searchBy(SQLStatementBuilder.Operator op, String searchBy, String data) {
        if (op == SQLStatementBuilder.BasicOperator.LIKE_OP) {
            SQLStatementBuilder.BasicField pvName = new SQLStatementBuilder.BasicField("'%" + data.toUpperCase() + "%'");
            SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(searchBy);
            return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.LIKE_OP, pvName);
        } else if (op == SQLStatementBuilder.BasicOperator.EQUAL_OP) {
            SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(searchBy);
            return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.EQUAL_OP, Integer.parseInt(data));
        } else if (op == SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP) {
            SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(searchBy);
            return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, Integer.parseInt(data));
        }
        return null;
    }
}
