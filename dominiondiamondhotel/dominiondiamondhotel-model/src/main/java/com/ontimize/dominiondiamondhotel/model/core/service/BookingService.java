package com.ontimize.dominiondiamondhotel.model.core.service;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;
import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.entity.Artist;
import com.ontimize.dominiondiamondhotel.model.core.entity.Event;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
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
    private PostalCodeService postalCodeService;
    private CountryCodeService countryCodeService;
    private HttpClient httpClient;
    private CustomerDao customerDao;
    @Autowired
    public BookingService(DefaultOntimizeDaoHelper daoHelper,
                          BookingDao bookingDao, RoomDao roomDao, CustomerDao customerDao,
                          CustomerService customerService, RoomService roomService,
                          HotelService hotelService,
                          PostalCodeService postalCodeService, CountryCodeService countryCodeService) {
        this(daoHelper, bookingDao,
                roomDao,customerDao,
                customerService, roomService,
                hotelService,
                postalCodeService, HttpClientBuilder.create().build(), countryCodeService);
    }

    public BookingService(DefaultOntimizeDaoHelper daoHelper,
                          BookingDao bookingDao, RoomDao roomDao, CustomerDao customerDao,
                          CustomerService customerService, RoomService roomService,
                          HotelService hotelService,
                          PostalCodeService postalCodeService, HttpClient httpClient, CountryCodeService countryCodeService) {
        this.daoHelper = daoHelper;
        this.bookingDao = bookingDao;
        this.roomDao = roomDao;
        this.customerService = customerService;
        this.roomService = roomService;
        this.hotelService = hotelService;
        this.postalCodeService = postalCodeService;
        this.httpClient = httpClient;
        this.countryCodeService = countryCodeService;
        this.customerDao = customerDao;
    }

    private static final String ACCUW_APIKEY = "luaDerURMSfvcG8HrlwSyYD037JwDGCT";
    private static final String ACCUW_GENERAL_URI = "http://dataservice.accuweather.com/";
    private static final String ACCUW_POSTALCODES_ES_SEARCH = "locations/v1/postalcodes/es/search?";
    private static final String ACCUW_DAILY_FORECAST_URI = "forecasts/v1/daily/5day/";
    private static final String ACCUW_API_KEY_URI = "apikey=" + ACCUW_APIKEY;
    private static final String ACCUW_LANGUAGUE_URI = "&language=en-us";
    private static final String ACCUW_DETAILS_URI = "&details=true";
    private static final String ACCUW_Q_TO_SEARCH = "&q=";
    private static final String TM_APIKEY = "&apikey=j6RzdC0NB4bQuUfXeW05beAAlRJcgGm4";
    private static final String TM_GENERAL_URI = "https://app.ticketmaster.com/discovery/v2/events.json?";
    private static final String TM_COUNTRY_CODE = "countryCode=";
    private static final String TM_POSTAL_CODE = "&postalCode=";
    private static final String TM_DATETIME = "&startDateTime=";


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
            return this.daoHelper.query(this.bookingDao, bookingIdKeyMap, BookingDao.getColumns());
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
            HttpEntity httpEntity = getEntity(ACCUW_GENERAL_URI + ACCUW_POSTALCODES_ES_SEARCH + ACCUW_API_KEY_URI + ACCUW_Q_TO_SEARCH + zipToSearch + ACCUW_LANGUAGUE_URI + ACCUW_DETAILS_URI);
            String retSrc;
            if (httpEntity != null) {
                retSrc = EntityUtils.toString(httpEntity);
                JSONArray resultGetKey = gson.fromJson(retSrc, JSONArray.class);
                key = String.valueOf(((LinkedTreeMap<?, ?>) resultGetKey.get(0)).get("Key"));
            } else {
                throw new IOException(HTTP_ENTITY_NULL);
            }
        } catch (IOException e) {
            throw new OntimizeJEERuntimeException();
        }

        Forecast forecast;

        try {
            gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            HttpEntity httpEntity = getEntity(ACCUW_GENERAL_URI + ACCUW_DAILY_FORECAST_URI + key + "?" + ACCUW_API_KEY_URI + ACCUW_LANGUAGUE_URI);
            if (httpEntity != null) {
                String content = EntityUtils.toString(httpEntity);
                forecast = gson.fromJson(content, Forecast.class);
            } else {
                throw new IOException(HTTP_ENTITY_NULL);
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
    public EntityResult getEventsQuery(Map<String, Object> keyMap, List<String> attrList) {
        int hotelId = Integer.parseInt(String.valueOf(((List<?>) this.daoHelper.query(this.bookingDao, keyMap, BookingDao.getColumns()).get(BookingDao.ATTR_HOTEL_ID)).get(0)));
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
        EntityResult postalCodeQuery = postalCodeService.postalCodeQuery(zipKey, List.of(PostalCodeDao.ATTR_ZIP, PostalCodeDao.ATTR_ISO_ID));
        int zipToSearch = Integer.parseInt(String.valueOf(((List<?>) postalCodeQuery.get(PostalCodeDao.ATTR_ZIP)).get(0)));
        int isoToSearch = Integer.parseInt(String.valueOf(((List<?>) postalCodeQuery.get(PostalCodeDao.ATTR_ISO_ID)).get(0)));
        Map<String, Object> countryCodeKey = new HashMap<>();
        be = BasicExpressionUtils.searchBy(SQLStatementBuilder.BasicOperator.EQUAL_OP, CountryCodeDao.ATTR_ID, String.valueOf(isoToSearch));
        if (be != null) {
            countryCodeKey.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        String finalISO = String.valueOf(((List<?>) countryCodeService.countryCodeQuery(countryCodeKey, List.of(CountryCodeDao.ATTR_ISO)).get(CountryCodeDao.ATTR_ISO)).get(0));
        LinkedList<Event> events = new LinkedList<>();
        try {
            HttpEntity httpEntity = getEntity(TM_GENERAL_URI + TM_COUNTRY_CODE + finalISO + TM_APIKEY + TM_POSTAL_CODE + zipToSearch + TM_DATETIME + DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
            if (httpEntity != null) {
                String src = EntityUtils.toString(httpEntity);
                JsonObject fullResult = new Gson().fromJson(src, JsonObject.class);
                JsonArray jsonEventArrays = fullResult.getAsJsonObject("_embedded").getAsJsonArray("events");
                jsonEventArrays.forEach(event -> {
                    JsonObject eventFromJsonElement = event.getAsJsonObject();
                    Event e = new Event();
                    e.setName(eventFromJsonElement.get("name").getAsString());
                    e.setUrl(eventFromJsonElement.get("url").getAsString());
                    try {
                        e.setDate(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse(eventFromJsonElement.getAsJsonObject("dates").getAsJsonObject("start").get("dateTime").getAsString()));
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    JsonObject classifications = eventFromJsonElement.getAsJsonArray("classifications").get(0).getAsJsonObject();
                    e.setEventType(classifications.getAsJsonObject("segment").get("name").getAsString());
                    e.setGenre(classifications.getAsJsonObject("genre").get("name").getAsString());
                    e.setSubGenre(classifications.getAsJsonObject("subGenre").get("name").getAsString());
                    e.setFamily(classifications.get("family").getAsBoolean());
                    e.setMaxPrice(eventFromJsonElement.getAsJsonArray("priceRanges").get(1).getAsJsonObject().get("max").getAsDouble());
                    JsonObject embeddedInside = eventFromJsonElement.getAsJsonObject("_embedded");
                    JsonObject venues = embeddedInside.getAsJsonArray("venues").get(0).getAsJsonObject();
                    e.setHallName(venues.getAsJsonObject().get("name").getAsString());
                    e.setAddress(venues.getAsJsonObject("address").get("line1").getAsString());
                    JsonObject jsonArtist = embeddedInside.getAsJsonArray("attractions").get(0).getAsJsonObject();
                    Artist a = new Artist();
                    a.setName(jsonArtist.get("name").getAsString());
                    JsonObject artistClassifications = jsonArtist.getAsJsonArray("classifications").get(0).getAsJsonObject();
                    a.setGenre(artistClassifications.getAsJsonObject("genre").get("name").getAsString());
                    a.setSubGenre(artistClassifications.getAsJsonObject("subGenre").get("name").getAsString());
                    a.setType(artistClassifications.getAsJsonObject("type").get("name").getAsString());
                    a.setSubType(artistClassifications.getAsJsonObject("subType").get("name").getAsString());
                    a.setFamily(artistClassifications.get("family").getAsBoolean());
                    e.setArtist(a);
                    events.add(e);
                });
            } else {
                throw new IOException(HTTP_ENTITY_NULL);
            }
        } catch (IOException e) {
            throw new OntimizeJEERuntimeException();
        }
        EntityResult erEvents = new EntityResultMapImpl();
        erEvents.setCode(EntityResult.OPERATION_SUCCESSFUL);
        erEvents.put("events", List.of(events));
        return erEvents;
    }
  
    public EntityResult payExpenses(Map<String, Object> getFilter, Map<String, Object> getData) throws OntimizeJEERuntimeException {
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
            } else {
                er.setCode(EntityResult.OPERATION_SUCCESSFUL);
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
        } else {
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

    private HttpEntity getEntity (String uri) throws IOException {
        HttpGet getRequest = new HttpGet(uri);
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException("Failed with HTTP error code : " + statusCode);
        }
        return response.getEntity();
    }
}
