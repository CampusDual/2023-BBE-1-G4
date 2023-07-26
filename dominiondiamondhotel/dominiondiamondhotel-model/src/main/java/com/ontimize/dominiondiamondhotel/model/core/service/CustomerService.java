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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ontimize.dominiondiamondhotel.api.core.utils.HelperUtils.INVALID_DATA;

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
        String idNumber = String.valueOf(attrMap.get(CustomerDao.ATTR_IDNUMBER));
        String phone = String.valueOf(attrMap.get(CustomerDao.ATTR_PHONE));
        int idTypeid = (int) attrMap.get(CustomerDao.ATTR_IDTYPE_ID);
        Map<String, Object> idDocKeyMap = new HashMap<>();
        idDocKeyMap.put(IdDocumentTypesDao.ATTR_ID, idTypeid);
        String email = String.valueOf(attrMap.get(CustomerDao.ATTR_EMAIL));
        EntityResult typeIdExist = this.daoHelper.query(this.idDocumentTypesDao, idDocKeyMap, List.of(IdDocumentTypesDao.ATTR_IDTYPE));
        Map<String, Object> customerKeyMap = new HashMap<>();
        customerKeyMap.put(CustomerDao.ATTR_IDNUMBER, idNumber);
        EntityResult idDocNumberAlreadyExist = this.daoHelper.query(this.customerDao, customerKeyMap, List.of(CustomerDao.ATTR_ID));
        EntityResult er = new EntityResultMapImpl();
        if (typeIdExist.get(IdDocumentTypesDao.ATTR_IDTYPE) != null &&
                ValidatorUtils.idValidator(idTypeid, idNumber) &&
                ValidatorUtils.emailValidator(email) &&
                idDocNumberAlreadyExist.get(IdDocumentTypesDao.ATTR_ID) == null &&
                ValidatorUtils.phoneValidator(phone)) {
            return this.daoHelper.insert(this.customerDao, attrMap);
        } else {
            er.setMessage(INVALID_DATA);
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult customerUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap) throws OntimizeJEERuntimeException {
        String idNumber = String.valueOf(keyMap.get(CustomerDao.ATTR_IDNUMBER));
        EntityResult entityResult = this.daoHelper.query(this.customerDao, keyMap, List.of(CustomerDao.ATTR_IDTYPE_ID));
        int idTypeid = (int) ((List<?>) entityResult.get(CustomerDao.ATTR_IDTYPE_ID)).get(0);
        String email = String.valueOf(attrMap.get(CustomerDao.ATTR_EMAIL));
        String phone = String.valueOf(attrMap.get(CustomerDao.ATTR_PHONE));
        EntityResult er = new EntityResultMapImpl();
        if (ValidatorUtils.idValidator(idTypeid, idNumber) && ValidatorUtils.emailValidator(email) && ValidatorUtils.phoneValidator(phone)) {
            return this.daoHelper.update(this.customerDao, attrMap, keyMap);
        } else {
            er.setMessage(INVALID_DATA);
        }
        er.setCode(EntityResult.OPERATION_WRONG);
        return er;
    }

    @Override
    public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
        return this.daoHelper.delete(this.customerDao, keyMap);
    }
}
