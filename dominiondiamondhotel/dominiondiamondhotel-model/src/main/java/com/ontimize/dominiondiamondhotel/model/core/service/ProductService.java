package com.ontimize.dominiondiamondhotel.model.core.service;
import com.ontimize.dominiondiamondhotel.api.core.service.IProductService;
import com.ontimize.dominiondiamondhotel.model.core.dao.AllergensDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductTypeDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.CommonUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.ProductUtils;
import com.ontimize.jee.common.db.AdvancedEntityResult;
import com.ontimize.jee.common.db.AdvancedEntityResultMapImpl;
import com.ontimize.dominiondiamondhotel.model.core.utils.BasicExpressionUtils;
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

    @Override
    public EntityResult productInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        Map<String, Object> filter = (Map<String, Object>) attrMap.get(FILTER);
        int productTypeId = Integer.parseInt(String.valueOf(filter.get(ProductDao.ATTR_PRODUCTTYPE_ID)));
        double price = Double.parseDouble(String.valueOf(filter.get(ProductDao.ATTR_PRICE)));
        Map<String, Object> productTypeMap = new HashMap<>();
        productTypeMap.put(ProductTypeDao.ATTR_ID, productTypeId);
        EntityResult productTypeExists = this.productTypeService.productTypeQuery(productTypeMap, List.of(ProductTypeDao.ATTR_ID));
        EntityResult allergenExists = new EntityResultMapImpl();
        EntityResult er = new EntityResultMapImpl();
        if (filter.get(ProductDao.ATTR_ALLERGENS_ID) != null) {
            Map<String, Object> allergenMap = new HashMap<>();
            allergenMap.put(AllergensDao.ATTR_ID, Integer.parseInt(String.valueOf(filter.get(ProductDao.ATTR_ALLERGENS_ID))));
            allergenExists = this.daoHelper.query(this.allergensDao, allergenMap, List.of(AllergensDao.ATTR_ID));
        }
        if (((allergenExists.get(AllergensDao.ATTR_ID) instanceof List || filter.get("allergens_id") == null) && ((List<?>) productTypeExists.get(ProductTypeDao.ATTR_ID)).get(0) != null && price > 0)) {
            return this.daoHelper.insert(this.productDao, filter);
        } else {
            er.setMessage(INVALID_DATA);
            er.setCode(EntityResult.OPERATION_WRONG);
            return er;
        }
    }
    @Override
    public AdvancedEntityResult productPaginationQuery(Map<?,?> keysValues, List<?> attributesValues, int pagesize, int offset, List<SQLStatementBuilder.SQLOrder> orderby) throws OntimizeJEERuntimeException {
        Map<String,Object>filterMap= new HashMap<>();

        if(keysValues.get("pricemin")!=null && keysValues.get("pricemax")!= null){
            Double min = Double.parseDouble(String.valueOf(keysValues.get("pricemin")));
            Double max = Double.parseDouble(String.valueOf(keysValues.get("pricemax")));
            if(min>0 && max>min){
                filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, CommonUtils.andExpression(max, min , 2));
            }else{
                return new AdvancedEntityResultMapImpl(EntityResult.OPERATION_WRONG,EntityResult.type);
            }
        }
        if(keysValues.get("producttype_id")!=null){
            filterMap.put(ProductDao.ATTR_PRODUCTTYPE_ID,Integer.parseInt(String.valueOf(keysValues.get("producttype_id"))));
        }
        if(keysValues.get("allergens_id")!=null ){
            filterMap.put(ProductDao.ATTR_ALLERGENS_ID,Integer.parseInt(String.valueOf(keysValues.get("allergens_id"))));
        }
        return this.daoHelper.paginationQuery(this.productDao,filterMap,attributesValues,pagesize,offset,orderby,"filteredgetproduct");
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
            menu.put("First dishes", getRandomDishes(getDishes("First dish", 0)));
            menu.put("Second dishes", getRandomDishes(getDishes("Second dish", 0)));
            menu.put("Desserts", getRandomDishes(getDishes("Dessert", 0)));
            finalMenu.put("Menu", menu);
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
            seafoodMap.put("First dishes", getRandomDishes(getDishes("First dish", 1)));
            seafoodMap.put("Second dishes", getRandomDishes(getDishes("Second dish", 1)));
            seafoodMap.put("Desserts", getRandomDishes(getDishes("Dessert", 0)));
            veganMap.put("First dishes", getRandomDishes(getDishes("First dish", 2)));
            veganMap.put("Second dishes", getRandomDishes(getDishes("Second dish", 2)));
            veganMap.put("Desserts", getRandomDishes(getDishes("Dessert", 2)));
            vegetarianMap.put("First dishes", getRandomDishes(getDishes("First dish",3 )));
            vegetarianMap.put("Second dishes", getRandomDishes(getDishes("Second dish", 3)));
            vegetarianMap.put("Desserts", getRandomDishes(getDishes("Dessert", 3)));
            kidsMap.put("First dishes", getRandomDishes(getDishes("Kids dish", 4)));
            kidsMap.put("Desserts", getRandomDishes(getDishes("Kids dessert", 4)));
            finalMenu.put("Seafood Menu", seafoodMap);
            finalMenu.put("Vegan Menu", veganMap);
            finalMenu.put("Vegetarian Menu", vegetarianMap);
            finalMenu.put("Kids Menu", kidsMap);
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
        Map<String, Object> key = new HashMap<>();
        switch (option){

            case 1:
                be2 = ProductUtils.opForSeafood();
                break;
            case 2:
                be2 = ProductUtils.opForVegans();
                break;
            case 3:
                be2 = ProductUtils.opForVegetarians();
                break;

        }
        SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, ProductDao.ATTR_PRODUCTTYPE_ID, dishId);
        if(be != null && be2 != null){
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,CommonUtils.normalAnd(be,be2));
        }else if (be != null) {
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }else if(be2 != null){
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be2);
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

}
