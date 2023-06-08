package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IRoomService;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.COLUMNS;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;
import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByHotelId;

@RestController
@RequestMapping("/rooms")
public class RoomRestController extends ORestController<IRoomService> {
    @Autowired
    private IRoomService roomService;

    @Override
    public IRoomService getService() {
        return this.roomService;
    }

    @GetMapping(value = "managerGetRoom", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult managerGetRoom(@RequestBody Map<String, Object> req) {
        try {
            Map<?, ?> filter = (Map<?, ?>) req.get(FILTER);
            int id = (int) filter.get("id");
            Map<String, Object> key = new HashMap<>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchById(id));
            return roomService.getRoomByIdQuery(key, (List<?>) req.get(COLUMNS));
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    private SQLStatementBuilder.BasicExpression searchById(int id) {
        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(RoomDao.ATTR_ID);
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.EQUAL_OP, id);
    }

    @GetMapping(value = "getRoomFromHotelId", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult roomFromHotelId(@RequestBody Map<String, Object> req) {
        try {
            Map<?, ?> filter = (Map<?, ?>) req.get(FILTER);
            int id = Integer.parseInt(String.valueOf(filter.get(RoomDao.ATTR_HOTEL_ID)));
            Map<String, Object> key = new HashMap<>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchByHotelId(id));
            return roomService.getRoomByHotelIdQuery(key, (List<?>) req.get(COLUMNS));
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @GetMapping(value = "assignRoom", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult assignRoom(@RequestBody Map<String, Object> req) {
        try {
            EntityResult er = roomService.getRoomByHotelIdAndStatusQuery(req);
            EntityResult result = new EntityResultMapImpl();
            result.put(RoomDao.ATTR_ID, ((List<?>) er.get(RoomDao.ATTR_ID)).get(0));
            result.put(RoomDao.ATTR_NUMBER, ((List<?>) er.get(RoomDao.ATTR_NUMBER)).get(0));
            return result;
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @PutMapping(value = "cleaningManagement", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult cleaningManagement(@RequestBody Map<String, Object> req) {
        try {
            return this.roomService.cleaningManagement(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
}
