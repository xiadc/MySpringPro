package com.lab.stcube.service;

import java.util.List;
import java.util.Map;

import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.stcube.algorithm.IAlgorithm;
import com.lab.stcube.dao.FeatureDao;
import com.lab.stcube.model.SpatioTemporalFeatureSet;
import com.lab.stcube.transform.IDataTransform;

@Service
public class TestService {
	
	@Autowired
	private IDataTransform dataTransform;
	
	@Autowired
	private FeatureDao featureDao;
	
	public SpatioTemporalFeatureSet getBasedata(){
		//SpatioTemporalFeatureSet st_objects = dataTransform.transferDataToSTFeatureSet();
		SpatioTemporalFeatureSet st_objects = featureDao.queryAllSpatioTemporalFeature();
		try {
			st_objects.changeCRS("EPSG:3857");
		} catch (MismatchedDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st_objects;
	}
	
	public List<Map<String, Object>> getBasedata1(){
		//SpatioTemporalFeatureSet st_objects = dataTransform.transferDataToSTFeatureSet();
		List<Map<String, Object>> st_objects = featureDao.countTile2d(76.26000, 77.37000, 61.3000, 65.4000, 100);
		return st_objects;
	}

}
