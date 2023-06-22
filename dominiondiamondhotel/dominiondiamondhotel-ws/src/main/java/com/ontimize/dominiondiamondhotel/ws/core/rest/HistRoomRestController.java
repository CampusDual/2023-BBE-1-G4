package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IHistRoomService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/histroom")
public class HistRoomRestController extends ORestController<IHistRoomService> {

    @Autowired
    private IHistRoomService histRoomService;

    @Override
    public IHistRoomService getService() {
        return this.histRoomService;
    }

}
