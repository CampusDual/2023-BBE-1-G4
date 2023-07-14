package com.ontimize.dominiondiamondhotel.model.core.utils;

import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductsAllergensDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;

import java.util.List;

public class ProductUtils {

    private ProductUtils(){

    }

    public static SQLStatementBuilder.BasicExpression notMeat() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 15);
    }

    public static SQLStatementBuilder.BasicExpression notCrustace() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 3);
    }

    public static SQLStatementBuilder.BasicExpression notFish() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 5);
    }

    public static SQLStatementBuilder.BasicExpression notMolusc() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 8);
    }

    public static SQLStatementBuilder.BasicExpression firstAnd() {
        return new SQLStatementBuilder.BasicExpression(notMeat(), SQLStatementBuilder.BasicOperator.AND_OP, notCrustace());
    }

    public static SQLStatementBuilder.BasicExpression secondAnd() {
        return new SQLStatementBuilder.BasicExpression(notFish(), SQLStatementBuilder.BasicOperator.AND_OP, notMolusc());
    }

    public static SQLStatementBuilder.BasicExpression andForVegetarians() {

        return new SQLStatementBuilder.BasicExpression(firstAnd(), SQLStatementBuilder.BasicOperator.AND_OP, secondAnd());

    }

    public static SQLStatementBuilder.BasicExpression opForVegetarians() {
        return new SQLStatementBuilder.BasicExpression(andForVegetarians(), SQLStatementBuilder.BasicOperator.OR_OP, allergensCanBeNull());
    }

    public static SQLStatementBuilder.BasicExpression definitelyCrustace() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.EQUAL_OP, 3);
    }

    public static SQLStatementBuilder.BasicExpression definitelyMolusc() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.EQUAL_OP, 8);
    }

    public static SQLStatementBuilder.BasicExpression opForSeafood() {
        return new SQLStatementBuilder.BasicExpression(definitelyCrustace(), SQLStatementBuilder.BasicOperator.OR_OP, definitelyMolusc());
    }

    public static SQLStatementBuilder.BasicExpression notMilk() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 7);
    }

    public static SQLStatementBuilder.BasicExpression notEggs() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NOT_EQUAL_OP, 4);
    }

    public static SQLStatementBuilder.BasicExpression thirdAnd() {
        return new SQLStatementBuilder.BasicExpression(notMilk(), SQLStatementBuilder.BasicOperator.AND_OP, notEggs());
    }

    public static SQLStatementBuilder.BasicExpression vegansAnd() {
        return new SQLStatementBuilder.BasicExpression(opForVegetarians(), SQLStatementBuilder.BasicOperator.AND_OP, thirdAnd());
    }

    public static SQLStatementBuilder.BasicExpression opForVegans() {
        return new SQLStatementBuilder.BasicExpression(vegansAnd(), SQLStatementBuilder.BasicOperator.OR_OP, allergensCanBeNull());
    }

    public static SQLStatementBuilder.BasicExpression allergensCanBeNull() {
        SQLStatementBuilder.BasicField allergensId = new SQLStatementBuilder.BasicField(ProductsAllergensDao.ATTR_ALLERGEN_ID);
        return new SQLStatementBuilder.BasicExpression(allergensId, SQLStatementBuilder.BasicOperator.NULL_OP, null);
    }

    public static SQLStatementBuilder.BasicExpression productsWithoutTheseAllergens(List<?> idsWithAllergens) {
        SQLStatementBuilder.BasicField productId = new SQLStatementBuilder.BasicField(ProductDao.ATTR_ID);
        return new SQLStatementBuilder.BasicExpression(productId, SQLStatementBuilder.BasicOperator.NOT_IN_OP, idsWithAllergens);
    }


    public static SQLStatementBuilder.BasicExpression productsWithTheseAllergens(List<?>  idsWithAllergens) {
        SQLStatementBuilder.BasicField productId = new SQLStatementBuilder.BasicField(ProductDao.ATTR_ID);
        return new SQLStatementBuilder.BasicExpression(productId, SQLStatementBuilder.BasicOperator.IN_OP, idsWithAllergens);
    }


}
