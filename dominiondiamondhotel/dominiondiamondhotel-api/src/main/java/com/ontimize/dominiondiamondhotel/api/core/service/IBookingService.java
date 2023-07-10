package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IBookingService {
    public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckInUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckOutUpdate(Map<String, Object> keyMap) throws  OntimizeJEERuntimeException;
    public EntityResult bookingCalificationsAndCommentUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult getForecastQuery(Map<String, Object> keyMap, List<String> attrList);
    public EntityResult bookingExpenseUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult payExpenses(Map<String, Object> keyMap) throws  OntimizeJEERuntimeException;
}
