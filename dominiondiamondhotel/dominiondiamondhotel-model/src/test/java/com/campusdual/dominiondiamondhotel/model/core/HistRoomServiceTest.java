package com.campusdual.dominiondiamondhotel.model.core;

import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HistRoomDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.service.CustomerService;
import com.ontimize.dominiondiamondhotel.model.core.service.HistRoomService;
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
import static org.mockito.ArgumentMatchers.anyList;
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
    IdDocumentTypesDao idDocumentTypesDao;
    @Mock
    RoomDao roomDao;
    @Mock
    RoomService roomService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class getHistRoomTest {
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
            when(daoHelper.query(any(HistRoomDao.class), anyMap(), anyList(),anyString())).thenReturn(histEntityResult);
            EntityResult result = histRoomService.histroomQuery(data, columns);
            Assertions.assertEquals(0, result.getCode());
            verify(roomService, times(1)).roomQuery(anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(HistRoomDao.class), anyMap(), anyList(),anyString());

        }
    }
}
