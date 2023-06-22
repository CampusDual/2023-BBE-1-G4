package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IHistRoomService;
import com.ontimize.dominiondiamondhotel.model.core.dao.HistRoomDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
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

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.INVALID_DATA;

@Lazy
@Service("HistRoomService")
public class HistRoomService implements IHistRoomService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private HistRoomDao histRoomDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private IdDocumentTypesDao idDocumentTypesDao;

    @Override
    public EntityResult getHistRoom(Map<String, Object> filter) throws OntimizeJEERuntimeException {
        Map<?, ?> filterMap = (Map<?, ?>) filter.get(FILTER);
        int roomId = (int) filterMap.get(RoomDao.ATTR_ID);
        Map<String, Object> roomIdKeyMap = new HashMap<>();
        roomIdKeyMap.put(RoomDao.ATTR_ID, roomId);
        EntityResult roomExists = this.daoHelper.query(this.roomDao, roomIdKeyMap, List.of(RoomDao.ATTR_ID));
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) roomExists.get(RoomDao.ATTR_ID)).get(0) != null){
            Map<String, Object> roomFilter = new HashMap<>();
            roomFilter.put(HistRoomDao.ATTR_ROOM_ID, roomId);
            List<SQLStatementBuilder.SQLOrder> orderBy = new ArrayList<>();
            orderBy.add(new SQLStatementBuilder.SQLOrder("change_date", false));
            return this.daoHelper.query(this.histRoomDao, roomFilter, List.of(HistRoomDao.ATTR_ROOM_ID, HistRoomDao.ATTR_CHANGE_DATE, HistRoomDao.ATTR_STATE_ID, HistRoomDao.ATTR_ID), orderBy, "gethistroom");


        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }
}
