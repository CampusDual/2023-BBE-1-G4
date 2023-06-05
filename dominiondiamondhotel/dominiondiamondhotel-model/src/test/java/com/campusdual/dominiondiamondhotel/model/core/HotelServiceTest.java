package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.PostalCodeDao;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @InjectMocks
    HotelService hotelService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    HotelDao hotelDao;
    @Mock
    PostalCodeDao postalCodeDao;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelServiceQuery{
        @Test
        void testHotelQuery(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String>attrList = new ArrayList<>();
            attrList.add(HotelDao.ATTR_ID);
            when(daoHelper.query(any(HotelDao.class),anyMap(),anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(new HashMap<>(),attrList);
            Assertions.assertEquals(0,result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class),anyMap(),anyList());
        }
        @Test
        void hotelByName(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put("name",List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String>attrList = new ArrayList<>();
            attrList.add(HotelDao.ATTR_ID);
            Map<String, Object>hotelbyName= new HashMap<>();
            hotelbyName.put("name",null);
            when(daoHelper.query(any(HotelDao.class),anyMap(),anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(hotelbyName,attrList);
            Assertions.assertEquals(0,result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class),anyMap(),anyList());
        }
        @Test
        void hotelById(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put("id",List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String>attrList = new ArrayList<>();
            attrList.add(HotelDao.ATTR_ID);
            Map<String, Object>hotelbyId= new HashMap<>();
            hotelbyId.put("id",1);
            when(daoHelper.query(any(HotelDao.class),anyMap(),anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(hotelbyId,attrList);
            Assertions.assertEquals(0,result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class),anyMap(),anyList());
        }
    }
    @Test
    void hotelByZip(){
        EntityResult er = new EntityResultMapImpl();
        er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        er.put("zip_id",List.of(1));
        er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        List<String>attrList = new ArrayList<>();
        attrList.add(HotelDao.ATTR_ID);
        Map<String, Object>hotelbyZip= new HashMap<>();
        hotelbyZip.put("zip_id",1);
        when(daoHelper.query(any(HotelDao.class),anyMap(),anyList())).thenReturn(er);
        EntityResult result = hotelService.hotelQuery(hotelbyZip,attrList);
        Assertions.assertEquals(0,result.getCode());
        verify(daoHelper, times(1)).query(any(HotelDao.class),anyMap(),anyList());
    }
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelServiceInsert{
        @Test
        void testHotelInsert(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put("id", List.of(2));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put("id", nullList);
            Map<String, Object> hotelToInsert = new HashMap<>();
            hotelToInsert.put("name", "Asier");
            hotelToInsert.put("zip_id", 1);
            when(daoHelper.insert(any(HotelDao.class), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelInsert(hotelToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(HotelDao.class), anyMap());
        }
    }
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelServiceUpdate{
        @Test
        void testHotelUpdate(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put("id", List.of(2));
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("id", List.of(1));
            filter.put("name", "Asier");
            filter.put("zip_id", 1);
            when(daoHelper.update(any(HotelDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelUpdate(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).update(any(HotelDao.class), anyMap(),anyMap());
        }
    }
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelServiceDelete{
        @Test
        void testHotelDelete(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> filter = new HashMap<>();
            filter.put("id", List.of(1));
            when(daoHelper.delete(any(HotelDao.class), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelDelete(filter);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).delete(any(HotelDao.class), anyMap());
        }

    }




}
