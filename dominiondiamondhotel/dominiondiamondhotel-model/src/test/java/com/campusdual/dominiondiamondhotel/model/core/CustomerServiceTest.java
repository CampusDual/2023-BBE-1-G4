package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.dominiondiamondhotel.model.core.service.CustomerService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    CustomerService customerService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    CustomerDao customerDao;
    @Mock
    IdDocumentTypesDao idDocumentTypesDao;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CustomerServiceQuery {
        @Test
        void testCustomerQuery() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(CustomerDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = customerService.customerQuery(new HashMap<>(), List.of(CustomerDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(CustomerDao.class), anyMap(), anyList());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CustomerServiceInsert {
        @Test
        void testCustomerInsert() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(CustomerDao.ATTR_IDTYPE_ID, List.of(2));
            EntityResult idDocAlreadyExists = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put(CustomerDao.ATTR_ID, nullList);
            Map<String, Object> customerToInsert = new HashMap<>();
            customerToInsert.put(CustomerDao.ATTR_NAME, "Prueba");
            customerToInsert.put(CustomerDao.ATTR_LASTNAME1, "PruebaLastName");
            customerToInsert.put(CustomerDao.ATTR_EMAIL, "prueba@gmail.com");
            customerToInsert.put(CustomerDao.ATTR_IDNUMBER, "34305361X");
            customerToInsert.put(CustomerDao.ATTR_IDTYPE_ID, 2);
            customerToInsert.put(CustomerDao.ATTR_PHONE, 644257396);
            when(daoHelper.query(any(IdDocumentTypesDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.query(any(CustomerDao.class), anyMap(), anyList())).thenReturn(idDocAlreadyExists);
            when(daoHelper.insert(any(CustomerDao.class), anyMap())).thenReturn(er);
            EntityResult result = customerService.customerInsert(customerToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(IdDocumentTypesDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(CustomerDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).insert(any(CustomerDao.class), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CustomerServiceUpdate {
        @Test
        void testCustomerUpdate() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(CustomerDao.ATTR_IDTYPE_ID, List.of(2));
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put(CustomerDao.ATTR_ID, List.of(1));
            data.put(CustomerDao.ATTR_IDNUMBER, "47407434H");
            filter.put(CustomerDao.ATTR_EMAIL, "prueba@gmail.com");
            filter.put(CustomerDao.ATTR_PHONE, "644257396");
            when(daoHelper.query(any(CustomerDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.update(any(CustomerDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult result = customerService.customerUpdate(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(CustomerDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(CustomerDao.class), anyMap(), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CustomerServiceDelete {
        @Test
        void testCustomerDelete() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> filter = new HashMap<>();
            filter.put(CustomerDao.ATTR_ID, List.of(1));
            when(daoHelper.delete(any(CustomerDao.class), anyMap())).thenReturn(er);
            EntityResult result = customerService.customerDelete(filter);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).delete(any(CustomerDao.class), anyMap());
        }
    }
}
