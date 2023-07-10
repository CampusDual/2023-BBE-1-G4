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
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.*;
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
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            when(daoHelper.query(any(RoomDao.class), anyMap(), anyList())).thenReturn(roomUpdate);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList(), anyString())).thenReturn(hotelEr);
            when(roomService.roomUpdate(anyMap(), anyMap())).thenReturn(er2);
            EntityResult result = bookingService.bookingCheckOutUpdate(bookingToInsert);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(2)).query(any(BookingDao.class), anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
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
            verify(customerService, times(1)).customerQuery(anyMap(), anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
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
            when(daoHelper.query(any(CustomerDao.class), anyMap(), anyList())).thenReturn(customerER);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(updateER);
            EntityResult result = bookingService.payExpenses(filter, data);
            Assertions.assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).query(any(BookingDao.class),anyMap(),anyList());
            verify(daoHelper, times(1)).query(any(CustomerDao.class),anyMap(),anyList());
            verify(daoHelper, times(1)).update(any(BookingDao.class),anyMap(),anyMap());
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class BookingServiceForecast {
        @Test
        void testForecast() throws IOException {
            String forecastValue = "{\"Headline\":{\"EffectiveDate\":\"2023-07-03T08:00:00+02:00\",\"EffectiveEpochDate\":1688364000,\"Severity\":3,\"Text\":\"The heat wave will continue through Tuesday\",\"Category\":\"heat\",\"EndDate\":\"2023-07-04T20:00:00+02:00\",\"EndEpochDate\":1688493600,\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?lang=en-us\"},\"DailyForecasts\":[{\"Date\":\"2023-07-03T07:00:00+02:00\",\"EpochDate\":1688360400,\"Temperature\":{\"Minimum\":{\"Value\":69.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":96.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":3,\"IconPhrase\":\"Partly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":34,\"IconPhrase\":\"Mostly clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=1&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=1&lang=en-us\"},{\"Date\":\"2023-07-04T07:00:00+02:00\",\"EpochDate\":1688446800,\"Temperature\":{\"Minimum\":{\"Value\":67.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":94.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=2&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=2&lang=en-us\"},{\"Date\":\"2023-07-05T07:00:00+02:00\",\"EpochDate\":1688533200,\"Temperature\":{\"Minimum\":{\"Value\":66.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":94.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":35,\"IconPhrase\":\"Partly cloudy\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=3&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=3&lang=en-us\"},{\"Date\":\"2023-07-06T07:00:00+02:00\",\"EpochDate\":1688619600,\"Temperature\":{\"Minimum\":{\"Value\":65.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":92.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=4&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=4&lang=en-us\"},{\"Date\":\"2023-07-07T07:00:00+02:00\",\"EpochDate\":1688706000,\"Temperature\":{\"Minimum\":{\"Value\":66.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":92.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Icon\":1,\"IconPhrase\":\"Sunny\",\"HasPrecipitation\":false},\"Night\":{\"Icon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false},\"Sources\":[\"AccuWeather\"],\"MobileLink\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=5&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/es/madrid/28015/daily-weather-forecast/95226_pc?day=5&lang=en-us\"}]}";
            String keyValue = "[{\"Version\":1,\"Key\":\"95226_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Madrid\",\"EnglishName\":\"Madrid\",\"PrimaryPostalCode\":\"28015\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"ES\",\"LocalizedName\":\"Spain\",\"EnglishName\":\"Spain\"},\"AdministrativeArea\":{\"ID\":\"MD\",\"LocalizedName\":\"Madrid\",\"EnglishName\":\"Madrid\",\"Level\":1,\"LocalizedType\":\"Autonomous Community\",\"EnglishType\":\"Autonomous Community\",\"CountryID\":\"ES\"},\"TimeZone\":{\"Code\":\"CEST\",\"Name\":\"Europe/Madrid\",\"GmtOffset\":2.0,\"IsDaylightSaving\":true,\"NextOffsetChange\":\"2023-10-29T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":40.429,\"Longitude\":-3.71,\"Elevation\":{\"Metric\":{\"Value\":667.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":2188.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"Alerts\",\"DailyPollenForecast\",\"ForecastConfidence\",\"FutureRadar\",\"MinuteCast\",\"Radar\"],\"Details\":{\"Key\":\"95226_PC\",\"StationCode\":\"LEA6\",\"StationGmtOffset\":1.0,\"BandMap\":\"SP\",\"Climo\":\"LEVS\",\"LocalRadar\":\"\",\"MediaRegion\":null,\"Metar\":\"LEMD\",\"NXMetro\":\"\",\"NXState\":\"\",\"Population\":null,\"PrimaryWarningCountyCode\":\"\",\"PrimaryWarningZoneCode\":\"MDZ722802\",\"Satellite\":\"EUR3\",\"Synoptic\":\"08220\",\"MarineStation\":\"\",\"MarineStationGMTOffset\":null,\"VideoCode\":\"\",\"LocationStem\":\"es/madrid/28015\",\"PartnerID\":null,\"Sources\":[{\"DataType\":\"AirQualityCurrentConditions\",\"Source\":\"Plume Labs\",\"SourceId\":63},{\"DataType\":\"AirQualityForecasts\",\"Source\":\"Plume Labs\",\"SourceId\":63},{\"DataType\":\"Alerts\",\"Source\":\"State Meteorological Agency\",\"SourceId\":39},{\"DataType\":\"CurrentConditions\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"DailyForecast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"DailyPollenForecast\",\"Source\":\"Copernicus Atmosphere Monitoring Service\",\"SourceId\":78},{\"DataType\":\"ForecastConfidence\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"FutureRadar\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"Historical\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"HourlyForecast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"MinuteCast\",\"Source\":\"AccuWeather\",\"SourceId\":1},{\"DataType\":\"Radar\",\"Source\":\"State Meteorological Agency\",\"SourceId\":39}],\"CanonicalPostalCode\":\"28015\",\"CanonicalLocationKey\":\"95226_PC\"}}]";            HttpResponse responseKey = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
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
}