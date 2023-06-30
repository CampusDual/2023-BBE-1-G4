package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IProductService;
import com.ontimize.dominiondiamondhotel.api.core.service.IProductTypeService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductRestController extends ORestController<IProductService> {

    @Autowired
    private IProductService productService;

    @Override
    public IProductService getService() {
        return this.productService;
    }

    @PostMapping(value = "addProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult addProduct(@RequestBody Map<String, Object> req) {
        try {
            return this.productService.productInsert(req);
        } catch (Exception e) {
            e.printStackTrace();
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @GetMapping(value = "getMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult getMenu() {
        try {
            return this.productService.getMenuQuery();
        } catch (Exception e) {
            e.printStackTrace();
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
    @GetMapping(value = "getVarietyMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult getVarietyMenu() {
        try {
            return this.productService.getVarietyMenusQuery();
        } catch (Exception e) {
            e.printStackTrace();
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
}
