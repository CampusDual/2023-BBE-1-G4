package com.ontimize.dominiondiamondhotel.model.core.service;

import com.ontimize.dominiondiamondhotel.api.core.service.ICustomerService;
import com.ontimize.dominiondiamondhotel.api.core.utils.ValidatorUtils;
import com.ontimize.dominiondiamondhotel.model.core.dao.CustomerDao;
import com.ontimize.dominiondiamondhotel.model.core.dao.IdDocumentTypesDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Lazy
@Service("CustomerService")
public class CustomerService implements ICustomerService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private IdDocumentTypesDao idDocumentTypesDao;

    @Override
    public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
        return this.daoHelper.query(this.customerDao, keyMap, attrList);
    }

    @Override
    public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
        String idNumber = String.valueOf(attrMap.get("idnumber"));
        String phone = String.valueOf(attrMap.get("phone"));
        int idTypeid = (int) attrMap.get("idtype_id");
        Map<String, Object> idDocKeyMap = new HashMap<>();
        idDocKeyMap.put(IdDocumentTypesDao.ATTR_ID, idTypeid);
        List<String> idDocAttrList = new ArrayList<>();
        idDocAttrList.add(IdDocumentTypesDao.ATTR_IDTYPE);
        String email = String.valueOf(attrMap.get("mail"));
        EntityResult typeIdExist = this.daoHelper.query(this.idDocumentTypesDao, idDocKeyMap, idDocAttrList);
        Map<String, Object> customerKeyMap = new HashMap<>();
        customerKeyMap.put(CustomerDao.ATTR_IDNUMBER, idNumber);
        List<String> customerAttrList = new ArrayList<>();
        customerAttrList.add(CustomerDao.ATTR_ID);
        EntityResult idDocNumberAlreadyExist = this.daoHelper.query(this.customerDao, customerKeyMap, customerAttrList);
        EntityResult er = new EntityResultMapImpl();
        if (typeIdExist.get("idtype") != null &&
                ValidatorUtils.idValidator(idTypeid, idNumber) &&
                ValidatorUtils.emailValidator(email) &&
                idDocNumberAlreadyExist.get("id") == null &&
                ValidatorUtils.phoneValidator(phone)) {
            return this.daoHelper.insert(this.customerDao, attrMap);
        } else {
            er.setMessage("Invalid data");
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult customerUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        String idNumber = String.valueOf(keyMap.get("idnumber"));
        ArrayList<String> idtype = new ArrayList<>();
        idtype.add(CustomerDao.ATTR_IDTYPE_ID.toLowerCase());
        EntityResult entityResult = this.daoHelper.query(this.customerDao, keyMap, idtype);
        int idTypeid = (int) ((List<?>) entityResult.get("idtype_id")).get(0);
        String email = String.valueOf(attrMap.get("mail"));
        String phone = String.valueOf(attrMap.get("phone"));
        EntityResult er = new EntityResultMapImpl();
        if (ValidatorUtils.idValidator(idTypeid, idNumber) && ValidatorUtils.emailValidator(email) && ValidatorUtils.phoneValidator(phone)) {
            return this.daoHelper.update(this.customerDao, attrMap, keyMap);
        } else {
            er.setMessage("Invalid data");
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.delete(this.customerDao, keyMap);
    }


}
