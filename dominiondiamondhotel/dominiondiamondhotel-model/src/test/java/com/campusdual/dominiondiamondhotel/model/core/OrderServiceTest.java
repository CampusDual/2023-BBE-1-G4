package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
import com.ontimize.dominiondiamondhotel.model.core.service.OrderService;
import com.ontimize.dominiondiamondhotel.model.core.service.ProductService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.DATA;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    OrderDao orderDao;
    @Mock
    BookingService bookingService;
    @Mock
    ProductService productService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AddOrder {
        @Test
        void addOrderTest(){
            Map<String, Object> req = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, 1);
            Map<String, Object> data = new HashMap<>();
            List<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            data.put("products", arrayList);
            req.put(FILTER, filter);
            req.put(DATA, data);
            List<?> arrayList1 = new ArrayList<>();
            req.put("products_list", arrayList1);
            EntityResult bookingER = new EntityResultMapImpl();
            bookingER.put(BookingDao.ATTR_ID, List.of(1));
            EntityResult productER = new EntityResultMapImpl();
            productER.put(ProductDao.ATTR_PRODUCTTYPE_ID, List.of(1));
            EntityResult orderER = new EntityResultMapImpl();
            orderER.put(OrderDao.ATTR_ID, List.of(1));
            when(bookingService.bookingQuery(anyMap(),anyList())).thenReturn(bookingER);
            when(productService.productQuery(anyMap(),anyList())).thenReturn(productER);
            when(daoHelper.insert(any(OrderDao.class), anyMap())).thenReturn(orderER);
            EntityResult result = orderService.orderFood(req);
            Assertions.assertEquals(0, result.getCode());
            verify(bookingService, times(1)).bookingQuery(anyMap(),anyList());
            verify(productService, times(arrayList.size())).productQuery(anyMap(),anyList());
            verify(daoHelper, times(arrayList.size())).insert(any(OrderDao.class),anyMap());
        }

    }
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CheckOrder {
        @Test
        void checkOrderTest(){
            Map<String, Object> req = new HashMap<>();
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put(OrderDao.ATTR_ID, 7);
            req.put(FILTER, orderMap);
            EntityResult orderER = new EntityResultMapImpl();
            orderER.put(OrderDao.ATTR_ID, List.of(1));
            orderER.put(OrderDao.ATTR_CHECKED, List.of(false));
            EntityResult orderUpdateER = new EntityResultMapImpl();
            orderUpdateER.put(OrderDao.ATTR_ID, List.of(1));
            when(daoHelper.query(any(OrderDao.class),anyMap(),anyList())).thenReturn(orderER);
            when(daoHelper.update(any(OrderDao.class),anyMap(), anyMap())).thenReturn(orderUpdateER);
            EntityResult result = orderService.checkOrder(req);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(OrderDao.class),anyMap(),anyList());
            verify(daoHelper, times(1)).update(any(OrderDao.class),anyMap(),anyMap());
        }
    }
}
