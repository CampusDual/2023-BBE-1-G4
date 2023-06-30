package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

public class CommonUtils {
    private CommonUtils() {

    }

    public static SQLStatementBuilder.BasicExpression moreThan(SQLStatementBuilder.BasicField attrMin, SQLStatementBuilder.BasicField field){

        return new SQLStatementBuilder.BasicExpression(field, SQLStatementBuilder.BasicOperator.MORE_EQUAL_OP, attrMin);

    }

    public static SQLStatementBuilder.BasicExpression lessThan( SQLStatementBuilder.BasicField attrMax, SQLStatementBuilder.BasicField field){

        return new SQLStatementBuilder.BasicExpression(field, SQLStatementBuilder.BasicOperator.LESS_EQUAL_OP, attrMax);

    }


    public static SQLStatementBuilder.BasicExpression andExpression(Double max, Double min, int opcion){

        SQLStatementBuilder.BasicField field = null;

        switch (opcion){

            case 1:
                field = new SQLStatementBuilder.BasicField(HotelDao.ATTR_RATING);
                break;
            case 2:
                field = new SQLStatementBuilder.BasicField(ProductDao.ATTR_PRICE);
                break;

        }

        SQLStatementBuilder.BasicField attrMin = new SQLStatementBuilder.BasicField(String.valueOf(min));
        SQLStatementBuilder.BasicField attrMax = new SQLStatementBuilder.BasicField(String.valueOf(max));

        return  new SQLStatementBuilder.BasicExpression(moreThan(attrMin, field), SQLStatementBuilder.BasicOperator.AND_OP, lessThan(attrMax, field));



    }

    public static SQLStatementBuilder.BasicExpression normalAnd(SQLStatementBuilder.BasicExpression be, SQLStatementBuilder.BasicExpression be2){



        return  new SQLStatementBuilder.BasicExpression(be, SQLStatementBuilder.BasicOperator.AND_OP, be2);



    }

}
