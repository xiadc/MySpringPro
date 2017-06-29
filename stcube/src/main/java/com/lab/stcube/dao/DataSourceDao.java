package com.lab.stcube.dao;

import java.util.List;
import java.util.Map;


public interface DataSourceDao {
	
	public List<Map<String, String>> queryAllData(String filepath);

}
