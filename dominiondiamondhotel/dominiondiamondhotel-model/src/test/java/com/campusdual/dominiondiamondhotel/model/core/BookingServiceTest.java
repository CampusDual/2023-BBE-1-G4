package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
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
    BookingDao bookingDao;
    @Mock
    CustomerDao customerDao;
    @Mock
    RoomDao roomDao;

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
            when(daoHelper.query(any(CustomerDao.class), anyMap(), anyList())).thenReturn(erCustomer);
            EntityResult result = bookingService.bookingCheckInUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(3)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            verify(daoHelper, times(1)).query(any(CustomerDao.class), anyMap(), anyList());
            verify(roomService, times(1)).roomUpdate(anyMap(), anyMap());
        }

        @Test
        void testBookingCheckOut() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(BookingDao.ATTR_ID, List.of(1));
            er.put(BookingDao.ATTR_ROOM_ID, List.of(1));
            er.put(BookingDao.ATTR_CHECK_IN, List.of(LocalDate.now().plusDays(10)));
            er.put(BookingDao.ATTR_CHECK_OUT, List.of(LocalDate.now().plusDays(15)));
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
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingCheckOutUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
        }
    }
}
