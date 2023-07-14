package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IOrderService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.OrderDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.ProductDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
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
        Map<?, ?> filter = (Map<?, ?>) attrMap.get(FILTER);
        Map<?, ?> data = (Map<?, ?>) attrMap.get(DATA);
        boolean fin = false;
        List<String> productSimple = new ArrayList<>();
        List<String> productMenu = new ArrayList<>();
        List<?> productsOrderList = (List<?>) attrMap.get("products_list");
        EntityResult er = new EntityResultMapImpl();
        Map<String, Object> booking = new HashMap<>();
        int bookingId = Integer.parseInt(String.valueOf(filter.get(BookingDao.ATTR_ID)));
        booking.put(BookingDao.ATTR_ID, bookingId);
        EntityResult bookingExist = this.bookingService.bookingQuery(booking, List.of(BookingDao.ATTR_ID));
        if (!bookingExist.isEmpty()) {
            Map<String, Object> productBooking = new HashMap<>();
            EntityResult productExist;
            List<String> products = (List<String>) data.get("products");
            for (String product : products) {
                productBooking.put(ProductDao.ATTR_ID, product);
                productExist = productService.productQuery(productBooking, List.of(ProductDao.ATTR_PRODUCTTYPE_ID));
                if (productExist.isEmpty()) {
                    fin = true;
                    break;
                } else {
                    if (Integer.parseInt(String.valueOf(((List<?>) productExist.get(ProductDao.ATTR_PRODUCTTYPE_ID)).get(0))) == 9) {
                        productMenu.add(product);
                    } else {
                        productSimple.add(product);
                    }
                }
            }
            if (!fin) {
                if (productMenu.size() != productsOrderList.size()) {
                    er.setMessage("Need specify dish of menu");
                    er.setCode(EntityResult.OPERATION_WRONG);
                } else {
                    Map<String, Object> productSimpleMap = new HashMap<>();
                    Map<String, Object> productMenuMap = new HashMap<>();
                    for (int i = 0; i < productMenu.size(); i++) {
                        List<?> insertMenu = (List<?>) productsOrderList.get(i);
                        int productId = Integer.parseInt(productMenu.get(i));
                        if (productId == 52 && insertMenu.size() != 2) {
                            er.setMessage("Kid's menu only has 2 dishes");
                            er.setCode(EntityResult.OPERATION_WRONG);
                            return er;
                        }
                    }
                    for (String s : productSimple) {
                        productSimpleMap.put(OrderDao.ATTR_BOOKING_ID, bookingId);
                        productSimpleMap.put(OrderDao.ATTR_PRODUCT_ID, s);
                        this.daoHelper.insert(this.orderDao, productSimpleMap);
                    }
                    for (int i = 0; i < productMenu.size(); i++) {
                        productMenuMap.put(OrderDao.ATTR_BOOKING_ID, bookingId);
                        productMenuMap.put(OrderDao.ATTR_PRODUCT_ID, productMenu.get(i));
                        List<?> insertMenu = (List<?>) productsOrderList.get(i);
                        String menuSelect = insertMenu.toString();
                        productMenuMap.put(OrderDao.ATTR_PRODUCTS_LIST, menuSelect);
                        this.daoHelper.insert(this.orderDao, productMenuMap);
                    }
                    er.setMessage("booking´s id: " + bookingId);
                    er.setCode(EntityResult.OPERATION_SUCCESSFUL);
                    return er;
                }
            }
        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult checkOrder(Map<String, Object> req) throws OntimizeJEERuntimeException {

        Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
        int orderId = (int) filter.get(OrderDao.ATTR_ID);
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put(OrderDao.ATTR_ID, orderId);
        EntityResult orderExists = this.daoHelper.query(this.orderDao, orderMap, List.of(OrderDao.ATTR_ID, OrderDao.ATTR_CHECKED));
        EntityResult er = new EntityResultMapImpl();

        if(!orderExists.isEmpty()){

            boolean checked = (boolean) ((List<?>) orderExists.get(OrderDao.ATTR_CHECKED)).get(0);
            if(!checked){

                Map<String, Object> checkMap = new HashMap<>();
                checkMap.put(OrderDao.ATTR_CHECKED, true);
                this.daoHelper.update(this.orderDao, checkMap, filter);
                er.setMessage("Checked = true");
                er.setCode(EntityResult.OPERATION_SUCCESSFUL);
                return er;

            }

        }
        er.setCode(EntityResult.OPERATION_WRONG);
        er.setMessage("This order does not exists or it's already checked");
        return er;

    }
}
