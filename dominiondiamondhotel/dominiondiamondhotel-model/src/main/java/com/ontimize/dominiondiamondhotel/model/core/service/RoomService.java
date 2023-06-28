package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IRoomService;
import com.ontimize.dominiondiamondhotel.model.core.dao.HistRoomDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.BasicExpressionUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.INVALID_DATA;
import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByHotelId;
import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByStatus;

@Lazy
@Service("RoomService")
public class RoomService implements IRoomService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private HotelService hotelService;

    @Autowired
    HistRoomDao histRoomDao;

    @Override
    public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        int number = Integer.parseInt(String.valueOf(attrMap.get(RoomDao.ATTR_NUMBER)));
        Map<String, Object> roomKeyMap = new HashMap<>();
        roomKeyMap.put(RoomDao.ATTR_NUMBER, number);
        EntityResult roomNumberAlreadyExists = this.daoHelper.query(this.roomDao, roomKeyMap, List.of(RoomDao.ATTR_ID));
        EntityResult er = new EntityResultMapImpl();
        if (roomNumberAlreadyExists.get("id") == null) {
            int hotelId = Integer.parseInt(String.valueOf(attrMap.get(RoomDao.ATTR_HOTEL_ID)));
            Map<String, Object> key = new HashMap<>();
            SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, HotelDao.ATTR_ID, String.valueOf(hotelId));
            if (be != null) {
                key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
            }
            EntityResult hotelExists = this.hotelService.hotelQuery(key, HotelDao.getColumns());
            if (hotelExists.get(HotelDao.ATTR_ID) != null) {
                Map<String, Object> hotelAddRoomData = new HashMap<>();
                Map<String, Object> hotelAddRoomKey = new HashMap<>();
                int hotelRooms = Integer.parseInt(String.valueOf(((List<?>) hotelExists.get(HotelDao.ATTR_TOTALROOMS)).get(0)));
                hotelAddRoomData.put(HotelDao.ATTR_TOTALROOMS, hotelRooms + 1);
                hotelAddRoomKey.put(HotelDao.ATTR_ID, hotelId);
                EntityResult hotelAddRoom = this.hotelService.hotelUpdate(hotelAddRoomData, hotelAddRoomKey);
                if (hotelAddRoom != null) {
                    return this.daoHelper.insert(this.roomDao, attrMap);
                }
            }
        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult roomUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        EntityResult roomUpdated = this.daoHelper.update(this.roomDao, attrMap, keyMap);
        if (roomUpdated.getCode() == EntityResult.OPERATION_SUCCESSFUL){
        boolean stateIdChange = attrMap.get(RoomDao.ATTR_STATE_ID) != null;
            if (stateIdChange) {
                Map<String, Object> histKeyMap = new HashMap<>();
                histKeyMap.put(HistRoomDao.ATTR_CHANGE_DATE, LocalDate.now());
                histKeyMap.put(HistRoomDao.ATTR_STATE_ID, attrMap.get(RoomDao.ATTR_STATE_ID));
                histKeyMap.put(HistRoomDao.ATTR_ROOM_ID, keyMap.get(RoomDao.ATTR_ID));
                this.daoHelper.insert(this.histRoomDao, histKeyMap);
            }
        }
        return roomUpdated;
    }

    @Override
    public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        EntityResult roomInfo = this.daoHelper.query(this.roomDao, keyMap, RoomDao.getColumns());
        int hotelId = Integer.parseInt(String.valueOf(((List<?>) (roomInfo.get(RoomDao.ATTR_HOTEL_ID))).get(0)));
        Map<String, Object> hotelKeyQuery = new HashMap<>();
        hotelKeyQuery.put(HotelDao.ATTR_ID, hotelId);
        EntityResult hotelInfo = this.hotelService.hotelQuery(hotelKeyQuery, HotelDao.getColumns());
        int hotelRooms = Integer.parseInt(String.valueOf(((List<?>) hotelInfo.get(HotelDao.ATTR_TOTALROOMS)).get(0)));
        Map<String, Object> hotelRoomData = new HashMap<>();
        Map<String, Object> hotelRoomKey = new HashMap<>();
        hotelRoomData.put(HotelDao.ATTR_TOTALROOMS, hotelRooms - 1);
        hotelRoomKey.put(HotelDao.ATTR_ID, hotelId);
        EntityResult hotelAddRoom = this.hotelService.hotelUpdate(hotelRoomData, hotelRoomKey);
        if (hotelAddRoom != null) {
            return this.daoHelper.delete(this.roomDao, keyMap);
        }
        EntityResult er = new EntityResultMapImpl();
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult getRoomByIdQuery(Map<String, Object> keyMap, List<?> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult getRoomByHotelIdQuery(Map<String, Object> keyMap, List<?> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult getRoomByHotelIdAndStatusQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
        Map<?, ?> filter = (Map<?, ?>) req.get(FILTER);
        int hotelId = (int) filter.get(RoomDao.ATTR_HOTEL_ID);
        Map<String, Object> key = new HashMap<>();
        key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
                new SQLStatementBuilder.BasicExpression(searchByHotelId(hotelId), SQLStatementBuilder.BasicOperator.AND_OP, searchByStatus(1)));
        return this.daoHelper.query(this.roomDao, key, List.of(RoomDao.ATTR_ID, RoomDao.ATTR_NUMBER));
    }

    @Override
    public EntityResult cleaningManagement(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> filter = (Map<?, ?>) keyMap.get(FILTER);
        int roomId = (int) filter.get(RoomDao.ATTR_ID);
        Map<String, Object> roomIdKeyMap = new HashMap<>();
        roomIdKeyMap.put(RoomDao.ATTR_ID, roomId);
        EntityResult roomExists = this.daoHelper.query(this.roomDao, roomIdKeyMap, List.of(RoomDao.ATTR_ID, RoomDao.ATTR_STATE_ID));
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) roomExists.get(RoomDao.ATTR_ID)).get(0) != null && Integer.parseInt(String.valueOf(((List<?>) roomExists.get(RoomDao.ATTR_STATE_ID)).get(0))) == 4) {
            Map<String, Object> roomUpdateFilter = new HashMap<>();
            Map<String, Object> roomUpdateData = new HashMap<>();
            roomUpdateFilter.put(RoomDao.ATTR_ID, roomId);
            roomUpdateData.put(RoomDao.ATTR_STATE_ID, 1);
            roomUpdate(roomUpdateData, roomUpdateFilter);
            EntityResult room = this.daoHelper.query(this.roomDao, roomUpdateFilter, List.of(RoomDao.ATTR_ID, RoomDao.ATTR_STATE_ID));
            int roomIdAfterUpdate = Integer.parseInt(String.valueOf(((List<?>) roomExists.get(RoomDao.ATTR_ID)).get(0)));
            String roomStatus = String.valueOf(((List<?>) room.get(RoomDao.ATTR_STATE_ID)).get(0));
            er.setMessage("Id de la habitación: " + roomIdAfterUpdate + " Estado de la habitación: " + roomStatus);
        } else {
            er.setMessage("Invalid data");
            er.setCode(EntityResult.OPERATION_WRONG);
        }
        return er;
    }
}



