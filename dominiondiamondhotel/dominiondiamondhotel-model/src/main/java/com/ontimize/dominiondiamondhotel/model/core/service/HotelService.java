package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IHotelService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.HotelDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.PostalCodeDao;
import com.ontimize.dominiondiamondhotel.model.core.utils.BookingUtils;
import com.ontimize.dominiondiamondhotel.model.core.utils.HotelUtils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.text.Keymap;
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
    public EntityResult filteredGet(Map<?, ?> keyMap, List<SQLStatementBuilder.SQLOrder> orderByList) throws OntimizeJEERuntimeException {

        Map<?, ?> filter = (Map<?, ?>) keyMap.get(FILTER);
        Map<String, Object> filterMap = new HashMap<>();

        if(filter.get("zip") == null){

            Double min = Double.parseDouble(String.valueOf(filter.get("qualitymin")));
            Double max = Double.parseDouble(String.valueOf(filter.get("qualitymax")));
            filterMap.put("min", min);
            filterMap.put("max", max);
            filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, HotelUtils.notLess(min));
            filterMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, HotelUtils.notMore(max));

        }else{

            filterMap.put(HotelDao.ATTR_ZIP_ID, Integer.parseInt(String.valueOf(filter.get("zip"))));

        }

        return this.daoHelper.query(this.hotelDao, filterMap, List.of("*"), orderByList, "filteredget");

    }

}
