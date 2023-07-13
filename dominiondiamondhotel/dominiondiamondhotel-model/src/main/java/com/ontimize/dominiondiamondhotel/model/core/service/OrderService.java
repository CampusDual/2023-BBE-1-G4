package com.ontimize.dominiondiamondhotel.model.core.service;

import com.google.gson.JsonArray;
import com.ontimize.dominiondiamondhotel.api.core.service.IOrderService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.OrderDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.*;

@Lazy
@Service("OrderService")
public class OrderService implements IOrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;
    @Autowired
    private ProductService productService;
    @Autowired
    private BookingService bookingService;

    @Override
    public EntityResult orderFood(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        Map<String,Object>filter = (Map<String,Object>)attrMap.get(FILTER);
        Map<String,Object>data = (Map<String, Object>) attrMap.get(DATA);
        boolean fin = false;
        ArrayList productSimple = new ArrayList();
        ArrayList productMenu= new ArrayList();
        ArrayList productsOrderList = (ArrayList) attrMap.get("products_list");
        EntityResult er = new EntityResultMapImpl();
        Map<String,Object> booking = new HashMap<>();
        int bookingId = Integer.parseInt(String.valueOf(filter.get(BookingDao.ATTR_ID)));
        booking.put(BookingDao.ATTR_ID,bookingId);
        EntityResult bookingExist = this.bookingService.bookingQuery(booking,List.of(BookingDao.ATTR_ID));
        if(!bookingExist.isEmpty()){
            Map<String,Object>productBooking = new HashMap<>();
            EntityResult productExist = new EntityResultMapImpl();
            ArrayList products = (ArrayList) data.get("products");
            for(int i =0; i<products.size();i++){
                productBooking.put(ProductDao.ATTR_ID,products.get(i));
                productExist = productService.productQuery(productBooking,List.of(ProductDao.ATTR_PRODUCTTYPE_ID));
                if(productExist.isEmpty()){
                    fin=true;
                    i=products.size();
                }else{
                    if(Integer.parseInt(String.valueOf(((List<?>) productExist.get(ProductDao.ATTR_PRODUCTTYPE_ID)).get(0))) == 9){
                        productMenu.add(products.get(i));
                    }else{
                        productSimple.add(products.get(i));
                    }
                }
            }
            if(!fin){
                if(productMenu.size()!= productsOrderList.size()){
                    er.setMessage("Need specify dish of menu");
                    er.setCode(EntityResult.OPERATION_WRONG);
                }else{
                    Map<String,Object>productSimpleMap = new HashMap<>();
                    Map<String,Object>productMenuMap = new HashMap<>();
                    for(int i=0;i<productMenu.size();i++){
                        ArrayList insertMenu = (ArrayList) productsOrderList.get(i);
                        int productId = (int) productMenu.get(i);
                        if(productId == 52 && insertMenu.size() != 2){
                            er.setMessage("Kid's menu only has 2 dishes");
                            er.setCode(EntityResult.OPERATION_WRONG);
                            return er;
                        }
                    }
                    for(int i=0;i<productSimple.size();i++){
                        productSimpleMap.put(OrderDao.ATTR_BOOKING_ID,bookingId);
                        productSimpleMap.put(OrderDao.ATTR_PRODUCT_ID,productSimple.get(i));
                        this.daoHelper.insert(this.orderDao,productSimpleMap);
                    }
                    for(int i=0;i<productMenu.size();i++){
                        productMenuMap.put(OrderDao.ATTR_BOOKING_ID,bookingId);
                        productMenuMap.put(OrderDao.ATTR_PRODUCT_ID,productMenu.get(i));
                        ArrayList insertMenu = (ArrayList) productsOrderList.get(i);
                        String menuSelect = insertMenu.toString();
                        productMenuMap.put(OrderDao.ATTR_PRODUCTS_LIST, menuSelect);
                        this.daoHelper.insert(this.orderDao, productMenuMap);
                    }
                    er.setMessage("booking´s id: "+bookingId);
                    er.setCode(EntityResult.OPERATION_SUCCESSFUL);
                    return er;
                }
            }
        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }
}
