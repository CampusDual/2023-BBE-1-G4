package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.IPostalCodeService;
import com.ontimize.dominiondiamondhotel.model.core.dao.PostalCodeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Lazy
@Service("PostalCodeService")
public class PostalCodeService implements IPostalCodeService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private PostalCodeDao postalCodeDao;

    @Override
    public EntityResult postalCodeQuery(Map<String, Object> keyMap, List<String> attrList) {
        return this.daoHelper.query(this.postalCodeDao, keyMap, attrList);
    }
}
