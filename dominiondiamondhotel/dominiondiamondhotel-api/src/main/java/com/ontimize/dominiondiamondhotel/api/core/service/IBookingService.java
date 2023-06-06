package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import org.springframework.security.core.parameters.P;

import java.util.Map;

public interface IBookingService {
    public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckInUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult bookingCheckOutUpdate(Map<String, Object> keyMap) throws  OntimizeJEERuntimeException;
    public EntityResult bookingCalificationsAndCommentUpdate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
}
