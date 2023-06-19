package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.RoomDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.BookingUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.*;
import static com.ontimize.dominiondiamondhotel.model.core.utils.RoomUtils.searchByHotelId;

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

    @Autowired
    private HotelDao hotelDao;

    @Override
    public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        String entryDate = String.valueOf(attrMap.get(BookingDao.ATTR_ENTRY_DATE));
        String exitDate = String.valueOf(attrMap.get(BookingDao.ATTR_EXIT_DATE));
        EntityResult entityResult = new EntityResultMapImpl();
        if (ValidatorUtils.dateValidator(entryDate, exitDate)) {
            return this.daoHelper.insert(this.bookingDao, attrMap);
        } else {
            entityResult.setMessage(INVALID_DATA);
            entityResult.setCode(EntityResult.OPERATION_WRONG);
            return entityResult;
        }
    }

    @Override
    public EntityResult bookingCheckInUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get(BookingDao.ATTR_ID)));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, this.bookingDao.getColumns());
        int hotelIdFromBooking = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_HOTEL_ID)).get(0)));
        int customerId = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_CUSTOMER_ID)).get(0)));
        String customerIdNumber = String.valueOf(getFilter.get(CustomerDao.ATTR_IDNUMBER));
        Map<String, Object> customerKeyMap = new HashMap<>();
        customerKeyMap.put(CustomerDao.ATTR_IDNUMBER, customerIdNumber);
        EntityResult customerExists = this.daoHelper.query(this.customerDao, customerKeyMap, List.of(CustomerDao.ATTR_ID));
        int customerExistsId = Integer.parseInt(String.valueOf(((List<?>) customerExists.get(CustomerDao.ATTR_ID)).get(0)));
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0) != null && customerId == customerExistsId && ((List<?>) bookingExists.get(BookingDao.ATTR_CHECK_OUT)).get(0) == null) {
            Map<String, Object> req = new HashMap<>();
            Map<String, Object> hotelId = new HashMap<>();
            hotelId.put(BookingDao.ATTR_HOTEL_ID, hotelIdFromBooking);
            req.put(FILTER, hotelId);
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, ((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0));
            Map<String, Object> data = new HashMap<>();
            data.put(BookingDao.ATTR_CHECK_IN, LocalDateTime.now());
            EntityResult erRoomId = this.roomService.getRoomByHotelIdAndStatusQuery(req);
            data.put(BookingDao.ATTR_ROOM_ID, ((List<?>) erRoomId.get(RoomDao.ATTR_ID)).get(0));
            this.daoHelper.update(this.bookingDao, data, filter);
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, this.bookingDao.getColumns());
            if (result.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
                Map<String, Object> roomUpdateFilter = new HashMap<>();
                Map<String, Object> roomUpdateData = new HashMap<>();
                roomUpdateFilter.put(RoomDao.ATTR_ID, ((List<?>) erRoomId.get(RoomDao.ATTR_ID)).get(0));
                roomUpdateData.put(RoomDao.ATTR_STATE_ID, 2);
                this.roomService.roomUpdate(roomUpdateData, roomUpdateFilter);
            }
            return this.daoHelper.query(this.bookingDao, bookingIdKeyMap, this.bookingDao.getColumns());
        } else {
            er.setMessage(INVALID_DATA);
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult bookingCheckOutUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get(BookingDao.ATTR_ID)));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, this.bookingDao.getColumns());
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0) != null && ((List<?>) bookingExists.get(BookingDao.ATTR_CHECK_OUT)).get(0) == null) {
            int roomIdFromBooking = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_ROOM_ID)).get(0)));
            Map<String, Object> filter = new HashMap<>();
            filter.put("id", ((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0));
            Map<String, Object> data = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();
            data.put(BookingDao.ATTR_CHECK_OUT, now);
            this.daoHelper.update(this.bookingDao, data, filter);
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, this.bookingDao.getColumns());
            if (result.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
                Map<String, Object> roomUpdateFilter = new HashMap<>();
                Map<String, Object> roomUpdateData = new HashMap<>();
                roomUpdateFilter.put(RoomDao.ATTR_ID, roomIdFromBooking);
                roomUpdateData.put(RoomDao.ATTR_STATE_ID, 4);
                updatePopularity(Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_HOTEL_ID)).get(0))));
                this.roomService.roomUpdate(roomUpdateData, roomUpdateFilter);
                EntityResult room = this.daoHelper.query(this.roomDao, roomUpdateFilter, List.of(RoomDao.ATTR_ID, RoomDao.ATTR_STATE_ID));
                String roomStatus = String.valueOf(((List<?>) room.get(RoomDao.ATTR_STATE_ID)).get(0));
                er.setMessage("Fecha de check_out: " + now + " Estado de la habitación: " + roomStatus);
                return er;
            }
        } else {
            er.setMessage(INVALID_DATA);
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult bookingCalificationsAndCommentUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        Map<String, Object> getData = (Map<String, Object>) keyMap.get(DATA);
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get(BookingDao.ATTR_ID)));
        String customerIdNumber = String.valueOf(getFilter.get(CustomerDao.ATTR_IDNUMBER));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        Map<String, Object> customerIdKeyMap = new HashMap<>();
        customerIdKeyMap.put(CustomerDao.ATTR_IDNUMBER, customerIdNumber);
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, List.of(BookingDao.ATTR_ID, BookingDao.ATTR_HOTEL_ID, BookingDao.ATTR_CUSTOMER_ID, BookingDao.ATTR_CHECK_OUT));
        EntityResult customerExists = this.daoHelper.query(this.customerDao, customerIdKeyMap, List.of(CustomerDao.ATTR_ID));
        int bookingCustomerId = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_CUSTOMER_ID)).get(0)));
        int customerExistsId = Integer.parseInt(String.valueOf(((List<?>) customerExists.get(CustomerDao.ATTR_ID)).get(0)));
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0) != null && ((List<?>) bookingExists.get(BookingDao.ATTR_CHECK_OUT)).get(0) != null && bookingCustomerId == customerExistsId) {
            int cleaning = Integer.parseInt(String.valueOf(getData.get(BookingDao.ATTR_CLEANING)));
            int facilities = Integer.parseInt(String.valueOf(getData.get(BookingDao.ATTR_FACILITIES)));
            int pricequality = Integer.parseInt(String.valueOf(getData.get(BookingDao.ATTR_PRICEQUALITY)));
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, bookingId);
            getData.put(BookingDao.ATTR_MEAN, (cleaning + facilities + pricequality) / 3.0);
            if (BookingUtils.calificationCheck(cleaning) &&
                    BookingUtils.calificationCheck(facilities) && BookingUtils.calificationCheck(pricequality) &&
                    this.daoHelper.update(this.bookingDao, getData, filter).getCode() == EntityResult.OPERATION_SUCCESSFUL) {
                hotelRating(Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_HOTEL_ID)).get(0))));
                return this.daoHelper.query(this.bookingDao, bookingIdKeyMap, List.of(BookingDao.ATTR_ID, BookingDao.ATTR_COMM, BookingDao.ATTR_MEAN));
            }
        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    private EntityResult hotelRating(int hotelId){

        Map<String, Object> hotelIdKeyMap = new HashMap<>();
        hotelIdKeyMap.put(BookingDao.ATTR_HOTEL_ID, hotelId);
        hotelIdKeyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, BookingUtils.meanNotNull());
        EntityResult hotelMean = this.daoHelper.query(this.bookingDao, hotelIdKeyMap, List.of("hotelmean"), "meanHotel");
        Double mean = Double.parseDouble(String.valueOf(((List<?>) hotelMean.get("hotelmean")).get(0)));

        Map<String, Object> hotelIdForUpdateKeyMap = new HashMap<>();
        hotelIdForUpdateKeyMap.put(HotelDao.ATTR_ID, hotelId);
        Map<String, Object> getData = new HashMap<>();
        getData.put(HotelDao.ATTR_RATING, mean);
        return this.daoHelper.update(this.hotelDao, getData,hotelIdForUpdateKeyMap);

    }

    private EntityResult updatePopularity(int hotelId){

        Map<String, Object> hotelIdKeyMap = new HashMap<>();
        hotelIdKeyMap.put(BookingDao.ATTR_HOTEL_ID, hotelId);
        EntityResult hotelPopularity = this.daoHelper.query(this.bookingDao, hotelIdKeyMap, List.of("counting"), "countPopularity");
        int count = Integer.parseInt(String.valueOf(((List<?>) hotelPopularity.get("counting")).get(0)));

        Map<String, Object> hotelIdForUpdateKeyMap = new HashMap<>();
        hotelIdForUpdateKeyMap.put(HotelDao.ATTR_ID, hotelId);
        Map<String, Object> getData = new HashMap<>();
        getData.put(HotelDao.ATTR_POPULARITY, count);
        return this.daoHelper.update(this.hotelDao, getData,hotelIdForUpdateKeyMap);

    }


}
