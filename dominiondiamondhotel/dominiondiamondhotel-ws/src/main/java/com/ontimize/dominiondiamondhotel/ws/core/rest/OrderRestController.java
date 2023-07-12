package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IOrderService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderRestController extends ORestController<IOrderService> {
    @Autowired
    IOrderService orderService;
    @Override
    public IOrderService getService() {
        return this.orderService;
    }
    @PostMapping(value = "orderFood", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult orderFood(@RequestBody Map<String, Object> req) {
        try {
            return this.orderService.orderFood(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
}
