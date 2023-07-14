package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IProductService;
import com.ontimize.dominiondiamondhotel.model.core.dao.AllergensDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductTypeDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductsAllergensDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.BasicExpressionUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.CommonUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.ProductUtils;
import com.ontimize.jee.common.db.AdvancedEntityResult;
import com.ontimize.jee.common.db.AdvancedEntityResultMapImpl;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.INVALID_DATA;

@Lazy
@Service("ProductService")
public class ProductService implements IProductService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private AllergensDao allergensDao;

    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private ProductsAllergensDao productsAllergensDao;

    private final String FIRST_DISHES = "First dishes";
    private final String FIRST_DISH = "First dish";
    private final String SECOND_DISHES = "Second dishes";
    private final String SECOND_DISH = "Second dish";
    private final String DESSERTS = "Desserts";
    private final String DESSERT = "Dessert";

    @Override
    public EntityResult productInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        Map<?, ?> filter = (Map<?, ?>) attrMap.get(FILTER);
        List<Integer> listOfAllergens = (List<Integer>) attrMap.get("allergens");
        int productTypeId = Integer.parseInt(String.valueOf(filter.get(ProductDao.ATTR_PRODUCTTYPE_ID)));
        double price = Double.parseDouble(String.valueOf(filter.get(ProductDao.ATTR_PRICE)));
        Map<String, Object> productTypeMap = new HashMap<>();
        productTypeMap.put(ProductTypeDao.ATTR_ID, productTypeId);
        EntityResult productTypeExists = this.productTypeService.productTypeQuery(productTypeMap, List.of(ProductTypeDao.ATTR_ID));
        EntityResult allergenER;
        EntityResult er = new EntityResultMapImpl();
        boolean allergenExists = true;
        if (listOfAllergens != null) {
            Map<String, Object> allergenMap = new HashMap<>();
            for (Integer listOfAllergen : listOfAllergens) {
                allergenMap.put(AllergensDao.ATTR_ID, listOfAllergen);
                allergenER = this.daoHelper.query(this.allergensDao, allergenMap, List.of(AllergensDao.ATTR_ID));
                if (((List<?>) allergenER.get(AllergensDao.ATTR_ID)).get(0) == null) {
                    allergenExists = false;
                    break;
                }
            }
        }
        if (((List<?>) productTypeExists.get(ProductTypeDao.ATTR_ID)).get(0) != null && price > 0 && allergenExists) {
            EntityResult insertedProduct = this.daoHelper.insert(this.productDao, filter);
            int productId = (int) insertedProduct.get(ProductDao.ATTR_ID);
            if (listOfAllergens != null) {
                for (int allergenId : listOfAllergens) {
                    Map<String, Object> mapForInsert = new HashMap<>();
                    mapForInsert.put(ProductsAllergensDao.ATTR_PRODUCT_ID, productId);
                    mapForInsert.put(ProductsAllergensDao.ATTR_ALLERGEN_ID, allergenId);
                    this.daoHelper.insert(this.productsAllergensDao, mapForInsert);
                }
            }
            er.setMessage("Product saved; Product's id = " + productId);
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        } else {
            er.setMessage(INVALID_DATA);
            er.setCode(EntityResult.OPERATION_WRONG);
        }
        return er;
    }

    @Override
    public AdvancedEntityResult productPaginationQuery(Map<?, ?> keysValues, List<?> attributesValues, int pagesize, int offset, List<SQLStatementBuilder.SQLOrder> orderby) throws OntimizeJEERuntimeException {
        Map<String, Object> filterMap = new HashMap<>();
        if (keysValues.get("pricemin") != null && keysValues.get("pricemax") != null) {
            double min = Double.parseDouble(String.valueOf(keysValues.get("pricemin")));
            double max = Double.parseDouble(String.valueOf(keysValues.get("pricemax")));
            if (min > 0 && max > min) {
                filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, CommonUtils.andExpression(max, min, 2));
            } else {
                return new AdvancedEntityResultMapImpl(EntityResult.OPERATION_WRONG, EntityResult.type);
            }
        }
        if (keysValues.get("producttype_id") != null) {
            filterMap.put(ProductDao.ATTR_PRODUCTTYPE_ID, Integer.parseInt(String.valueOf(keysValues.get("producttype_id"))));
        }
        if (keysValues.get("allergens_id") != null) {
            List<Integer> listOfAllergens = (List<Integer>) keysValues.get("allergens_id");
            Map<String, Object> allergensMap = new HashMap<>();
            for (Integer listOfAllergen : listOfAllergens) {
                allergensMap.put(ProductsAllergensDao.ATTR_ALLERGEN_ID, listOfAllergen);
            }
            EntityResult productsId = this.daoHelper.query(this.productsAllergensDao, allergensMap, List.of(ProductsAllergensDao.ATTR_PRODUCT_ID));
            List<?> productsArrayList = (List<?>) productsId.get("product_id");
            filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, ProductUtils.productsWithoutTheseAllergens(productsArrayList));
        }
        return this.daoHelper.paginationQuery(this.productDao, filterMap, attributesValues, pagesize, offset, orderby, "filteredgetproduct");
    }


    @Override
    public EntityResult productQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.productDao, keyMap, attrList);
    }

    @Override
    public EntityResult getMenuQuery() throws OntimizeJEERuntimeException {
        try {
            EntityResult finalMenu = new EntityResultMapImpl();
            LinkedHashMap<String, Object> menu = new LinkedHashMap<>();
            menu.put(FIRST_DISHES, getRandomDishes(getDishes(FIRST_DISH, 0)));
            menu.put(SECOND_DISH, getRandomDishes(getDishes(SECOND_DISH, 0)));
            menu.put(DESSERTS, getRandomDishes(getDishes(DESSERT, 0)));
            finalMenu.put("Menu", menu);
            Map<String, Object> filter = new HashMap<>();
            filter.put(ProductDao.ATTR_ID, 46);
            String description = getMenuAsString(finalMenu);
            Map<String, Object> data = new HashMap<>();
            data.put(ProductDao.ATTR_DESCRIPTION, description);
            this.daoHelper.update(this.productDao, data, filter);
            return finalMenu;
        } catch (Exception e) {
            e.printStackTrace();
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @Override
    public EntityResult getVarietyMenusQuery() throws OntimizeJEERuntimeException {
        try {
            EntityResult finalMenu = new EntityResultMapImpl();  // 0 = normal, 1 = seafood, 2 = vegan, 3 = vegetarian, 4= kids;
            LinkedHashMap<String, Object> seafoodMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> veganMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> vegetarianMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> kidsMap = new LinkedHashMap<>();
            seafoodMap.put(FIRST_DISHES, getRandomDishes(getDishes(FIRST_DISH, 1)));
            seafoodMap.put(SECOND_DISHES, getRandomDishes(getDishes(SECOND_DISH, 1)));
            seafoodMap.put(DESSERTS, getRandomDishes(getDishes(DESSERT, 0)));
            veganMap.put(FIRST_DISHES, getRandomDishes(getDishes(FIRST_DISH, 2)));
            veganMap.put(SECOND_DISHES, getRandomDishes(getDishes(SECOND_DISH, 2)));
            veganMap.put(DESSERTS, getRandomDishes(getDishes(DESSERT, 2)));
            vegetarianMap.put(FIRST_DISHES, getRandomDishes(getDishes(FIRST_DISH, 3)));
            vegetarianMap.put(SECOND_DISHES, getRandomDishes(getDishes(SECOND_DISH, 3)));
            vegetarianMap.put(DESSERTS, getRandomDishes(getDishes(DESSERT, 3)));
            kidsMap.put(FIRST_DISHES, getRandomDishes(getDishes("Kids dish", 4)));
            kidsMap.put(DESSERTS, getRandomDishes(getDishes("Kids dessert", 4)));
            finalMenu.put("Seafood Menu", seafoodMap);
            finalMenu.put("Vegan Menu", veganMap);
            finalMenu.put("Vegetarian Menu", vegetarianMap);
            finalMenu.put("Kids Menu", kidsMap);
            List<String> strings = getVarietyMenusAsString(finalMenu);
            String seafood = strings.get(0);
            String vegan = strings.get(1);
            String vegetarian = strings.get(2);
            String kids = strings.get(3);
            Map<String, Object> seafoodFilter = new HashMap<>();
            seafoodFilter.put(ProductDao.ATTR_ID, 51);
            Map<String, Object> veganFilter = new HashMap<>();
            veganFilter.put(ProductDao.ATTR_ID, 50);
            Map<String, Object> vegetarianFilter = new HashMap<>();
            vegetarianFilter.put(ProductDao.ATTR_ID, 49);
            Map<String, Object> kidsFilter = new HashMap<>();
            kidsFilter.put(ProductDao.ATTR_ID, 52);
            Map<String, Object> seafoodData = new HashMap<>();
            seafoodData.put(ProductDao.ATTR_DESCRIPTION, seafood);
            Map<String, Object> veganData = new HashMap<>();
            veganData.put(ProductDao.ATTR_DESCRIPTION, vegan);
            Map<String, Object> vegetarianData = new HashMap<>();
            vegetarianData.put(ProductDao.ATTR_DESCRIPTION, vegetarian);
            Map<String, Object> kidsData = new HashMap<>();
            kidsData.put(ProductDao.ATTR_DESCRIPTION, kids);
            this.daoHelper.update(this.productDao, seafoodData, seafoodFilter);
            this.daoHelper.update(this.productDao, veganData, veganFilter);
            this.daoHelper.update(this.productDao, vegetarianData, vegetarianFilter);
            this.daoHelper.update(this.productDao, kidsData, kidsFilter);
            return finalMenu;
        } catch (Exception e) {
            e.printStackTrace();
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }


    private String getIdOfProductType(String productType) {
        Map<String, Object> key = new HashMap<>();
        SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.LIKE_OP, ProductTypeDao.ATTR_NAME, productType);
        if (be != null) {
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        return String.valueOf(((List<?>) productTypeService.productTypeQuery(key, List.of(ProductTypeDao.ATTR_ID)).get(ProductTypeDao.ATTR_ID)).get(0));
    }

    private EntityResult getDishes(String dishName, int option) {
        String dishId = getIdOfProductType(dishName);
        SQLStatementBuilder.BasicExpression be2 = null;
        SQLStatementBuilder.BasicExpression be3 = null;
        SQLStatementBuilder.BasicExpression be4 = null;
        List<?> productsArrayList;
        List<?> allProductsArrayList;
        Map<String, Object> key = new HashMap<>();
        switch (option) {
            case 1:
                be2 = ProductUtils.opForSeafood();
                break;
            case 2:
                be2 = ProductUtils.opForVegans();
                break;
            case 3:
                be2 = ProductUtils.opForVegetarians();
                break;
            default:
        }
        SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, ProductDao.ATTR_PRODUCTTYPE_ID, dishId);
        if (be != null && be2 != null) {
            Map<String, Object> filter = new HashMap<>();
            filter.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be2);
            EntityResult productIds = this.daoHelper.query(this.productsAllergensDao, filter, List.of(ProductsAllergensDao.ATTR_PRODUCT_ID));
            productsArrayList = (List<?>) productIds.get(ProductsAllergensDao.ATTR_PRODUCT_ID);
            Map<String, Object> emptyFilter = new HashMap<>();
            EntityResult allProducts = this.daoHelper.query(this.productsAllergensDao, emptyFilter, List.of(ProductsAllergensDao.ATTR_PRODUCT_ID));
            allProductsArrayList = (List<?>) allProducts.get(ProductsAllergensDao.ATTR_PRODUCT_ID);
            be3 = ProductUtils.productsWithTheseAllergens(productsArrayList);
            be4 = ProductUtils.productsWithoutTheseAllergens(allProductsArrayList);
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, CommonUtils.normalAnd(be, CommonUtils.normalOr(be3, be4)));
        } else if (be != null) {
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        return this.productQuery(key, List.of(ProductDao.ATTR_NAME));
    }

    private List<?> getRandomDishes(EntityResult dishes) {
        List<?> dishesFromER = (List<?>) dishes.get(ProductDao.ATTR_NAME);
        List<String> dishesReturn = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            int randomIndex = r.nextInt(dishesFromER.size());
            String dishToAdd = String.valueOf(dishesFromER.get(randomIndex));
            while (dishesReturn.contains(dishToAdd)) {
                randomIndex = r.nextInt(dishesFromER.size());
                dishToAdd = String.valueOf(dishesFromER.get(randomIndex));
            }
            dishesReturn.add(dishToAdd);
        }
        return dishesReturn;
    }

    public String getMenuAsString(EntityResult finalMenu) {
        LinkedHashMap<String, Object> menuMap = (LinkedHashMap<String, Object>) finalMenu.get("Menu");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : menuMap.entrySet()) {
            String category = entry.getKey();
            List<String> dishes = (List<String>) entry.getValue();

            sb.append(category).append(": ");
            for (String dish : dishes) {
                sb.append(dish).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("; ");
        }
        return sb.toString();
    }

    public List<String> getVarietyMenusAsString(EntityResult finalMenu) {

        LinkedHashMap<String, Object> menu = new LinkedHashMap<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    menu = (LinkedHashMap<String, Object>) finalMenu.get("Seafood Menu");
                    break;
                case 1:
                    menu = (LinkedHashMap<String, Object>) finalMenu.get("Vegan Menu");
                    break;
                case 2:
                    menu = (LinkedHashMap<String, Object>) finalMenu.get("Vegetarian Menu");
                    break;
                case 3:
                    menu = (LinkedHashMap<String, Object>) finalMenu.get("Kids Menu");
                    break;
            }

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : menu.entrySet()) {
                String category = entry.getKey();
                List<String> dishes = (List<String>) entry.getValue();
                sb.append(category).append(": ");
                for (String dish : dishes) {
                    sb.append(dish).append(", ");
                }
                sb.setLength(sb.length() - 2);
                sb.append("; ");
            }
            strings.add(sb.toString());
            sb.setLength(0);
        }
        return strings;
    }

}
