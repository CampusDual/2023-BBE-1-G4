package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IHotelService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.PostalCodeDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.BookingUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.HotelUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.dominiondiamondhotel.model.core.utils.HotelUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.db.SQLStatementBuilder.ExtendedSQLConditionValuesProcessor;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.text.Keymap;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.FILTER;

@Lazy
@Service("HotelService")
public class HotelService implements IHotelService {
    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private HotelDao hotelDao;

    @Autowired
    PostalCodeDao postalCodeDao;

    @Autowired
    private BookingDao bookingDao;

    private static final String MONTH = "month";
    private static final String YEAR = "year";

    @Override
    public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.hotelDao, keyMap, attrList);
    }

    @Override
    public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.insert(this.hotelDao, attrMap);
    }

    @Override
    public EntityResult hotelUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
    }

    @Override
    public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.delete(this.hotelDao, keyMap);
    }

    @Override
    public EntityResult hotelPaginationQuery(Map<?, ?> keyMap) throws OntimizeJEERuntimeException {

        Map<?, ?> filter = (Map<?, ?>) keyMap.get(FILTER);
        Map<String, Object> filterMap = new HashMap<>();
        List<SQLStatementBuilder.SQLOrder> orderByList = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        columns.add(HotelDao.ATTR_NAME);
        columns.add(HotelDao.ATTR_ID);
        columns.add(HotelDao.ATTR_RATING);
        columns.add(HotelDao.ATTR_POPULARITY);
        columns.add(HotelDao.ATTR_ZIP_ID);

        if(filter.get("zip") == null){

            Double min = Double.parseDouble(String.valueOf(filter.get("qualitymin")));
            Double max = Double.parseDouble(String.valueOf(filter.get("qualitymax")));
            if(min > 0 && max <= 10) {
                filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, HotelUtils.andExpression(HotelUtils.moreThan(min), HotelUtils.lessThan(max)));
            }else{
                EntityResult res = new EntityResultMapImpl();
                res.setCode(EntityResult.OPERATION_WRONG);
                return res;
            }

        }else{

            filterMap.put(HotelDao.ATTR_ZIP_ID, Integer.parseInt(String.valueOf(filter.get("zip"))));

        }

        if(filter.get("popularity") != null){

            orderByList.add(new SQLStatementBuilder.SQLOrder("popularity", (boolean) filter.get("popularity")));

        }

        return this.daoHelper.query(this.hotelDao, filterMap, columns, orderByList, "filteredget");

    }

    public EntityResult hotelOccupationQuery(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        Map<?, ?> getFilter = (Map<?, ?>) keyMap.get(FILTER);
        int hotelId = Integer.parseInt(String.valueOf(getFilter.get("hotel_id")));
        Map<String, Object> hotelExistKeyMap = new HashMap<>();
        BasicExpression be = HotelUtils.searchBy(BasicOperator.EQUAL_OP, HotelDao.ATTR_ID, String.valueOf(hotelId));
        if (be != null) {
            hotelExistKeyMap.put(ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, be);
        }
        EntityResult hotelExists = this.hotelQuery(hotelExistKeyMap, HotelDao.getColumns());
        if (((List<?>) hotelExists.get(HotelDao.ATTR_ID)).get(0) != null) {
            LocalDate initialDate = null;
            LocalDate endDate = null;
            if (getFilter.get("initial_date") != null && getFilter.get("end_date") != null) {
                initialDate = LocalDate.parse(String.valueOf(getFilter.get("initial_date")));
                endDate = LocalDate.parse(String.valueOf(getFilter.get("end_date")));
            }
            if (getFilter.get(MONTH) != null && getFilter.get(YEAR) != null) {
                initialDate = LocalDate.parse((getFilter.get(YEAR) + "-" + getFilter.get(MONTH) + "-01"));
                endDate = initialDate.withDayOfMonth(initialDate.getMonth().length(initialDate.isLeapYear()));
            }
            if (getFilter.get(YEAR) != null && getFilter.get(MONTH) == null) {
                initialDate = LocalDate.parse(getFilter.get(YEAR) + "-01-01");
                endDate = LocalDate.parse(getFilter.get(YEAR) + "-12-31");
            }
            if (initialDate != null && endDate != null) {
                Map<String, Object> bookingKeyMap = new HashMap<>();
                BasicField entryDate = new BasicField(BookingDao.ATTR_ENTRY_DATE);
                BasicField entryDateToCompare = new BasicField("'"+initialDate+"'");
                BasicField finalDate = new BasicField(BookingDao.ATTR_EXIT_DATE);
                BasicField finalDateToCompare = new BasicField("'"+endDate+"'");
                bookingKeyMap.put(ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, basicExpressionBetween(entryDate, entryDateToCompare, finalDate, finalDateToCompare));
                bookingKeyMap.put(BookingDao.ATTR_HOTEL_ID, hotelId);
                EntityResult bookings = daoHelper.query(this.bookingDao, bookingKeyMap, bookingDao.getColumns());
                if (bookings != null) {
                    double occupation = 0.0;
                    LocalDate initDate = initialDate;
                    double totalRooms = Double.parseDouble(String.valueOf(((List<?>)hotelExists.get(HotelDao.ATTR_TOTALROOMS)).get(0)));
                    do {
                        int roomCount = 0;
                        for (int i = 0; i < ((List<?>) bookings.get(BookingDao.ATTR_ENTRY_DATE)).size(); i++) {
                            LocalDate actualEntryDate = LocalDate.parse(String.valueOf(((List<?>) bookings.get(BookingDao.ATTR_ENTRY_DATE)).get(0)));
                            LocalDate actualEndDate = LocalDate.parse(String.valueOf(((List<?>) bookings.get(BookingDao.ATTR_EXIT_DATE)).get(0)));
                            if ((initDate.isEqual(actualEntryDate) || initDate.isAfter(actualEntryDate)) &&
                                    (initDate.isEqual(actualEndDate) || initDate.isBefore(actualEndDate))) {
                                roomCount++;
                            }
                        }
                        occupation += roomCount / totalRooms;
                        initDate = initDate.plusDays(1);
                    } while (initDate.isBefore(endDate));
                    double periodOccupation = (occupation*100) / (Duration.between(initialDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1);
                    EntityResult result = new EntityResultMapImpl();
                    Map<String, Object> data = new HashMap<>();
                    data.put("occupation", periodOccupation);
                    result.setCode(EntityResult.OPERATION_SUCCESSFUL);
                    result.put("data", data);
                    return result;
                }
            }
        }
        EntityResult res = new EntityResultMapImpl();
        res.setCode(EntityResult.OPERATION_WRONG);
        return res;
    }

    private BasicExpression basicExpressionBetween(BasicField entryDate, BasicField entryDateToCompare, BasicField finalDate, BasicField finalDateToCompare){
        BasicExpression initialDateBe1 = new BasicExpression(entryDate, BasicOperator.MORE_EQUAL_OP, entryDateToCompare);
        BasicExpression initialDateBe2 = new BasicExpression(finalDate, BasicOperator.MORE_EQUAL_OP, entryDateToCompare);
        BasicExpression finalDateBe1 = new BasicExpression(entryDate, BasicOperator.LESS_EQUAL_OP, finalDateToCompare);
        BasicExpression finalDateBe2 = new BasicExpression(finalDate, BasicOperator.LESS_EQUAL_OP, finalDateToCompare);
        BasicExpression beBookings1 = new BasicExpression(initialDateBe1, BasicOperator.AND_OP, finalDateBe1);
        BasicExpression beBookings2 = new BasicExpression(initialDateBe2, BasicOperator.AND_OP, finalDateBe2);
        return new BasicExpression(beBookings1, BasicOperator.OR_OP, beBookings2);
    }
}
