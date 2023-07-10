package com.ontimize.dominiondiamondhotel.model.core.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;
import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.entity.Forecast;
import com.ontimize.dominiondiamondhotel.model.core.utils.BasicExpressionUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.BookingUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.*;

@Lazy
@Service("BookingService")
public class BookingService implements IBookingService {
    private DefaultOntimizeDaoHelper daoHelper;
    private BookingDao bookingDao;
    private RoomDao roomDao;
    private CustomerService customerService;
    private RoomService roomService;
    private HotelService hotelService;
    private PostalCodeDao postalCodeDao;
    private PostalCodeService postalCodeService;
    private HttpClient httpClient;
    private CustomerDao customerDao;
    @Autowired
    public BookingService(DefaultOntimizeDaoHelper daoHelper,
                          BookingDao bookingDao, RoomDao roomDao, CustomerDao customerDao,
                          CustomerService customerService, RoomService roomService,
                          HotelService hotelService, PostalCodeDao postalCodeDao,
                          PostalCodeService postalCodeService) {
        this(daoHelper, bookingDao,
                roomDao,customerDao,
                customerService, roomService,
                hotelService, postalCodeDao,
                postalCodeService, HttpClientBuilder.create().build());
    }

    public BookingService(DefaultOntimizeDaoHelper daoHelper,
                          BookingDao bookingDao, RoomDao roomDao, CustomerDao customerDao,
                          CustomerService customerService, RoomService roomService,
                          HotelService hotelService, PostalCodeDao postalCodeDao,
                          PostalCodeService postalCodeService, HttpClient httpClient) {
        this.daoHelper = daoHelper;
        this.bookingDao = bookingDao;
        this.roomDao = roomDao;
        this.customerService = customerService;
        this.roomService = roomService;
        this.hotelService = hotelService;
        this.postalCodeDao = postalCodeDao;
        this.postalCodeService = postalCodeService;
        this.httpClient = httpClient;
        this.customerDao = customerDao;
    }

    private static final String APIKEY = "luaDerURMSfvcG8HrlwSyYD037JwDGCT";
    private static final String GENERAL_URI = "http://dataservice.accuweather.com/";
    private static final String POSTALCODES_ES_SEARCH = "locations/v1/postalcodes/es/search?";
    private static final String DAILY_FORECAST_URI = "forecasts/v1/daily/5day/";
    private static final String API_KEY_URI = "apikey=" + APIKEY;
    private static final String LANGUAGUE_URI = "&language=en-us";
    private static final String DETAILS_URI = "&details=true";
    private static final String Q_TO_SEARCH = "&q=";

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
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
        int hotelIdFromBooking = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_HOTEL_ID)).get(0)));
        int customerId = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_CUSTOMER_ID)).get(0)));
        String customerIdNumber = String.valueOf(getFilter.get(CustomerDao.ATTR_IDNUMBER));
        Map<String, Object> customerKeyMap = new HashMap<>();
        customerKeyMap.put(CustomerDao.ATTR_IDNUMBER, customerIdNumber);
        EntityResult customerExists = this.customerService.customerQuery(customerKeyMap, List.of(CustomerDao.ATTR_ID));
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
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
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
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
        EntityResult er = new EntityResultMapImpl();
        if (((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0) != null && ((List<?>) bookingExists.get(BookingDao.ATTR_CHECK_OUT)).get(0) == null) {
            int roomIdFromBooking = Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_ROOM_ID)).get(0)));
            Map<String, Object> filter = new HashMap<>();
            filter.put("id", ((List<?>) bookingExists.get(BookingDao.ATTR_ID)).get(0));
            Map<String, Object> data = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();
            data.put(BookingDao.ATTR_CHECK_OUT, now);
            this.daoHelper.update(this.bookingDao, data, filter);
            EntityResult result = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
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
        EntityResult customerExists = this.customerService.customerQuery(customerIdKeyMap, List.of(CustomerDao.ATTR_ID));
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
                    this.daoHelper.update(this.bookingDao, getData, filter).getCode() == EntityResult.OPERATION_SUCCESSFUL &&
                    (hotelRating(Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_HOTEL_ID)).get(0)))).getCode() == EntityResult.OPERATION_SUCCESSFUL)) {
                return this.daoHelper.query(this.bookingDao, bookingIdKeyMap, List.of(BookingDao.ATTR_ID, BookingDao.ATTR_COMM, BookingDao.ATTR_MEAN));
            }
        }
        er.setMessage(INVALID_DATA);
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult getForecastQuery(Map<String, Object> keyMap, List<String> attrList) {
        Gson gson = new Gson();

        int hotelId = Integer.parseInt(String.valueOf(((List<?>) this.daoHelper.query(this.bookingDao, keyMap, attrList).get(BookingDao.ATTR_HOTEL_ID)).get(0)));
        Map<String, Object> hotelKey = new HashMap<>();
        SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, HotelDao.ATTR_ID, String.valueOf(hotelId));
        if (be != null) {
            hotelKey.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        int hotelZipCode = Integer.parseInt(String.valueOf(((List<?>) hotelService.hotelQuery(hotelKey, List.of(HotelDao.ATTR_ZIP_ID)).get(HotelDao.ATTR_ZIP_ID)).get(0)));
        Map<String, Object> zipKey = new HashMap<>();
        be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, PostalCodeDao.ATTR_ID, String.valueOf(hotelZipCode));
        if (be != null) {
            zipKey.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        int zipToSearch = Integer.parseInt(String.valueOf(((List<?>) postalCodeService.postalCodeQuery(zipKey, List.of(PostalCodeDao.ATTR_ZIP)).get(PostalCodeDao.ATTR_ZIP)).get(0)));

        String key;

        try {
            HttpGet getRequest = new HttpGet(GENERAL_URI + POSTALCODES_ES_SEARCH + API_KEY_URI + Q_TO_SEARCH + zipToSearch + LANGUAGUE_URI + DETAILS_URI);
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity httpEntity = response.getEntity();
            String retSrc;
            if (httpEntity != null) {
                retSrc = EntityUtils.toString(httpEntity);
                // parsing JSON
                JSONArray resultGetKey = gson.fromJson(retSrc, JSONArray.class); //Convert String to JSON Array
                key = String.valueOf(((LinkedTreeMap<?, ?>) resultGetKey.get(0)).get("Key"));
            } else {
                throw new IOException("HttpEntity null");
            }
        } catch (IOException e) {
            throw new OntimizeJEERuntimeException();
        }

        Forecast forecast;

        try {
            gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            HttpGet getRequest = new HttpGet(GENERAL_URI + DAILY_FORECAST_URI + key + "?" + API_KEY_URI + LANGUAGUE_URI);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity httpEntity = response.getEntity();

            if (httpEntity != null) {
                // parsing JSON
                String content = EntityUtils.toString(httpEntity);
                forecast = gson.fromJson(content, Forecast.class);
            } else {
                throw new IOException("HttpEntity null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new OntimizeJEERuntimeException(e.getLocalizedMessage());
        }
        EntityResult erForecast = new EntityResultMapImpl();
        erForecast.setCode(EntityResult.OPERATION_SUCCESSFUL);
        erForecast.put("forecast", forecast);
        return erForecast;
    }

    @Override
    public EntityResult bookingExpenseUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        Map<?, ?> getData = (Map<?, ?>) keyMap.get(DATA);
        int bookingId = Integer.parseInt(String.valueOf(getFilter.get(BookingDao.ATTR_ID)));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        SQLStatementBuilder.BasicExpression be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, BookingDao.ATTR_ID, String.valueOf(bookingId));
        if (be != null) {
            bookingIdKeyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        EntityResult bookingExists = this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
        if (!bookingExists.isEmpty()) {
            double savedExpense = Double.parseDouble(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_EXPENSES)).get(0)));
            double totalExpenses = savedExpense + Double.parseDouble(String.valueOf(getData.get(BookingDao.ATTR_EXPENSES)));
            Map<String, Object> bookingUpdateKeyMap = new HashMap<>();
            bookingUpdateKeyMap.put(BookingDao.ATTR_ID, bookingId);
            Map<String, Object> bookingAttrKeyMap = new HashMap<>();
            bookingAttrKeyMap.put(BookingDao.ATTR_EXPENSES, totalExpenses);
            return this.daoHelper.update(this.bookingDao, bookingAttrKeyMap, bookingUpdateKeyMap);
        }
        EntityResult errorEr = new EntityResultMapImpl();
        errorEr.setMessage(INVALID_DATA);
        errorEr.setCode(EntityResult.OPERATION_WRONG);
        return errorEr;
    }

    @Override
    public EntityResult payExpenses(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        Map<?, ?> getData = (Map<?, ?>) keyMap.get(DATA);
        int bookingId =  Integer.parseInt(String.valueOf(getFilter.get(BookingDao.ATTR_ID)));
        String documentId = (String) getFilter.get(CustomerDao.ATTR_IDNUMBER);
        double money = Double.parseDouble(String.valueOf((getData.get("paying"))));
        Map<String, Object> bookingIdKeyMap = new HashMap<>();
        bookingIdKeyMap.put(BookingDao.ATTR_ID, bookingId);
        EntityResult bookingExists =  this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
        EntityResult er = new EntityResultMapImpl();
        if (!bookingExists.isEmpty() && money > 0 && Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_EXPENSES)).get(0))) > 0) {

            Map<String, Object> customersBooking = new HashMap<>();
            customersBooking.put(CustomerDao.ATTR_ID, Integer.parseInt(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_CUSTOMER_ID)).get(0))));
            EntityResult customersDocumentId = this.daoHelper.query(this.customerDao, customersBooking, List.of(CustomerDao.ATTR_IDNUMBER));

            if(!String.valueOf(((List<?>) customersDocumentId.get(CustomerDao.ATTR_IDNUMBER)).get(0)).equalsIgnoreCase(documentId)){

                er.setMessage("Invalid document id");
                er.setCode(EntityResult.OPERATION_WRONG);

            }else{

                double savedExpense = Double.parseDouble(String.valueOf(((List<?>) bookingExists.get(BookingDao.ATTR_EXPENSES)).get(0)));
                double updatedExpense = savedExpense - money;
                if (updatedExpense > 0) {
                    er.setMessage("You need to pay " + updatedExpense + " more");
                } else {
                    if (updatedExpense < 0) {
                        er.setMessage("Your change is : " + (money - savedExpense));
                        updatedExpense = 0;
                    } else {
                        er.setMessage("Thank you, your operation has been completed successfully");
                    }
                }

                Map<String, Object> updatedExpenseMap = new HashMap<>();
                updatedExpenseMap.put(BookingDao.ATTR_EXPENSES, updatedExpense);
                this.daoHelper.update(this.bookingDao, updatedExpenseMap, bookingIdKeyMap);
            }

        }else {
            er.setCode(EntityResult.OPERATION_WRONG);
            er.setMessage(INVALID_DATA);
        }
        return er;
    }

    private EntityResult hotelRating(int hotelId) {
        Map<String, Object> hotelIdKeyMap = new HashMap<>();
        hotelIdKeyMap.put(BookingDao.ATTR_HOTEL_ID, hotelId);
        hotelIdKeyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, BookingUtils.meanNotNull());
        EntityResult hotelMean = this.daoHelper.query(this.bookingDao, hotelIdKeyMap, List.of("hotelmean"), "meanHotel");
        Double mean = Double.parseDouble(String.valueOf(((List<?>) hotelMean.get("hotelmean")).get(0)));
        Map<String, Object> hotelIdForUpdateKeyMap = new HashMap<>();
        hotelIdForUpdateKeyMap.put(HotelDao.ATTR_ID, hotelId);
        Map<String, Object> getData = new HashMap<>();
        getData.put(HotelDao.ATTR_RATING, mean);
        return this.hotelService.hotelUpdate(getData, hotelIdForUpdateKeyMap);
    }


    private EntityResult updatePopularity(int hotelId) {
        Map<String, Object> hotelIdKeyMap = new HashMap<>();
        hotelIdKeyMap.put(BookingDao.ATTR_HOTEL_ID, hotelId);
        EntityResult hotelPopularity = this.daoHelper.query(this.bookingDao, hotelIdKeyMap, List.of("counting"), "countPopularity");
        int count = Integer.parseInt(String.valueOf(((List<?>) hotelPopularity.get("counting")).get(0)));
        Map<String, Object> hotelIdForUpdateKeyMap = new HashMap<>();
        hotelIdForUpdateKeyMap.put(HotelDao.ATTR_ID, hotelId);
        Map<String, Object> getData = new HashMap<>();
        getData.put(HotelDao.ATTR_POPULARITY, count);
        return this.hotelService.hotelUpdate(getData, hotelIdForUpdateKeyMap);
    }
}
