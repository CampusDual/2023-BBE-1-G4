package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.ICustomerService;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Lazy
@Service("CustomerService")
public class CustomerService implements ICustomerService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private CustomerDao customerDao;


    @Override
    public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.customerDao, keyMap, attrList);
    }

    @Override
    public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.insert(this.customerDao, attrMap);
    }

    @Override
    public EntityResult customerUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.update(this.customerDao, attrMap, keyMap);
    }

    @Override
    public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.customerDao.delete(keyMap);
    }
}
