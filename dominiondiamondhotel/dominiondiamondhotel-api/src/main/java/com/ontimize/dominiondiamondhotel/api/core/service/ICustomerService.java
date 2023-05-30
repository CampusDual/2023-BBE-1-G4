package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface ICustomerService {
    public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
    public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
    public EntityResult customerUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException;
    public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
}
