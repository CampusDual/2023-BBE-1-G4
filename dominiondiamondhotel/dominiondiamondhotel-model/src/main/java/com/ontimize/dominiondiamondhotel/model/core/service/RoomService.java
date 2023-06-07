package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IRoomService;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByHotelId;
import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByStatus;

@Lazy
@Service("RoomService")
public class RoomService implements IRoomService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private RoomDao roomDao;

    @Override
    public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        int number = Integer.parseInt(String.valueOf(attrMap.get("number")));
        Map<String, Object> roomKeyMap = new HashMap<>();
        roomKeyMap.put(RoomDao.ATTR_NUMBER, number);
        List<String> roomAttrList = new ArrayList<>();
        roomAttrList.add(RoomDao.ATTR_ID);
        EntityResult roomNumberAlreadyExists = this.daoHelper.query(this.roomDao, roomKeyMap, roomAttrList);
        EntityResult er = new EntityResultMapImpl();
        if (roomNumberAlreadyExists.get("id") == null) {
            return this.daoHelper.insert(this.roomDao, attrMap);
        } else {
            er.setMessage("Invalid data");
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult roomUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.update(this.roomDao, attrMap, keyMap);
    }

    @Override
    public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.delete(this.roomDao, keyMap);
    }

    @Override
    public EntityResult getRoomByIdQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult getRoomByHotelIdQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.roomDao, keyMap, attrList);
    }

    @Override
    public EntityResult getRoomByHotelIdAndStatusQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
        List<String> columnsToShow = new ArrayList<>();
        columnsToShow.add("id");
        columnsToShow.add("number");
        Map<String, Object> filter = (Map<String, Object>) req.get("filter");
        int hotelId = (int) filter.get("hotel_id");
        Map<String, Object> key = new HashMap<String, Object>();
        key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
                new SQLStatementBuilder.BasicExpression(searchByHotelId(hotelId), SQLStatementBuilder.BasicOperator.AND_OP, searchByStatus(1)));
        return this.daoHelper.query(this.roomDao, key, columnsToShow);
    }

    @Override
    public EntityResult cleaningManagement(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<String, Object> filter = (Map<String, Object>) keyMap.get("filter");
        List<String> columns = new ArrayList<>();
        columns.add("id");
        columns.add("state_id");
        int roomId = (int) filter.get("id");
        Map<String, Object> roomIdKeyMap = new HashMap<>();
        roomIdKeyMap.put(RoomDao.ATTR_ID, roomId);
        EntityResult roomExists = this.daoHelper.query(this.roomDao, roomIdKeyMap, columns);
        EntityResult er = new EntityResultMapImpl();
        if(((List<String>) roomExists.get("id")).get(0) != null && Integer.parseInt(String.valueOf(((List<String>)roomExists.get("state_id")).get(0))) == 4){
            Map<String, Object> roomUpdateFilter = new HashMap<>();
            Map<String, Object> roomUpdateData = new HashMap<>();
            roomUpdateFilter.put("id", roomId);
            roomUpdateData.put("state_id", 1);
            roomUpdate(roomUpdateData, roomUpdateFilter);
            List<String> roomAttrList = new ArrayList<>();
            roomAttrList.add(RoomDao.ATTR_ID);
            roomAttrList.add(RoomDao.ATTR_STATE_ID);
            EntityResult room = this.daoHelper.query(this.roomDao, roomUpdateFilter, roomAttrList);
            int roomIdAfterUpdate = Integer.parseInt(String.valueOf(((List<String>)roomExists.get("id")).get(0)));
            String roomStatus = String.valueOf(((List<String>)room.get("state_id")).get(0));
            er.setMessage("Id de la habitación: "+roomIdAfterUpdate+" Estado de la habitación: "+roomStatus);
            return er;

        }else {
            er.setMessage("Invalid data");
            er.setCode(EntityResult.OPERATION_WRONG);
            return er;
        }
    }



}



