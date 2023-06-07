package com.ontimize.dominiondiamondhotel.api.core.service;


import com.ontimize.jee.common.dto.EntityResult;

import java.util.List;
import java.util.Map;


public interface IUserService {
	public EntityResult userQuery(Map<?, ?> keyMap, List<?> attrList);
	public EntityResult userInsert(Map<?, ?> attrMap);
	public EntityResult userUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap);
	public EntityResult userDelete(Map<?, ?> keyMap);
}
