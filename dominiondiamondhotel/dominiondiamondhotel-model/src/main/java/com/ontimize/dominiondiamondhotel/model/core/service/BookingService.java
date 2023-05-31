package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IBookingService;
import com.ontimize.dominiondiamondhotel.model.core.dao.BookingDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;

import java.util.Map;

@Lazy
@Service("BookingService")
public class BookingService implements IBookingService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private BookingDao bookingDao;


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
}
