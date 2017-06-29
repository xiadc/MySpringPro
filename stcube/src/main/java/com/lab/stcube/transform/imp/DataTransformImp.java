package com.lab.stcube.transform.imp;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.geotools.feature.SchemaException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lab.stcube.dao.DataSourceDao;
import com.lab.stcube.dao.FeatureDao;
import com.lab.stcube.dao.imp.ExcelDaoImp;
import com.lab.stcube.dao.imp.MongoFeatureDaoImp;
import com.lab.stcube.model.GeomTypeEnum;
import com.lab.stcube.model.SpatioTemporalFeature;
import com.lab.stcube.model.SpatioTemporalFeatureSet;
import com.lab.stcube.transform.IDataTransform;
import com.vividsolutions.jts.io.ParseException;


/**
 * 
 */
@Component
public class DataTransformImp implements IDataTransform {

    /**
     * Default constructor
     */
    public DataTransformImp() {
    	
    }

    /**
     * 
     */
    public FeatureDao featureDao;
    @Autowired
    public DataSourceDao dataSourceDao;

    /**
     * 
     */
    public SpatioTemporalFeatureSet st_objects;


    /**
     * 
     */
    public SpatioTemporalFeatureSet transferDataToSTFeatureSet() {
        // TODO implement here
    	Properties prop = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("dataSourcePath.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String filepath = prop.getProperty("excelFilepath");
		String [] attributes = prop.getProperty("attributes").split(",");
		String timePeriod = prop.getProperty("timePeriod");
		String timeFormat = prop.getProperty("timeFormat");
		String geometryType = prop.getProperty("geometry").split(":")[0];
		String [] geometryAttributes = prop.getProperty("geometry").split(":")[1].split(",");
		String CRS = prop.getProperty("CRS");
		dataSourceDao = new ExcelDaoImp();
		List<Map<String, String>> list = dataSourceDao.queryAllData(filepath);
		
		String typeSpec = "";
		for (String a : attributes){
			typeSpec += a.trim() + ":String,";
		}
		typeSpec = typeSpec.substring(0, typeSpec.length()-1);
		GeomTypeEnum geomType = null;
		switch(geometryType){
		case "point":
			geomType = GeomTypeEnum.Point;
//		case "lingString":
//			geomType = GeomTypeEnum.LingString;
//		case "polygon":
//			geomType = GeomTypeEnum.Polygon;
//		case "lineaRing":
//			geomType = GeomTypeEnum.LineaRing;
		}
		try {
			st_objects=new SpatioTemporalFeatureSet();
			st_objects.setFeatureType(typeSpec, geomType, CRS);
			for (Map<String, String> map : list){
				String wktStr ="POINT ("+map.get(geometryAttributes[0])+" "+map.get(geometryAttributes[1])+")";
				SpatioTemporalFeature stFtest=new SpatioTemporalFeature();
				stFtest.setFeatureType(st_objects.getFeatureType());
				for (String attribute : attributes){
					stFtest.setAttribute(attribute, map.get(attribute));
				}
				stFtest.setTimePeriod(map.get(timePeriod), timeFormat);	
				stFtest.setGeometry(wktStr);

				st_objects.addStObject(stFtest);
			}
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (MismatchedDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st_objects;
					
    }

    /**
     * 
     */
    public void persistSTFeatureSet() {
        // TODO implement here
    	transferDataToSTFeatureSet();
    	featureDao = new MongoFeatureDaoImp();
    	featureDao.addSTFeatureSet(st_objects);
    }
    
    public FeatureDao getFeatureDao(){
    	return featureDao;
    }
    
    public void setFeatureDao(FeatureDao featureDao){
    	this.featureDao = featureDao;
    }
    
    public DataSourceDao getDataSourceDao(){
    	return dataSourceDao;
    }
    
    public void setDataSourceDao(DataSourceDao dataSourceDao){
    	this.dataSourceDao = dataSourceDao;
    }
    
    public SpatioTemporalFeatureSet getSt_objects(){
    	return this.st_objects;
    }
    
    public void setSt_objects(SpatioTemporalFeatureSet st_objects){
    	this.st_objects = st_objects;
    }
    
    public static void main(String[] args) throws IOException {
		DataTransformImp dImp = new DataTransformImp();
		dImp.persistSTFeatureSet();
//		dImp.setDataSourceDao(new ExcelDaoImp());
//		dImp.transferDataToSTFeatureSet();
//		DataSourceDao dataSourceDao = dImp.getDataSourceDao();
//		Properties prop = new Properties();
//		InputStream in = dImp.getClass().getClassLoader().getResourceAsStream("dataSourcePath.properties");
//		prop.load(in);
//		String filepath = prop.getProperty("excelFilepath");
//		List<Map<String, String>> list = dataSourceDao.queryAllData(filepath);
//		for (Map<String, String> l : list){
//			Set<String> keys = l.keySet();
//			for (String key : keys){
//				System.out.print(l.get(key) + "...");
//			}
//			System.out.print("\r\n");
//		}

	}
    
}