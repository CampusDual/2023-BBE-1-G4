package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IHistRoomService {
    public EntityResult histroomQuery(Map<String, Object> attrMap, List<String> columns) throws OntimizeJEERuntimeException;
}
