package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
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
public class RoomServiceTest {
    @InjectMocks
    RoomService roomService;
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @Mock
    RoomDao roomDao;
    @Mock
    IdDocumentTypesDao idDocumentTypesDao;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RoomServiceQuery {
        @Test
        void testRoomQuery() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = roomService.roomQuery(new HashMap<>(), List.of(RoomDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(RoomDao.class), anyMap(), anyList());
        }

        @Test
        void roomById() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(RoomDao.ATTR_ID, List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> roombyId = new HashMap<>();
            roombyId.put(RoomDao.ATTR_ID, 1);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = roomService.roomQuery(roombyId, List.of(RoomDao.ATTR_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(RoomDao.class), anyMap(), anyList());
        }

        @Test
        void roomByHotelId() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(RoomDao.ATTR_ID, List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> roombyHotelId = new HashMap<>();
            roombyHotelId.put("hotel_id", 1);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = roomService.roomQuery(roombyHotelId, List.of(RoomDao.ATTR_HOTEL_ID));
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(RoomDao.class), anyMap(), anyList());
        }

        @Test
        void roomByHotelIdAndStatusQuery() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(RoomDao.ATTR_ID, List.of(1));
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String> attrList = new ArrayList<>();
            attrList.add(RoomDao.ATTR_ID);
            Map<String, Object> roombyHotelId = new HashMap<>();
            roombyHotelId.put(RoomDao.ATTR_ID, 1);
            when(roomService.getRoomByHotelIdQuery(roombyHotelId, attrList)).thenReturn(er);
            EntityResult result = roomService.roomQuery(roombyHotelId, attrList);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(RoomDao.class), anyMap(), anyList());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RoomServiceInsert {
        @Test
        void testRoomInsert() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            EntityResult roomAlreadyExists = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put(RoomDao.ATTR_ID, nullList);
            Map<String, Object> roomToInsert = new HashMap<>();
            roomToInsert.put(RoomDao.ATTR_ID, 1);
            roomToInsert.put(RoomDao.ATTR_NUMBER, 666);
            roomToInsert.put(RoomDao.ATTR_HOTEL_ID, 1);
            roomToInsert.put(RoomDao.ATTR_STATE_ID, 1);
            when(daoHelper.insert(any(RoomDao.class), anyMap())).thenReturn(er);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(roomAlreadyExists);
            EntityResult result = roomService.roomInsert(roomToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(RoomDao.class), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RoomServiceUpdate {
        @Test
        void testRoomUpdate() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put(RoomDao.ATTR_ID, List.of(1));
            filter.put(RoomDao.ATTR_ID, 1);
            filter.put(RoomDao.ATTR_NUMBER, 1);
            filter.put(RoomDao.ATTR_HOTEL_ID, 1);
            filter.put(RoomDao.ATTR_STATE_ID, 1);
            when(daoHelper.update(any(RoomDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult result = roomService.roomUpdate(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).update(any(RoomDao.class), anyMap(), anyMap());
        }

        @Test
        void testCleaningManagement() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(RoomDao.ATTR_ID, List.of(1));
            er.put(RoomDao.ATTR_STATE_ID, List.of(1));
            Map<String, Object> roomToUpdate = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> sqltypes = new HashMap<>();
            filter.put(RoomDao.ATTR_ID, 1);
            sqltypes.put(RoomDao.ATTR_ID, 1);
            roomToUpdate.put("filter", filter);
            roomToUpdate.put("sqltypes", sqltypes);
            EntityResult erRoom = new EntityResultMapImpl();
            erRoom.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erRoom.put(RoomDao.ATTR_ID, List.of(1));
            erRoom.put(RoomDao.ATTR_STATE_ID, List.of(4));
            EntityResult erRoomUpdated = new EntityResultMapImpl();
            erRoomUpdated.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erRoomUpdated.put(RoomDao.ATTR_ID, List.of(1));
            erRoomUpdated.put(RoomDao.ATTR_STATE_ID, List.of(1));
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(erRoom, erRoomUpdated);
            when(daoHelper.update(any(RoomDao.class), anyMap(), anyMap())).thenReturn(er);
            EntityResult result = roomService.cleaningManagement(roomToUpdate);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(RoomDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(RoomDao.class), anyMap(), anyMap());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RoomServiceDelete {
        @Test
        void testRoomDelete() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> filter = new HashMap<>();
            filter.put(RoomDao.ATTR_ID, List.of(1));
            when(daoHelper.delete(any(RoomDao.class), anyMap())).thenReturn(er);
            EntityResult result = roomService.roomDelete(filter);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).delete(any(RoomDao.class), anyMap());
        }
    }
}
