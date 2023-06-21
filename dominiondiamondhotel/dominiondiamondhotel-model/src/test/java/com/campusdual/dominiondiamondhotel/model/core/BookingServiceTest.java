package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
import com.ontimize.dominiondiamondhotel.model.core.service.CustomerService;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
import com.ontimize.dominiondiamondhotel.model.core.service.RoomService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static com.ontimize.jee.common.dto.EntityResult.OPERATION_WRONG;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @InjectMocks
    BookingService bookingService;
    @Mock
    RoomService roomService;
    @Mock
    HotelService hotelService;
    @Mock
    CustomerService customerService;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceInsert {
        @Test
        void testBookingInsertAssertsSuccesful() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put(BookingDao.ATTR_ENTRY_DATE, LocalDate.now().plusDays(10));
            bookingToInsert.put(BookingDao.ATTR_EXIT_DATE, LocalDate.now().plusDays(15));
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
        }

        @Test
        void testBookingInsertAssertsWrong() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(OPERATION_WRONG);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put(BookingDao.ATTR_ENTRY_DATE, LocalDate.now().plusDays(10));
            bookingToInsert.put(BookingDao.ATTR_EXIT_DATE, LocalDate.now().plusDays(17));
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            Assertions.assertEquals(OPERATION_WRONG, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceCheck {
        @Test
        void testBookingCheckIn() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(BookingDao.ATTR_ID, List.of(1));
            er.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            er.put(BookingDao.ATTR_CUSTOMER_ID, List.of(1));
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put("check_out", nullList);
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> sqltypes = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, 1);
            filter.put(CustomerDao.ATTR_IDNUMBER, "47407434H");
            sqltypes.put(BookingDao.ATTR_ID, 12);
            sqltypes.put(BookingDao.ATTR_CHECK_IN, 91);
            bookingToInsert.put(FILTER, filter);
            bookingToInsert.put("sqltypes", sqltypes);
            EntityResult erCustomer = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erCustomer.put(BookingDao.ATTR_ID, List.of(1));
            erCustomer.put(CustomerDao.ATTR_IDNUMBER, List.of("47407434H"));
            EntityResult erRoom = new EntityResultMapImpl();
            erRoom.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erRoom.put("id", List.of(1));
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er);
            when(roomService.getRoomByHotelIdAndStatusQuery(anyMap())).thenReturn(erRoom);
            when(customerService.customerQuery(anyMap(), anyList())).thenReturn(erCustomer);
            EntityResult result = bookingService.bookingCheckInUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(3)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            verify(customerService, times(1)).customerQuery(anyMap(), anyList());
            verify(roomService, times(1)).roomUpdate(anyMap(), anyMap());
        }

        @Test
        void testBookingCheckOut() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(BookingDao.ATTR_ID, List.of(1));
            er.put(BookingDao.ATTR_ROOM_ID, List.of(1));
            er.put(BookingDao.ATTR_CHECK_IN, List.of(LocalDate.now().plusDays(10)));
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put(BookingDao.ATTR_CHECK_OUT, nullList);
            EntityResult er2 = new EntityResultMapImpl();
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> sqltypes = new HashMap<>();
            sqltypes.put(BookingDao.ATTR_ID, 12);
            filter.put(BookingDao.ATTR_ID, 1);
            sqltypes.put(BookingDao.ATTR_CHECK_OUT, 91);
            bookingToInsert.put("filter", filter);
            bookingToInsert.put("sqltypes", sqltypes);
            EntityResult roomUpdate = new EntityResultMapImpl();
            roomUpdate.put(RoomDao.ATTR_STATE_ID, List.of(1));
            roomUpdate.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(roomUpdate);
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er2);
            EntityResult result = bookingService.bookingCheckOutUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceUpdate {
        @Test
        void testBookingCalifications() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            EntityResult customerExists = new EntityResultMapImpl();
            customerExists.put(CustomerDao.ATTR_ID, List.of(1));
            customerExists.put(CustomerDao.ATTR_IDNUMBER, List.of(1));
            EntityResult bookingExists = new EntityResultMapImpl();
            bookingExists.put(BookingDao.ATTR_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_CUSTOMER_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_CHECK_OUT, List.of(1));
            EntityResult hotelExists = new EntityResultMapImpl();
            hotelExists.put("hotelmean", List.of(10));
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put(BookingDao.ATTR_CLEANING, 10);
            bookingData.put(BookingDao.ATTR_FACILITIES, 10);
            bookingData.put(BookingDao.ATTR_PRICEQUALITY, 10);
            EntityResult bookingWithMean = new EntityResultMapImpl();
            Map<String, Object> bookingFilter = new HashMap<>();
            bookingFilter.put(BookingDao.ATTR_ID, 1);
            bookingFilter.put(CustomerDao.ATTR_IDNUMBER, 1);
            bookingToInsert.put("data", bookingData);
            bookingToInsert.put("filter", bookingFilter);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingExists, bookingWithMean);
            when(customerService.customerQuery(anyMap(), anyList())).thenReturn(customerExists);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList(), anyString())).thenReturn(hotelExists);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(er);
            when(hotelService.hotelUpdate(anyMap(), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingCalificationsAndCommentUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList(), anyString());
            verify(customerService, times(1)).customerQuery( anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            verify(hotelService, times(1)).hotelUpdate(anyMap(), anyMap());
        }
    }
}
