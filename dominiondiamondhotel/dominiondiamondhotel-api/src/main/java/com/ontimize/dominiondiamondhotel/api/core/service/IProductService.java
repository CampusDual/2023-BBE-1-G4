package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

import java.util.List;
import java.util.Map;

public interface IProductService {

    public EntityResult productInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

    public EntityResult productPaginationQuery(Map<?,?> keysValues, List<?> attributesValues, int pagesize, int offset, List<SQLStatementBuilder.SQLOrder> orderby) throws OntimizeJEERuntimeException;

}
