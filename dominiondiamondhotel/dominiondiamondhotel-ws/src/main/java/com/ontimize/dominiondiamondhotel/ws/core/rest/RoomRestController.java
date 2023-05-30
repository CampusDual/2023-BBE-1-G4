package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IHotelService;
import com.ontimize.dominiondiamondhotel.api.core.service.IRoomService;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomRestController extends ORestController<IRoomService> {
    @Autowired
    private IRoomService roomService;

    @Override
    public IRoomService getService() {
        return this.roomService;
    }

    @RequestMapping(value = "managerGetRoom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult managerGetRoom(@RequestBody Map<String, Object> req) {
        try {
            List<String> columns = (List<String>) req.get("columns");
            Map<String, Object> filter = (Map<String, Object>) req.get("filter");
            int id = (int) filter.get("id");
            Map<String, Object> key = new HashMap<String, Object>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchById(id));
            return roomService.getRoomByIdQuery(key, columns);
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

    @RequestMapping(value = "getRoomFromHotelId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult roomFromHotelId(@RequestBody Map<String, Object> req) {
        try {
            List<String> columns = (List<String>) req.get("columns");
            Map<String, Object> filter = (Map<String, Object>) req.get("filter");
            int id = (int) filter.get("hotel_id");
            Map<String, Object> key = new HashMap<String, Object>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchByHotelId(id));
            return roomService.getRoomByHotelIdQuery(key, columns);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    private SQLStatementBuilder.BasicExpression searchByHotelId(int id) {
        SQLStatementBuilder.BasicField attr = new SQLStatementBuilder.BasicField(RoomDao.ATTR_HOTEL_ID);
        return new SQLStatementBuilder.BasicExpression(attr, SQLStatementBuilder.BasicOperator.EQUAL_OP, id);
    }

}