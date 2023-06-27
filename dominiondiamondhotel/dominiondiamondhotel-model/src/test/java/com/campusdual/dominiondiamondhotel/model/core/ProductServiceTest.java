package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
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
import org.springframework.security.core.parameters.P;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    AllergensDao allergensDao;
    @Mock
    ProductTypeDao productTypeDao;
    @Mock
    ProductDao productDao;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ProductServiceInsert {

        @Test
        void addProductTest(){
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put(ProductDao.ATTR_NAME, "potato");
            data.put(ProductDao.ATTR_ALLERGENS_ID, 1);
            data.put(ProductDao.ATTR_DESCRIPTION, "a real potato");
            data.put(ProductDao.ATTR_PRODUCTTYPE_ID, 4);
            data.put(ProductDao.ATTR_PRICE, 1);
            filter.put("filter", data);
            EntityResult productTypeER = new EntityResultMapImpl();
            productTypeER.put(ProductTypeDao.ATTR_ID, List.of(1));
            EntityResult allergenTypeER = new EntityResultMapImpl();
            allergenTypeER.put(AllergensDao.ATTR_ID, List.of(1));
            EntityResult productER = new EntityResultMapImpl();
            productTypeER.put(ProductDao.ATTR_ID, List.of(1));
            when(daoHelper.query(any(ProductTypeDao.class), anyMap(), anyList())).thenReturn(productTypeER);
            when(daoHelper.query(any(AllergensDao.class), anyMap(), anyList())).thenReturn(allergenTypeER);
            when(daoHelper.insert(any(ProductDao.class), anyMap())).thenReturn(productER);
            EntityResult result = productService.productInsert(filter);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(ProductTypeDao.class),anyMap(),anyList());
            verify(daoHelper, times(1)).query(any(AllergensDao.class),anyMap(),anyList());
            verify(daoHelper, times(1)).insert(any(ProductDao.class),anyMap());


        }

    }
}
