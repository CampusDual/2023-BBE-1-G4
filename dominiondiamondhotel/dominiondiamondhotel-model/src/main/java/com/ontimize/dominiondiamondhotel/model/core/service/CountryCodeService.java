package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.ICountryCodeService;
import com.ontimize.dominiondiamondhotel.model.core.dao.CountryCodeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Lazy
@Service("CountryCodeService")
public class CountryCodeService implements ICountryCodeService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private CountryCodeDao countryCodeDao;

    @Override
    public EntityResult countryCodeQuery(Map<String, Object> keyMap, List<String> attrList) {
        return this.daoHelper.query(this.countryCodeDao, keyMap, attrList);
    }
}
