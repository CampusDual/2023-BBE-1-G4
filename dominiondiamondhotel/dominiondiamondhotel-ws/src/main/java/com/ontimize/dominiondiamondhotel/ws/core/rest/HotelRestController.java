package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IHotelService;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotels")
public class HotelRestController extends ORestController<IHotelService> {
    @Autowired
    private IHotelService hotelService;

    @Override
    public IHotelService getService() {
        return this.hotelService;
    }

    @RequestMapping(value = "hotelByName/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult hotelSearchByName(@PathVariable String name) {
        try {
            List<String> columns = new ArrayList<>();
            columns.add("name");
            Map<String, Object> key = new HashMap<String, Object>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchByName(name));
            return hotelService.getHotelByNameQuery(key, columns);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    private BasicExpression searchByName(String name) {
        BasicField pvName = new BasicField("'%" + name.toLowerCase() + "%'");
        BasicField attr = new BasicField("lower(" + HotelDao.ATTR_NAME + ")");
        return new BasicExpression(attr, BasicOperator.LIKE_OP, pvName);
    }

    @RequestMapping(value = "hotelById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult hotelSearchById(@PathVariable int id) {
        try {
            List<String> columns = new ArrayList<>();
            for (Field f : HotelDao.class.getDeclaredFields()) {
                String field = f.toString().substring(f.toString().lastIndexOf("_")+1);
                columns.add(field.toLowerCase());
            }
            Map<String, Object> key = new HashMap<String, Object>();
            key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchById(id));
            return hotelService.getHotelByIdQuery(key, columns);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    private BasicExpression searchById(int id) {
        BasicField attr = new BasicField(HotelDao.ATTR_ID);
        return new BasicExpression(attr, BasicOperator.EQUAL_OP, id);
    }

}
