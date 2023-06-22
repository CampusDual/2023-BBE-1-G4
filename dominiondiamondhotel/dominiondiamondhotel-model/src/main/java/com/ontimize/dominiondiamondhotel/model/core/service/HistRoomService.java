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
    private RoomService roomService;

    @Autowired
    private IdDocumentTypesDao idDocumentTypesDao;

    @Override
    public EntityResult histroomQuery(Map<String, Object> filter, List<String> columns) throws OntimizeJEERuntimeException {
        EntityResult roomExists = this.roomService.roomQuery(filter, List.of(RoomDao.ATTR_ID));
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) roomExists.get(RoomDao.ATTR_ID)).get(0) != null){
            Map<String, Object> roomFilter = new HashMap<>();
            roomFilter.put(HistRoomDao.ATTR_ROOM_ID, filter.get(RoomDao.ATTR_ID));
            return this.daoHelper.query(this.histRoomDao, roomFilter,columns, "gethistroom");


        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }
}
