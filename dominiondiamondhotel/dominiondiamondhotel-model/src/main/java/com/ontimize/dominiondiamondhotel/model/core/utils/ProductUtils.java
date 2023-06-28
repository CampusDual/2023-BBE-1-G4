package com.ontimize.dominiondiamondhotel.model.core.utils;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class ProductUtils {
    private ProductUtils(){

    }
    public static SQLStatementBuilder.BasicExpression moreThan(Double min){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(min));
        SQLStatementBuilder.BasicField price = new SQLStatementBuilder.BasicField(ProductDao.ATTR_PRICE);
        return new SQLStatementBuilder.BasicExpression(price, SQLStatementBuilder.BasicOperator.MORE_EQUAL_OP, attr);

    }
    public static SQLStatementBuilder.BasicExpression lessThan(Double max){

        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(String.valueOf(max));
        SQLStatementBuilder.BasicField price = new SQLStatementBuilder.BasicField(ProductDao.ATTR_PRICE);
        return new SQLStatementBuilder.BasicExpression(price, SQLStatementBuilder.BasicOperator.LESS_EQUAL_OP, attr);

    }
    public static SQLStatementBuilder.BasicExpression andExpression(SQLStatementBuilder.BasicExpression moreThan, SQLStatementBuilder.BasicExpression lessThan){

        return  new SQLStatementBuilder.BasicExpression(moreThan, SQLStatementBuilder.BasicOperator.AND_OP, lessThan);

    }
}
