package com.venus.test.service;

import com.venus.test.dao.IObjectDAO;

public class ObjectServiceImpl implements IObjectService {
    IObjectDAO objectDAO;
    
	public IObjectDAO getObjectDAO() {
		return objectDAO;
	}

	public void setObjectDAO(IObjectDAO objectDAO) {
		this.objectDAO = objectDAO;
	}

	@Override
	public void serviceWork() {
		this.objectDAO.work();
	}

}
