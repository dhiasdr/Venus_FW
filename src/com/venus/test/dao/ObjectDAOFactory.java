package com.venus.test.dao;

public class ObjectDAOFactory {
  public IObjectDAO createInstance(){
	  return new ObjectDAOImpl();
  }
}
