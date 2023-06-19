package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.PostalCodeDao;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.checkerframework.checker.units.qual.A;
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
class HotelServiceTest {
    @InjectMocks
    HotelService hotelService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    HotelDao hotelDao;
    @Mock
    PostalCodeDao postalCodeDao;
    @Mock
    BookingDao bookingDao;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HotelServiceQuery {
        @Test
        void testHotelQuery() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(HotelDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(new HashMap<>(), List.of(HotelDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class), anyMap(), anyList());
        }

        @Test
        void hotelByName() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_NAME, List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> hotelbyName = new HashMap<>();
            hotelbyName.put(HotelDao.ATTR_NAME, null);
            when(daoHelper.query(any(HotelDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(hotelbyName, List.of(HotelDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class), anyMap(), anyList());
        }

        @Test
        void hotelById() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_ID, List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> hotelbyId = new HashMap<>();
            hotelbyId.put(HotelDao.ATTR_ID, 1);
            when(daoHelper.query(any(HotelDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = hotelService.hotelQuery(hotelbyId, List.of(HotelDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(HotelDao.class), anyMap(), anyList());
        }

        @Test
        void hotelOccupation() {
            EntityResult hotelExists = new EntityResultMapImpl();
            hotelExists.setCode(EntityResult.OPERATION_SUCCESSFUL);
            hotelExists.put(HotelDao.ATTR_ID, List.of(1));
            hotelExists.put(HotelDao.ATTR_TOTALROOMS, List.of(10));
            EntityResult bookings = new EntityResultMapImpl();
            bookings.setCode(EntityResult.OPERATION_SUCCESSFUL);
            bookings.put("entry_date", List.of("2023-06-13", "2023-06-30", "2023-07-30", "2023-07-30"));
            bookings.put("exit_date", List.of("2023-06-22", "2023-07-03", "2023-09-03", "2023-09-03"));
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_HOTEL_ID, 10);
            filter.put("year", 2023);
            Map<String, Object> keyMap = new HashMap<>();
            keyMap.put("filter", filter);
            when(daoHelper.query(any(HotelDao.class), anyMap(), anyList())).thenReturn(hotelExists);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookings);
            EntityResult callResult = hotelService.hotelOccupationQuery(keyMap);
            Assertions.assertEquals(EntityResult.OPERATION_SUCCESSFUL, callResult.getCode());
            Assertions.assertEquals(1.095890410958904, ((Map<?, ?>)(callResult.get("data"))).get("occupation"));
        }
    }

    @Test
    void hotelByZip() {
        EntityResult er = new EntityResultMapImpl();
        er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        er.put(HotelDao.ATTR_ZIP_ID, List.of(1));
        er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        Map<String, Object> hotelbyZip = new HashMap<>();
        hotelbyZip.put("zip_id", 1);
        when(daoHelper.query(any(HotelDao.class), anyMap(), anyList())).thenReturn(er);
        EntityResult result = hotelService.hotelQuery(hotelbyZip, List.of(HotelDao.ATTR_ID));
        Assertions.assertEquals(0, result.getCode());
        verify(daoHelper, times(1)).query(any(HotelDao.class), anyMap(), anyList());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HotelServiceInsert {
        @Test
        void testHotelInsert() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_ID, List.of(2));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put(HotelDao.ATTR_ID, nullList);
            Map<String, Object> hotelToInsert = new HashMap<>();
            hotelToInsert.put(HotelDao.ATTR_NAME, "Asier");
            hotelToInsert.put(HotelDao.ATTR_ZIP_ID, 1);
            when(daoHelper.insert(any(HotelDao.class), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelInsert(hotelToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(HotelDao.class), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HotelServiceUpdate {
        @Test
        void testHotelUpdate() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_ID, List.of(2));
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put(HotelDao.ATTR_ID, List.of(1));
            filter.put(HotelDao.ATTR_NAME, "Asier");
            filter.put(HotelDao.ATTR_ZIP_ID, 1);
            when(daoHelper.update(any(HotelDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelUpdate(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).update(any(HotelDao.class), anyMap(), anyMap());
        }

        @Test
        void testHotelCalificate(){
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_RATING, 10);
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            filter.put(HotelDao.ATTR_ID, List.of(1));
            data.put(HotelDao.ATTR_RATING, 10);
            when(daoHelper.update(any(HotelDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult callCalificationUpdate = hotelService.hotelUpdate(filter, data);
            Assertions.assertEquals(0, callCalificationUpdate.getCode());
            verify(daoHelper, times(1)).update(any(HotelDao.class), anyMap(), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HotelServiceDelete {
        @Test
        void testHotelDelete() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> filter = new HashMap<>();
            filter.put(HotelDao.ATTR_ID, List.of(1));
            when(daoHelper.delete(any(HotelDao.class), anyMap())).thenReturn(er);
            EntityResult result = hotelService.hotelDelete(filter);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).delete(any(HotelDao.class), anyMap());
        }
    }


}
