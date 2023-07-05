package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
import com.ontimize.dominiondiamondhotel.model.core.service.ProductService;
import com.ontimize.jee.common.db.AdvancedEntityResult;
import com.ontimize.jee.common.db.AdvancedEntityResultMapImpl;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.dominiondiamondhotel.model.core.service.ProductTypeService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import java.util.ArrayList;
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
    @Mock
    ProductTypeService productTypeService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ProductServiceInsert {
        @Test
        void addProductTest(){
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put(ProductDao.ATTR_NAME, "potato");
            data.put(ProductDao.ATTR_DESCRIPTION, "a real potato");
            data.put(ProductDao.ATTR_PRODUCTTYPE_ID, 4);
            data.put(ProductDao.ATTR_PRICE, 1);
            filter.put("filter", data);
            EntityResult productTypeER = new EntityResultMapImpl();
            productTypeER.put(ProductTypeDao.ATTR_ID, List.of(1));
            EntityResult allergenTypeER = new EntityResultMapImpl();
            allergenTypeER.put(AllergensDao.ATTR_ID, List.of(1));
            EntityResult productER = new EntityResultMapImpl();
            productER.put(ProductDao.ATTR_ID,1);
            EntityResult productAllergensER = new EntityResultMapImpl();
            productAllergensER.put(ProductsAllergensDao.ATTR_ID, List.of(1));
            when(productTypeService.productTypeQuery(anyMap(), anyList())).thenReturn(productTypeER);
            when(daoHelper.insert(any(ProductDao.class), anyMap())).thenReturn(productER);
            EntityResult result = productService.productInsert(filter);
            Assertions.assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
            verify(productTypeService, times(1)).productTypeQuery(anyMap(),anyList());
            verify(daoHelper, times(1)).insert(any(ProductDao.class),anyMap());
        }
    }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class ProductPaginationQuery{

            @Test
            void testProductPaginationQuery(){

//                Map<String, Object> data = new HashMap<>();
//                data.put("producttype_id", 1);
//                List<String> columnsList= new ArrayList<>();
//                columnsList.add("id");
//                columnsList.add("name");
//                columnsList.add("description");
//                columnsList.add("producttype_id");
//                columnsList.add("allergens_id");
//                columnsList.add("price");
//                List<SQLStatementBuilder.SQLOrder> orderby = new ArrayList<>();
//                orderby.add(new SQLStatementBuilder.SQLOrder("price", true));
//                AdvancedEntityResult er = new AdvancedEntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,EntityResult.type);
//                er.put(ProductDao.ATTR_ID, List.of(1));
//                er.put(ProductDao.ATTR_NAME, "Coca-Cola 1L");
//                er.put(ProductDao.ATTR_DESCRIPTION,  "A bottle of Coca-Cola 1L");
//                er.put(ProductDao.ATTR_PRODUCTTYPE_ID,  List.of(1));
//                er.put(ProductDao.ATTR_ALLERGENS_ID,  List.of(1));
//                er.put(ProductDao.ATTR_PRICE,List.of(3.5));
//                when(daoHelper.paginationQuery(any(ProductDao.class),anyMap(),anyList(), anyInt(), anyInt(), anyList(), anyString()))
//                        .thenReturn(er);
//                EntityResult result = productService.productPaginationQuery(data, columnsList, -1,-1, orderby);
//                Assertions.assertEquals(0, result.getCode());
//                verify(daoHelper, times(1)).paginationQuery(any(ProductDao.class), anyMap(),anyList(), anyInt(), anyInt(), anyList(), anyString());

            }
        }

    


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ProductMenuGenerator {
        @Test
        void productMenuTest(){
            EntityResult productTypeER = new EntityResultMapImpl();
            productTypeER.put(ProductTypeDao.ATTR_ID, List.of(1));
            EntityResult productER = new EntityResultMapImpl();
            productER.put(ProductDao.ATTR_NAME, List.of("1", "2", "3", "4", "5"));
            EntityResult productUpdateER = new EntityResultMapImpl();
            productTypeER.put(ProductDao.ATTR_ID, List.of(1));
            when(productTypeService.productTypeQuery(anyMap(), anyList())).thenReturn(productTypeER);
            when(daoHelper.query(any(ProductDao.class), anyMap(), anyList())).thenReturn(productER);
            when(daoHelper.update(any(ProductDao.class), anyMap(), anyMap())).thenReturn(productUpdateER);
            EntityResult result = productService.getMenuQuery();
            Assertions.assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
            verify(productTypeService, times(3)).productTypeQuery(anyMap(),anyList());
            verify(daoHelper, times(3)).query(any(ProductDao.class),anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(ProductDao.class),anyMap(), anyMap());
        }

        @Test
        void varietyMenuTest(){

            EntityResult productTypeER = new EntityResultMapImpl();
            productTypeER.put(ProductTypeDao.ATTR_ID, List.of(1));
            when(productTypeService.productTypeQuery(anyMap(), anyList())).thenReturn(productTypeER);
            EntityResult er = new EntityResultMapImpl();
            er.put(ProductDao.ATTR_NAME, List.of("1", "2", "3", "4", "5"));
            EntityResult productUpdateER = new EntityResultMapImpl();
            productTypeER.put(ProductDao.ATTR_ID, List.of(1));
            when(daoHelper.query(any(ProductDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.update(any(ProductDao.class), anyMap(), anyMap())).thenReturn(productUpdateER);
            EntityResult result = productService.getVarietyMenusQuery();
            Assertions.assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
            verify(productTypeService, times(11)).productTypeQuery(anyMap(),anyList());
            verify(daoHelper, times(11)).query(any(ProductDao.class),anyMap(),anyList());
            verify(daoHelper, times(4)).update(any(ProductDao.class),anyMap(),anyMap());


        }
    }
}
