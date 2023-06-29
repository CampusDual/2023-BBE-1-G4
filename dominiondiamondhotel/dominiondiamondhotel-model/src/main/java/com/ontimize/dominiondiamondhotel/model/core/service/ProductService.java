package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IProductService;
import com.ontimize.dominiondiamondhotel.model.core.dao.AllergensDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductTypeDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.HotelUtils;
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

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.*;

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
    private ProductTypeDao productTypeDao;

    @Override
    public EntityResult productInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        Map<String , Object> filter = (Map<String, Object>) attrMap.get(FILTER);
        int productTypeId = Integer.parseInt(String.valueOf(filter.get(ProductDao.ATTR_PRODUCTTYPE_ID)));
        Double price = Double.parseDouble(String.valueOf(filter.get(ProductDao.ATTR_PRICE)));
        Map<String ,Object> productTypeMap = new HashMap<>();
        productTypeMap.put(ProductTypeDao.ATTR_ID, productTypeId);
        EntityResult productTypeExists = this.daoHelper.query(this.productTypeDao, productTypeMap, List.of(ProductTypeDao.ATTR_ID));
        EntityResult allergenExists = new EntityResultMapImpl();
        EntityResult er = new EntityResultMapImpl();
        if(filter.get(ProductDao.ATTR_ALLERGENS_ID) != null) {
            Map<String, Object> allergenMap = new HashMap<>();
            allergenMap.put(AllergensDao.ATTR_ID, Integer.parseInt(String.valueOf(filter.get(ProductDao.ATTR_ALLERGENS_ID))));
            allergenExists = this.daoHelper.query(this.allergensDao, allergenMap, List.of(AllergensDao.ATTR_ID));
        }
        if(((allergenExists.get(AllergensDao.ATTR_ID) instanceof List || filter.get("allergens_id") == null) && ((List<?>) productTypeExists.get(ProductTypeDao.ATTR_ID)).get(0) != null && price > 0)){

            return this.daoHelper.insert(this.productDao, filter);

        }else{
            er.setMessage(INVALID_DATA);
            er.setCode(EntityResult.OPERATION_WRONG);
            return er;
        }
    }
    @Override
    public AdvancedEntityResult productPaginationQuery(Map<?,?> keysValues, List<?> attributesValues, int pagesize, int offset, List<SQLStatementBuilder.SQLOrder> orderby) throws OntimizeJEERuntimeException {
        Map<String,Object>filterMap= new HashMap<>();

        if(keysValues.get("allergens_id")==null && keysValues.get("producttype_id")== null){
            Double min = Double.parseDouble(String.valueOf(keysValues.get("pricemin")));
            Double max = Double.parseDouble(String.valueOf(keysValues.get("pricemax")));
            if(min>0 && max>min){
                filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, ProductUtils.andExpression(ProductUtils.moreThan(min), ProductUtils.lessThan(max)));
            }else{
                return new AdvancedEntityResultMapImpl(EntityResult.OPERATION_WRONG,EntityResult.type);
            }
        }
        if(keysValues.get("allergens_id")==null && (keysValues.get("pricemin")== null || keysValues.get("pricemax")==null)){
            filterMap.put(ProductDao.ATTR_PRODUCTTYPE_ID,Integer.parseInt(String.valueOf(keysValues.get("producttype_id"))));
        }
        if(keysValues.get("producttype_id")==null && (keysValues.get("pricemin")== null || keysValues.get("pricemax")==null)){
            filterMap.put(ProductDao.ATTR_ALLERGENS_ID,Integer.parseInt(String.valueOf(keysValues.get("allergens_id"))));
        }
        return this.daoHelper.paginationQuery(this.productDao,filterMap,attributesValues,pagesize,offset,orderby,"filteredgetproduct");
    }


}