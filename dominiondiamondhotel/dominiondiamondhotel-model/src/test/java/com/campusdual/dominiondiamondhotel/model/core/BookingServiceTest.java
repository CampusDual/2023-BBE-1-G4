package com.campusdual.dominiondiamondhotel.model.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ontimize.dominiondiamondhotel.model.core.dao.*;
import com.ontimize.dominiondiamondhotel.model.core.entity.Forecast;
import com.ontimize.dominiondiamondhotel.model.core.service.*;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.*;
import static com.ontimize.jee.common.dto.EntityResult.OPERATION_SUCCESSFUL;
import static com.ontimize.jee.common.dto.EntityResult.OPERATION_WRONG;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    DefaultOntimizeDaoHelper daoHelper;
    @InjectMocks
    BookingService bookingService;
    @Mock
    RoomService roomService;
    @Mock
    HotelService hotelService;
    @Mock
    CustomerService customerService;
    @Mock
    BookingDao bookingDao;
    @Mock
    RoomDao roomDao;
    @Mock
    CustomerDao customerDao;
    @Mock
    PostalCodeService postalCodeService;
    @Mock
    HttpClient httpClient;
    @Mock
    CountryCodeService countryCodeService;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceInsert {
        @Test
        void testBookingInsertAssertsSuccesful() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put(BookingDao.ATTR_ENTRY_DATE, LocalDate.now().plusDays(10));
            bookingToInsert.put(BookingDao.ATTR_EXIT_DATE, LocalDate.now().plusDays(15));
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
        }

        @Test
        void testBookingInsertAssertsWrong() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(OPERATION_WRONG);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put(BookingDao.ATTR_ENTRY_DATE, LocalDate.now().plusDays(10));
            bookingToInsert.put(BookingDao.ATTR_EXIT_DATE, LocalDate.now().plusDays(17));
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            Assertions.assertEquals(OPERATION_WRONG, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceCheck {
        @Test
        void testBookingCheckIn() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(BookingDao.ATTR_ID, List.of(1));
            er.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            er.put(BookingDao.ATTR_CUSTOMER_ID, List.of(1));
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put("check_out", nullList);
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> sqltypes = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, 1);
            filter.put(CustomerDao.ATTR_IDNUMBER, "47407434H");
            sqltypes.put(BookingDao.ATTR_ID, 12);
            sqltypes.put(BookingDao.ATTR_CHECK_IN, 91);
            bookingToInsert.put(FILTER, filter);
            bookingToInsert.put("sqltypes", sqltypes);
            EntityResult erCustomer = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erCustomer.put(BookingDao.ATTR_ID, List.of(1));
            erCustomer.put(CustomerDao.ATTR_IDNUMBER, List.of("47407434H"));
            EntityResult erRoom = new EntityResultMapImpl();
            erRoom.setCode(EntityResult.OPERATION_SUCCESSFUL);
            erRoom.put("id", List.of(1));
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er);
            when(roomService.getRoomByHotelIdAndStatusQuery(anyMap())).thenReturn(erRoom);
            when(customerService.customerQuery(anyMap(), anyList())).thenReturn(erCustomer);
            EntityResult result = bookingService.bookingCheckInUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(3)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            verify(customerService, times(1)).customerQuery(anyMap(), anyList());
            verify(roomService, times(1)).roomUpdate(anyMap(), anyMap());
        }

        @Test
        void testBookingCheckOut() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            er.put(BookingDao.ATTR_ID, List.of(1));
            er.put(BookingDao.ATTR_ROOM_ID, List.of(1));
            er.put(BookingDao.ATTR_CHECK_IN, List.of(LocalDate.now().plusDays(10)));
            er.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            er.put(BookingDao.ATTR_EXPENSES, List.of(0));
            EntityResult hotelEr = new EntityResultMapImpl();
            hotelEr.put("counting", List.of(1));
            List<String> nullList = new ArrayList<>();
            nullList.add(null);
            er.put(BookingDao.ATTR_CHECK_OUT, nullList);
            EntityResult er2 = new EntityResultMapImpl();
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> sqltypes = new HashMap<>();
            sqltypes.put(BookingDao.ATTR_ID, 12);
            filter.put(BookingDao.ATTR_ID, 1);
            sqltypes.put(BookingDao.ATTR_CHECK_OUT, 91);
            bookingToInsert.put("filter", filter);
            bookingToInsert.put("sqltypes", sqltypes);
            EntityResult roomUpdate = new EntityResultMapImpl();
            roomUpdate.put(RoomDao.ATTR_ID, List.of(1));
            roomUpdate.put(RoomDao.ATTR_STATE_ID, List.of(1));
            roomUpdate.setCode(EntityResult.OPERATION_SUCCESSFUL);
            EntityResult hotelUpdate = new EntityResultMapImpl();
            roomUpdate.put(HotelDao.ATTR_ID, List.of(1));
            roomUpdate.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList(), anyString())).thenReturn(hotelEr);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(roomUpdate);
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er2);
            when(hotelService.hotelUpdate(anyMap(), anyMap())).thenReturn(hotelUpdate);
            EntityResult result = bookingService.bookingCheckOutUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList(), anyString());
            verify(hotelService, times(1)).hotelUpdate(anyMap(), anyMap());
            verify(roomService, times(1)).roomUpdate(anyMap(), anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceUpdate {
        @Test
        void testBookingCalifications() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            EntityResult customerExists = new EntityResultMapImpl();
            customerExists.put(CustomerDao.ATTR_ID, List.of(1));
            customerExists.put(CustomerDao.ATTR_IDNUMBER, List.of(1));
            EntityResult bookingExists = new EntityResultMapImpl();
            bookingExists.put(BookingDao.ATTR_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_CUSTOMER_ID, List.of(1));
            bookingExists.put(BookingDao.ATTR_CHECK_OUT, List.of(1));
            EntityResult hotelExists = new EntityResultMapImpl();
            hotelExists.put("hotelmean", List.of(10));
            Map<String, Object> bookingToInsert = new HashMap<>();
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put(BookingDao.ATTR_CLEANING, 10);
            bookingData.put(BookingDao.ATTR_FACILITIES, 10);
            bookingData.put(BookingDao.ATTR_PRICEQUALITY, 10);
            EntityResult bookingWithMean = new EntityResultMapImpl();
            Map<String, Object> bookingFilter = new HashMap<>();
            bookingFilter.put(BookingDao.ATTR_ID, 1);
            bookingFilter.put(CustomerDao.ATTR_IDNUMBER, 1);
            bookingToInsert.put("data", bookingData);
            bookingToInsert.put("filter", bookingFilter);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingExists, bookingWithMean);
            when(customerService.customerQuery(anyMap(), anyList())).thenReturn(customerExists);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList(), anyString())).thenReturn(hotelExists);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(er);
            when(hotelService.hotelUpdate(anyMap(), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingCalificationsAndCommentUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList(), anyString());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            verify(customerService, times(1)).customerQuery(anyMap(), anyList());
            verify(hotelService, times(1)).hotelUpdate(anyMap(), anyMap());
        }

        @Test
        void testBookingExpenses() {
            EntityResult bookingExistER = new EntityResultMapImpl();
            bookingExistER.put(BookingDao.ATTR_ID, List.of(1));
            bookingExistER.put(BookingDao.ATTR_EXPENSES, List.of(100));
            EntityResult bookingUpdateER = new EntityResultMapImpl();
            bookingUpdateER.put(BookingDao.ATTR_ID, List.of(1));
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingExistER);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(bookingUpdateER);
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, 1);
            Map<String, Object> data = new HashMap<>();
            data.put(BookingDao.ATTR_EXPENSES, 100.50);
            Map<String, Object> req = new HashMap<>();
            List<String> columns = BookingDao.getColumns();
            req.put(FILTER, filter);
            req.put(DATA, data);
            req.put(COLUMNS, columns);
            EntityResult finalER = bookingService.bookingExpenseUpdate(req);
            Assertions.assertEquals(OPERATION_SUCCESSFUL, finalER.getCode());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
        }

        @Test
        void testPayExpenses(){
            EntityResult bookingExistsER = new EntityResultMapImpl();
            bookingExistsER.put(BookingDao.ATTR_ID, List.of(1));
            bookingExistsER.put(BookingDao.ATTR_EXPENSES, List.of(1));
            bookingExistsER.put(BookingDao.ATTR_CUSTOMER_ID, List.of(1));
            EntityResult customerER = new EntityResultMapImpl();
            customerER.put(CustomerDao.ATTR_IDNUMBER, List.of("55555555X"));
            EntityResult updateER = new EntityResultMapImpl();
            updateER.put(BookingDao.ATTR_ID, List.of(1));
            Map<String, Object> filter = new HashMap<>();
            filter.put(BookingDao.ATTR_ID, 24);
            filter.put(CustomerDao.ATTR_IDNUMBER, "55555555X");
            Map<String, Object> data = new HashMap<>();
            data.put("paying", 100.50);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingExistsER);
            when(customerService.customerQuery(anyMap(), anyList())).thenReturn(customerER);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(updateER);
            EntityResult result = bookingService.payExpenses(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(BookingDao.class),anyMap(),anyList());
            verify(customerService, times(1)).customerQuery(anyMap(),anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class),anyMap(),anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceForecast {
        @Test
        void testForecast() throws IOException {
            String forecastValue = "{\"Headline\":{\"EffectiveDate\":\"2023-07-03T08:00:00+02:00\",\"EffectiveEpochDate\":1688364000,\"Severity\":3,\"Text\":\"The heat wave will continue through Tuesday\",\"Category\":\"heat\",\"EndDate\":\"2023-07-04T20:00:00+02:00\",\"EndEpochDate\":1688493600,\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?lang=en-us\"},\"DailyForecasts\":[{\"Date\":\"2023-07-03T07:00:00+02:00\",\"EpochDate\":1688360400,\"Temperature\":{\"Minimum\":{\"Value\":69.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":96.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":3,\"IconPhrase\":\"Partly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":34,\"IconPhrase\":\"Mostly clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=1&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=1&lang=en-us\"},{\"Date\":\"2023-07-04T07:00:00+02:00\",\"EpochDate\":1688446800,\"Temperature\":{\"Minimum\":{\"Value\":67.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":94.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=2&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=2&lang=en-us\"},{\"Date\":\"2023-07-05T07:00:00+02:00\",\"EpochDate\":1688533200,\"Temperature\":{\"Minimum\":{\"Value\":66.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":94.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":35,\"IconPhrase\":\"Partly cloudy\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=3&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=3&lang=en-us\"},{\"Date\":\"2023-07-06T07:00:00+02:00\",\"EpochDate\":1688619600,\"Temperature\":{\"Minimum\":{\"Value\":65.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":92.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=4&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=4&lang=en-us\"},{\"Date\":\"2023-07-07T07:00:00+02:00\",\"EpochDate\":1688706000,\"Temperature\":{\"Minimum\":{\"Value\":66.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":92.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":1,\"IconPhrase\":\"Sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=5&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=5&lang=en-us\"}]}";
            String keyValue = "[{\"Version\":1,\"Key\":\"95226_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Madrid\",\"EnglishName\":\"Madrid\",\"PrimaryPostalCode\":\"28015\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"ES\",\"LocalizedName\":\"Spain\",\"EnglishName\":\"Spain\"},\"AdministrativeArea\":{\"ID\":\"MD\",\"LocalizedName\":\"Madrid\",\"EnglishName\":\"Madrid\",\"Level\":1,\"LocalizedType\":\"Autonomous Community\",\"EnglishType\":\"Autonomous Community\",\"CountryID\":\"ES\"},\"TimeZone\":{\"Code\":\"CEST\",\"Name\":\"Europe/Madrid\",\"GmtOffset\":2.0,\"IsDaylightSaving\":true,\"NextOffsetChange\":\"2023-10-29T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":40.429,\"Longitude\":-3.71,\"Elevation\":{\"Metric\":{\"Value\":667.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":2188.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"Alerts\",\"DailyPollenForecast\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\",\"Radar\"],\"Details\":{\"Key\":\"95226_PC\",\"StationCode\":\"LEA6\",\"StationGmtOffset\":1.0,\"BandMap\":\"SP\",\"Climo\":\"LEVS\",\"LocalRadar\":\"\",\"MediaRegion\":null,\"Metar\":\"LEMD\",\"NXMetro\":\"\",\"NXState\":\"\",\"Population\":null,\"PrimaryWarningCountyCode\":\"\",\"PrimaryWarningZoneCode\":\"MDZ722802\",\"Satellite\":\"EUR3\",\"Synoptic\":\"08220\",\"MarineStation\":\"\",\"MarineStationGMTOffset\":null,\"VideoCode\":\"\",\"LocationStem\":\"es/madrid/28015\",\"PartnerID\":null,\"Sources\":[{\"DataType\":\"AirQualityCurrentConditions\",\"Source\":\"Plume Labs\",\"SourceId\":63},{\"DataType\":\"AirQualityForecasts\",\"Source\":\"Plume Labs\",\"SourceId\":63},{\"DataType\":\"Alerts\",\"Source\":\"State Meteorological Agency\",\"SourceId\":39},{\"DataType\":\"CurrentConditions\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"DailyForecast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"DailyPollenForecast\",\"Source\":\"Copernicus Atmosphere Monitoring Service\",\"SourceId\":78},{\"DataType\":\"ForecastConfidence\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"FutureRadar\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"Historical\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"HourlyForecast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"MinuteCast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"Radar\",\"Source\":\"State Meteorological Agency\",\"SourceId\":39}],\"CanonicalPostalCode\":\"28015\",\"CanonicalLocationKey\":\"95226_PC\"}}]";
            HttpResponse responseKey = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
            responseKey.setEntity(new ByteArrayEntity(keyValue.getBytes(StandardCharsets.UTF_8)));
            HttpResponse responseForecast = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
            responseForecast.setEntity(new ByteArrayEntity(forecastValue.getBytes(StandardCharsets.UTF_8)));
            when(httpClient.execute(any(HttpGet.class))).thenReturn(responseKey, responseForecast);
            EntityResult bookingDaoER = new EntityResultMapImpl();
            bookingDaoER.setCode(OPERATION_SUCCESSFUL);
            bookingDaoER.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingDaoER);
            EntityResult hotelServiceER = new EntityResultMapImpl();
            hotelServiceER.setCode(OPERATION_SUCCESSFUL);
            hotelServiceER.put(HotelDao.ATTR_ZIP_ID, List.of(1));
            when(hotelService.hotelQuery(anyMap(), anyList())).thenReturn(hotelServiceER);
            EntityResult postalCodeServiceER = new EntityResultMapImpl();
            postalCodeServiceER.setCode(OPERATION_SUCCESSFUL);
            postalCodeServiceER.put(PostalCodeDao.ATTR_ZIP, List.of(1));
            when(postalCodeService.postalCodeQuery(anyMap(), anyList())).thenReturn(postalCodeServiceER);
            Map<String, Object> bookingKeyMap = new HashMap<>();
            bookingKeyMap.put(BookingDao.ATTR_ID, List.of(1));
            EntityResult er = bookingService.getForecastQuery(bookingKeyMap, BookingDao.getColumns());
            Assertions.assertEquals(OPERATION_SUCCESSFUL, er.getCode());
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            Forecast forecastFromEr = (Forecast) er.get("forecast");
            Forecast forecastFromString = gson.fromJson(forecastValue, Forecast.class);
            Assertions.assertEquals(forecastFromString.getHeadline().getEffectiveDate(), forecastFromEr.getHeadline().getEffectiveDate());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
            verify(hotelService, times(1)).hotelQuery(anyMap(), anyList());
            verify(postalCodeService, times(1)).postalCodeQuery(anyMap(), anyList());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceEvents {
        @Test
        void testEvents() throws IOException {
            String eventValue = "{\"_embedded\":{\"events\":[{\"name\":\"Daði Freyr - im doing a tour\",\"type\":\"event\",\"id\":\"Z698xZ2qZaFJo\",\"test\":false,\"url\":\"https://www.ticketmaster.es/event/dai-freyr-im-doing-a-tour-tickets/35217?language=en-us\",\"locale\":\"en-us\",\"images\":[{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_EVENT_DETAIL_PAGE_16_9.jpg\",\"width\":205,\"height\":115,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_LANDSCAPE_16_9.jpg\",\"width\":1136,\"height\":639,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_16_9.jpg\",\"width\":1024,\"height\":576,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_PORTRAIT_16_9.jpg\",\"width\":640,\"height\":360,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_SOURCE\",\"width\":2426,\"height\":1365,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_PORTRAIT_3_2.jpg\",\"width\":640,\"height\":427,\"fallback\":false},{\"ratio\":\"4_3\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_CUSTOM.jpg\",\"width\":305,\"height\":225,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_ARTIST_PAGE_3_2.jpg\",\"width\":305,\"height\":203,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_3_2.jpg\",\"width\":1024,\"height\":683,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RECOMENDATION_16_9.jpg\",\"width\":100,\"height\":56,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_LARGE_16_9.jpg\",\"width\":2048,\"height\":1152,\"fallback\":false}],\"sales\":{\"public\":{\"startDateTime\":\"2023-03-24T09:00:00Z\",\"startTBD\":false,\"startTBA\":false,\"endDateTime\":\"2023-09-21T19:00:00Z\"}},\"dates\":{\"start\":{\"localDate\":\"2023-09-21\",\"localTime\":\"21:00:00\",\"dateTime\":\"2023-09-21T19:00:00Z\",\"dateTBD\":false,\"dateTBA\":false,\"timeTBA\":false,\"noSpecificTime\":false},\"timezone\":\"Europe/Madrid\",\"status\":{\"code\":\"onsale\"},\"spanMultipleDays\":false},\"classifications\":[{\"primary\":true,\"segment\":{\"id\":\"KZFzniwnSyZfZ7v7nJ\",\"name\":\"Music\"},\"genre\":{\"id\":\"KnvZfZ7vAeA\",\"name\":\"Rock\"},\"subGenre\":{\"id\":\"KZazBEonSMnZfZ7v6F1\",\"name\":\"Pop\"},\"family\":false}],\"promoter\":{\"id\":\"2731\",\"name\":\"Live Nation España S.A.U.\"},\"promoters\":[{\"id\":\"2731\",\"name\":\"Live Nation España S.A.U.\"}],\"priceRanges\":[{\"type\":\"standard\",\"currency\":\"EUR\",\"min\":18.0,\"max\":18.0},{\"type\":\"standard including fees\",\"currency\":\"EUR\",\"min\":20.5,\"max\":20.5}],\"_links\":{\"self\":{\"href\":\"/discovery/v2/events/Z698xZ2qZaFJo?locale=en-us\"},\"attractions\":[{\"href\":\"/discovery/v2/attractions/K8vZ917bzof?locale=en-us\"}],\"venues\":[{\"href\":\"/discovery/v2/venues/Z198xZ2qZA17?locale=en-us\"}]},\"_embedded\":{\"venues\":[{\"name\":\"Sala Mon Live\",\"type\":\"venue\",\"id\":\"Z198xZ2qZA17\",\"test\":false,\"url\":\"https://www.ticketmaster.es/venue/sala-mon-live-madrid-entradas/slpenelmad/114\",\"locale\":\"en-us\",\"postalCode\":\"28015\",\"timezone\":\"Europe/Madrid\",\"city\":{\"name\":\"Madrid\"},\"state\":{\"name\":\"Madrid\"},\"country\":{\"name\":\"Spain\",\"countryCode\":\"ES\"},\"address\":{\"line1\":\"Calle Hilarión Eslava, 36\"},\"location\":{\"longitude\":\"-3.7165\",\"latitude\":\"40.4359\"},\"upcomingEvents\":{\"mfx-es\":5,\"_total\":5,\"_filtered\":0},\"_links\":{\"self\":{\"href\":\"/discovery/v2/venues/Z198xZ2qZA17?locale=en-us\"}}}],\"attractions\":[{\"name\":\"Daði Freyr\",\"type\":\"attraction\",\"id\":\"K8vZ917bzof\",\"test\":false,\"url\":\"https://www.ticketmaster.com/dai-freyr-tickets/artist/2754658\",\"locale\":\"en-us\",\"externalLinks\":{\"youtube\":[{\"url\":\"https://www.youtube.com/channel/UCF18N219OPiOcElz_hSYoIQ\"}],\"twitter\":[{\"url\":\"https://twitter.com/dadimakesmusic\"}],\"itunes\":[{\"url\":\"https://music.apple.com/us/artist/dadi-freyr/1256781645\"}],\"spotify\":[{\"url\":\"https://open.spotify.com/artist/3Hb64DQZIhDCgyHKrzBXOL\"}],\"facebook\":[{\"url\":\"https://www.facebook.com/dadimakesmusic/\"}],\"instagram\":[{\"url\":\"https://www.instagram.com/dadimakesmusic/\"}],\"homepage\":[{\"url\":\"https://dadifreyr.com\"}]},\"images\":[{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_EVENT_DETAIL_PAGE_16_9.jpg\",\"width\":205,\"height\":115,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_LANDSCAPE_16_9.jpg\",\"width\":1136,\"height\":639,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_16_9.jpg\",\"width\":1024,\"height\":576,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_PORTRAIT_16_9.jpg\",\"width\":640,\"height\":360,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_SOURCE\",\"width\":2426,\"height\":1365,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RETINA_PORTRAIT_3_2.jpg\",\"width\":640,\"height\":427,\"fallback\":false},{\"ratio\":\"4_3\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_CUSTOM.jpg\",\"width\":305,\"height\":225,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_ARTIST_PAGE_3_2.jpg\",\"width\":305,\"height\":203,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_3_2.jpg\",\"width\":1024,\"height\":683,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_RECOMENDATION_16_9.jpg\",\"width\":100,\"height\":56,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/7a8/31ae7b27-31fd-4dca-9449-4aaa7bb3d7a8_TABLET_LANDSCAPE_LARGE_16_9.jpg\",\"width\":2048,\"height\":1152,\"fallback\":false}],\"classifications\":[{\"primary\":true,\"segment\":{\"id\":\"KZFzniwnSyZfZ7v7nJ\",\"name\":\"Music\"},\"genre\":{\"id\":\"KnvZfZ7vAvF\",\"name\":\"Dance/Electronic\"},\"subGenre\":{\"id\":\"KZazBEonSMnZfZ7vAJJ\",\"name\":\"Dance Pop\"},\"type\":{\"id\":\"KZAyXgnZfZ7v7la\",\"name\":\"Individual\"},\"subType\":{\"id\":\"KZFzBErXgnZfZ7vAd7\",\"name\":\"Musician\"},\"family\":false}],\"upcomingEvents\":{\"mfx-dk\":3,\"ticketnet\":2,\"mfx-es\":2,\"ticketmaster\":10,\"mfx-de\":4,\"mfx-no\":1,\"mfx-it\":2,\"mfx-pl\":1,\"_total\":25,\"_filtered\":0},\"_links\":{\"self\":{\"href\":\"/discovery/v2/attractions/K8vZ917bzof?locale=en-us\"}}}]}},{\"name\":\"JP Cooper\",\"type\":\"event\",\"id\":\"Z698xZ2qZaFS2\",\"test\":false,\"url\":\"https://www.ticketmaster.es/event/jp-cooper-tickets/36047?language=en-us\",\"locale\":\"en-us\",\"images\":[{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_PORTRAIT_3_2.jpg\",\"width\":640,\"height\":427,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RECOMENDATION_16_9.jpg\",\"width\":100,\"height\":56,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_3_2.jpg\",\"width\":1024,\"height\":683,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_EVENT_DETAIL_PAGE_16_9.jpg\",\"width\":205,\"height\":115,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_SOURCE\",\"width\":2426,\"height\":1365,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_LARGE_16_9.jpg\",\"width\":2048,\"height\":1152,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_ARTIST_PAGE_3_2.jpg\",\"width\":305,\"height\":203,\"fallback\":false},{\"ratio\":\"4_3\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_CUSTOM.jpg\",\"width\":305,\"height\":225,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_PORTRAIT_16_9.jpg\",\"width\":640,\"height\":360,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_16_9.jpg\",\"width\":1024,\"height\":576,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_LANDSCAPE_16_9.jpg\",\"width\":1136,\"height\":639,\"fallback\":false}],\"sales\":{\"public\":{\"startDateTime\":\"2023-05-26T09:00:00Z\",\"startTBD\":false,\"startTBA\":false,\"endDateTime\":\"2023-12-08T20:00:00Z\"}},\"dates\":{\"start\":{\"localDate\":\"2023-12-08\",\"localTime\":\"21:00:00\",\"dateTime\":\"2023-12-08T20:00:00Z\",\"dateTBD\":false,\"dateTBA\":false,\"timeTBA\":false,\"noSpecificTime\":false},\"timezone\":\"Europe/Madrid\",\"status\":{\"code\":\"onsale\"},\"spanMultipleDays\":false},\"classifications\":[{\"primary\":true,\"segment\":{\"id\":\"KZFzniwnSyZfZ7v7nJ\",\"name\":\"Music\"},\"genre\":{\"id\":\"KnvZfZ7vAvl\",\"name\":\"Other\"},\"subGenre\":{\"id\":\"KZazBEonSMnZfZ7vk1I\",\"name\":\"Other\"},\"family\":false}],\"promoter\":{\"id\":\"2731\",\"name\":\"Live Nation España S.A.U.\"},\"promoters\":[{\"id\":\"2731\",\"name\":\"Live Nation España S.A.U.\"}],\"priceRanges\":[{\"type\":\"standard including fees\",\"currency\":\"EUR\",\"min\":27.0,\"max\":27.0},{\"type\":\"standard\",\"currency\":\"EUR\",\"min\":24.0,\"max\":24.0}],\"_links\":{\"self\":{\"href\":\"/discovery/v2/events/Z698xZ2qZaFS2?locale=en-us\"},\"attractions\":[{\"href\":\"/discovery/v2/attractions/K8vZ9172I50?locale=en-us\"}],\"venues\":[{\"href\":\"/discovery/v2/venues/Z198xZ2qZA17?locale=en-us\"}]},\"_embedded\":{\"venues\":[{\"name\":\"Sala Mon Live\",\"type\":\"venue\",\"id\":\"Z198xZ2qZA17\",\"test\":false,\"url\":\"https://www.ticketmaster.es/venue/sala-mon-live-madrid-entradas/slpenelmad/114\",\"locale\":\"en-us\",\"postalCode\":\"28015\",\"timezone\":\"Europe/Madrid\",\"city\":{\"name\":\"Madrid\"},\"state\":{\"name\":\"Madrid\"},\"country\":{\"name\":\"Spain\",\"countryCode\":\"ES\"},\"address\":{\"line1\":\"Calle Hilarión Eslava, 36\"},\"location\":{\"longitude\":\"-3.7165\",\"latitude\":\"40.4359\"},\"upcomingEvents\":{\"mfx-es\":5,\"_total\":5,\"_filtered\":0},\"_links\":{\"self\":{\"href\":\"/discovery/v2/venues/Z198xZ2qZA17?locale=en-us\"}}}],\"attractions\":[{\"name\":\"JP Cooper\",\"type\":\"attraction\",\"id\":\"K8vZ9172I50\",\"test\":false,\"url\":\"https://www.ticketmaster.com/jp-cooper-tickets/artist/1712414\",\"locale\":\"en-us\",\"externalLinks\":{\"youtube\":[{\"url\":\"https://www.youtube.com/user/jpcoopermusic\"}],\"twitter\":[{\"url\":\"https://twitter.com/jpcoopermusic\"}],\"itunes\":[{\"url\":\"https://music.apple.com/gb/artist/jp-cooper/576710545\"}],\"facebook\":[{\"url\":\"https://www.facebook.com/jpcoopermusic\"}],\"spotify\":[{\"url\":\"https://open.spotify.com/user/1118529106\"}],\"musicbrainz\":[{\"id\":\"be1f10dc-3b88-48e6-baa7-95aea6abf2f6\"}],\"instagram\":[{\"url\":\"https://www.instagram.com/jpcoopermusic\"}],\"homepage\":[{\"url\":\"https://jpcoopermusic.com/\"}]},\"images\":[{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_PORTRAIT_3_2.jpg\",\"width\":640,\"height\":427,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RECOMENDATION_16_9.jpg\",\"width\":100,\"height\":56,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_3_2.jpg\",\"width\":1024,\"height\":683,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_EVENT_DETAIL_PAGE_16_9.jpg\",\"width\":205,\"height\":115,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_SOURCE\",\"width\":2426,\"height\":1365,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_LARGE_16_9.jpg\",\"width\":2048,\"height\":1152,\"fallback\":false},{\"ratio\":\"3_2\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_ARTIST_PAGE_3_2.jpg\",\"width\":305,\"height\":203,\"fallback\":false},{\"ratio\":\"4_3\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_CUSTOM.jpg\",\"width\":305,\"height\":225,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_PORTRAIT_16_9.jpg\",\"width\":640,\"height\":360,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_TABLET_LANDSCAPE_16_9.jpg\",\"width\":1024,\"height\":576,\"fallback\":false},{\"ratio\":\"16_9\",\"url\":\"https://s1.ticketm.net/dam/a/d9f/cc55a954-de8d-4bb9-b113-94ac0a19dd9f_RETINA_LANDSCAPE_16_9.jpg\",\"width\":1136,\"height\":639,\"fallback\":false}],\"classifications\":[{\"primary\":true,\"segment\":{\"id\":\"KZFzniwnSyZfZ7v7nJ\",\"name\":\"Music\"},\"genre\":{\"id\":\"KnvZfZ7vAeA\",\"name\":\"Rock\"},\"subGenre\":{\"id\":\"KZazBEonSMnZfZ7v6F1\",\"name\":\"Pop\"},\"type\":{\"id\":\"KZAyXgnZfZ7v7nI\",\"name\":\"Undefined\"},\"subType\":{\"id\":\"KZFzBErXgnZfZ7v7lJ\",\"name\":\"Undefined\"},\"family\":false}],\"upcomingEvents\":{\"mfx-dk\":1,\"ticketnet\":1,\"mfx-es\":2,\"tmr\":7,\"mfx-nl\":1,\"ticketweb\":4,\"ticketmaster\":20,\"mfx-no\":2,\"_total\":38,\"_filtered\":0},\"_links\":{\"self\":{\"href\":\"/discovery/v2/attractions/K8vZ9172I50?locale=en-us\"}}}]}}]},\"_links\":{\"self\":{\"href\":\"/discovery/v2/events.json?startDateTime=2023-07-11T11%3A13%3A37Z&countryCode=ES&postalCode=28015\"}},\"page\":{\"size\":20,\"totalElements\":2,\"totalPages\":1,\"number\":0}}";
            HttpResponse responseEvent = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
            responseEvent.setEntity(new ByteArrayEntity(eventValue.getBytes(StandardCharsets.UTF_8)));
            EntityResult bookingDaoER = new EntityResultMapImpl();
            bookingDaoER.setCode(OPERATION_SUCCESSFUL);
            bookingDaoER.put(BookingDao.ATTR_HOTEL_ID, List.of(1));
            EntityResult hotelER = new EntityResultMapImpl();
            hotelER.setCode(OPERATION_SUCCESSFUL);
            hotelER.put(HotelDao.ATTR_ZIP_ID, List.of(1));
            EntityResult postalCodeER = new EntityResultMapImpl();
            postalCodeER.setCode(OPERATION_SUCCESSFUL);
            postalCodeER.put(PostalCodeDao.ATTR_ZIP, List.of(1));
            postalCodeER.put(PostalCodeDao.ATTR_ISO_ID, List.of(1));
            EntityResult countryCodeER = new EntityResultMapImpl();
            countryCodeER.setCode(OPERATION_SUCCESSFUL);
            countryCodeER.put(CountryCodeDao.ATTR_ISO, List.of("ES"));
            when(httpClient.execute(any(HttpGet.class))).thenReturn(responseEvent);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(bookingDaoER);
            when(hotelService.hotelQuery(anyMap(), anyList())).thenReturn(hotelER);
            when(postalCodeService.postalCodeQuery(anyMap(), anyList())).thenReturn(postalCodeER);
            when(countryCodeService.countryCodeQuery(anyMap(), anyList())).thenReturn(countryCodeER);
            Map<String, Object> bookingKeyMap = new HashMap<>();
            bookingKeyMap.put(BookingDao.ATTR_ID, List.of(1));
            EntityResult finalER = bookingService.getEventsQuery(bookingKeyMap, BookingDao.getColumns());
            Assertions.assertEquals(2, ((List<?>)((List<?>) finalER.get("events")).get(0)).size());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
            verify(hotelService, times(1)).hotelQuery(anyMap(), anyList());
            verify(postalCodeService, times(1)).postalCodeQuery(anyMap(), anyList());
            verify(countryCodeService, times(1)).countryCodeQuery(anyMap(), anyList());
        }
    }
}