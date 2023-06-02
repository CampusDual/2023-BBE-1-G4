package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IBookingService {
    public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckOutUpdate(Map<String, Object> keyMap) throws  OntimizeJEERuntimeException;
}
