package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IHotelService {
    public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
    public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult hotelUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult hotelPaginationQuery(Map<?, ?> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult hotelOccupationQuery(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
}
