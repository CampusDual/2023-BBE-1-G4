package com.ontimize.dominiondiamondhotel.ws.core.rest;

import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.service.BookingService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.DATA;
import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;

@RestController
@RequestMapping("/bookings")
public class BookingRestController extends ORestController<IBookingService> {
    @Autowired
    private IBookingService bookingService;

    @Override
    public IBookingService getService() {
        return this.bookingService;
    }

    @PutMapping(value = "bookingCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult bookingCheck(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.bookingCheckInUpdate(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @PutMapping(value = "bookingCheckOut", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult bookingCheckOut(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.bookingCheckOutUpdate(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @PutMapping(value = "calification", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult addCalificationsAndComments(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.bookingCalificationsAndCommentUpdate(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @GetMapping(value = "forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult getForecast(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.getForecastQuery((Map<String, Object>) req.get("filter"), (List<String>) req.get("columns"));
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }

    @PutMapping(value = "expenses", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult updateExpenses(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.bookingExpenseUpdate(req);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }


    @PutMapping(value = "payExpenses", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult payExpenses(@RequestBody Map<String, Object> req) {
        try {
            Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
            Map<String, Object> data = (Map<String, Object>) req.get(DATA);
            return this.bookingService.payExpenses(filter, data);
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
  
   @GetMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityResult getEvents(@RequestBody Map<String, Object> req) {
        try {
            return this.bookingService.getEventsQuery((Map<String, Object>) req.get("filter"), BookingDao.getColumns());
        } catch (Exception e) {
            EntityResult res = new EntityResultMapImpl();
            res.setCode(EntityResult.OPERATION_WRONG);
            return res;
        }
    }
}
