package com.ontimize.dominiondiamondhotel.api.core.service;

import com.ontimize.jee.common.dto.EntityResult;

import java.util.List;
import java.util.Map;

public interface IPostalCodeService {
    public EntityResult postalCodeQuery(Map<String, Object> keyMap, List<String> attrList);
}
