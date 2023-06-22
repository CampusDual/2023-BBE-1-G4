package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.HistRoomDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.service.HistRoomService;
import com.ontimize.dominiondiamondhotel.model.core.service.HotelService;
import com.ontimize.dominiondiamondhotel.model.core.service.RoomService;
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
public class HistRoomServiceTest {

    @InjectMocks
    HistRoomService histRoomService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    HistRoomDao histRoomDao;
    @Mock
    RoomService roomService;

    @Mock
    HotelService hotelService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HistRoomTestQueries {
        @Test
        void testCustomerQuery() {

            Map<String, Object> data = new HashMap<>();
            data.put(RoomDao.ATTR_ID, 12);
            List<String> columns = new ArrayList<>();
            columns.add("id");
            columns.add("change_date");
            columns.add("room_id");
            columns.add("state_id");
            EntityResult entityResult = new EntityResultMapImpl();
            entityResult.put(RoomDao.ATTR_ID, List.of(1));
            EntityResult histEntityResult = new EntityResultMapImpl();
            histEntityResult.put(HistRoomDao.ATTR_ID, List.of(1));
            histEntityResult.put(HistRoomDao.ATTR_CHANGE_DATE, List.of(1));
            histEntityResult.put(HistRoomDao.ATTR_ROOM_ID, List.of(1));
            histEntityResult.put(HistRoomDao.ATTR_STATE_ID, List.of(1));
            when(roomService.roomQuery(anyMap(), anyList())).thenReturn(entityResult);
            when(daoHelper.query(any(HistRoomDao.class), anyMap(), anyList(), anyString())).thenReturn(histEntityResult);
            EntityResult result = histRoomService.histroomQuery(data, columns);
            Assertions.assertEquals(0, result.getCode());
            verify(roomService, times(1)).roomQuery(anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(HistRoomDao.class), anyMap(), anyList(), anyString());
        }

        @Test
        void testHistRoomByHotelId() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(HotelDao.ATTR_ID, List.of(1));
            Map<String, Object> keyMap = new HashMap<>();
            keyMap.put("hotel_id", 1);
            when(hotelService.hotelQuery(anyMap(), anyList())).thenReturn(er);
            when(daoHelper.query(any(HistRoomDao.class), anyMap(), anyList(), anyString())).thenReturn(er);
            EntityResult histRoomReturn = histRoomService.histRoomByHotelIdQuery(keyMap, HistRoomDao.getColumns());
            Assertions.assertEquals(EntityResult.OPERATION_SUCCESSFUL, histRoomReturn.getCode());
            verify(daoHelper, times(1)).query(any(HistRoomDao.class), anyMap(), anyList(), anyString());
            verify(hotelService, times(1)).hotelQuery(anyMap(), anyList());
        }
    }
}
