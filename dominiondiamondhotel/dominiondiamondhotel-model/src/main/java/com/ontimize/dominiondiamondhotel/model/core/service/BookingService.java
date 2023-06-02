package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Lazy
@Service("BookingService")
public class BookingService implements IBookingService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private RoomDao roomDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private RoomService roomService;

    @Override
    public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        String entry_date = String.valueOf(attrMap.get("entry_date"));
        String exit_date = String.valueOf(attrMap.get("exit_date"));

        EntityResult entityResult = new EntityResultMapImpl();

        if(ValidatorUtils.dateValidator(entry_date,exit_date)) {
            return this.daoHelper.insert(this.bookingDao, attrMap);
        }else{
            entityResult.setMessage("Invalidad date");
            entityResult.setCode(EntityResult.OPERATION_WRONG);
            return entityResult;
        }
    }

    @Override
    public EntityResult bookingCheckUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<String, Object> getFilter = (Map<String, Object>) keyMap.get("filter");
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get("id")));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        List<String> bookingAttrList = new ArrayList<>();
        bookingAttrList.add(BookingDao.ATTR_ID);
        bookingAttrList.add(BookingDao.ATTR_HOTEL_ID);
        bookingAttrList.add(BookingDao.ATTR_ROOM_ID);
        bookingAttrList.add(BookingDao.ATTR_CUSTOMER_ID);
        bookingAttrList.add(BookingDao.ATTR_CHECK_IN);
        bookingAttrList.add(BookingDao.ATTR_EXIT_DATE);
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, bookingAttrList);
        int hotelIdFromBooking = Integer.parseInt(String.valueOf(((List<String>)bookingExists.get("hotel_id")).get(0)));
        int customerId = Integer.parseInt(String.valueOf(((List<String>)bookingExists.get("customer_id")).get(0)));
        String customerIdNumber = String.valueOf(getFilter.get("idnumber"));
        Map<String, Object> customerKeyMap = new HashMap<>();
        customerKeyMap.put(CustomerDao.ATTR_IDNUMBER, customerIdNumber);
        List<String> customerAttrList = new ArrayList<>();
        customerAttrList.add(CustomerDao.ATTR_ID);
        EntityResult customerExists = this.daoHelper.query(this.customerDao, customerKeyMap, customerAttrList);
        int customerExistsId = Integer.parseInt(String.valueOf(((List<String>)customerExists.get("id")).get(0)));
        EntityResult er = new EntityResultMapImpl();
        if (bookingExists.get("id") != null && customerId == customerExistsId) {
            Map<String, Object> req = new HashMap<>();
            Map<String, Object> hotelId = new HashMap<>();
            hotelId.put("hotel_id", hotelIdFromBooking);
            req.put("filter", hotelId);
            Map<String, Object> filter = new HashMap<>();
            filter.put("id", ((List<String>) bookingExists.get("id")).get(0));
            Map<String, Object> data = new HashMap<>();
            data.put("check_in", LocalDate.now());
            EntityResult erRoomId = this.roomService.getRoomByHotelIdAndStatusQuery(req);
            data.put("room_id", ((List<Integer>) erRoomId.get("id")).get(0));
            this.daoHelper.update(this.bookingDao, data, filter);
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, bookingAttrList);
            if (result.getCode() == EntityResult.OPERATION_SUCCESSFUL){
                Map<String, Object> roomUpdateFilter = new HashMap<>();
                Map<String, Object> roomUpdateData = new HashMap<>();
                roomUpdateFilter.put("id", ((List<String>) erRoomId.get("id")).get(0));
                roomUpdateData.put("state_id", 2);
                this.roomService.roomUpdate(roomUpdateData, roomUpdateFilter);
            }
            return this.daoHelper.query(this.bookingDao, bookingIdKeyMap, bookingAttrList);
        } else {
            er.setMessage("Invalid data");
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult bookingCheckOutUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<String, Object> getFilter = (Map<String, Object>) keyMap.get("filter");
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get("id")));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        List<String> bookingAttrList = new ArrayList<>();
        bookingAttrList.add(BookingDao.ATTR_ID);
        bookingAttrList.add(BookingDao.ATTR_ENTRY_DATE);
        bookingAttrList.add(BookingDao.ATTR_HOTEL_ID);
        bookingAttrList.add(BookingDao.ATTR_ROOM_ID);
        bookingAttrList.add(BookingDao.ATTR_CUSTOMER_ID);
        bookingAttrList.add(BookingDao.ATTR_CHECK_IN);
        bookingAttrList.add(BookingDao.ATTR_EXIT_DATE);
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, bookingAttrList);
        EntityResult er = new EntityResultMapImpl();

        if (bookingExists.get("id") != null && ((List<String>) bookingExists.get("check_in")).get(0) != null ) {
            int roomIdFromBooking = Integer.parseInt(String.valueOf(((List<String>)bookingExists.get("room_id")).get(0)));
            Map<String, Object> filter = new HashMap<>();
            filter.put("id", ((List<String>) bookingExists.get("id")).get(0));
            Map<String, Object> data = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();
            data.put("check_out", now);
            this.daoHelper.update(this.bookingDao, data, filter);
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, bookingAttrList);
            if (result.getCode() == EntityResult.OPERATION_SUCCESSFUL){
                Map<String, Object> roomUpdateFilter = new HashMap<>();
                Map<String, Object> roomUpdateData = new HashMap<>();
                roomUpdateFilter.put("id", roomIdFromBooking);
                roomUpdateData.put("state_id", 1);
                this.roomService.roomUpdate(roomUpdateData, roomUpdateFilter);
                List<String> roomAttrList = new ArrayList<>();
                roomAttrList.add(RoomDao.ATTR_ID);
                roomAttrList.add(RoomDao.ATTR_STATE_ID);
                EntityResult room = this.daoHelper.query(this.roomDao, roomUpdateFilter, roomAttrList);
                String roomStatus = String.valueOf(((List<String>)room.get("state_id")).get(0));
                er.setMessage("Fecha de check_out: "+now+" Estado de la habitación: "+roomStatus);
                return er;
            }
        } else {
            er.setMessage("Invalid data");
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;

    }


}
