package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IRoomService {
    public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
    public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult roomUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult getRoomByIdQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
    public EntityResult getRoomByHotelIdQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
}
