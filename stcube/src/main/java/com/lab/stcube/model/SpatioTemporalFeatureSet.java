package com.lab.stcube.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
//import org.geotools.data.shapefile.ShapefileDataStore;
//import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.*;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 */
public class SpatioTemporalFeatureSet {

    /**
     * Default constructor
     */
    public SpatioTemporalFeatureSet() {
    	  //create the builder
    	m_stObjects = new ArrayList<SpatioTemporalFeature>();
    }

    /**
     * 
     */
    private List<SpatioTemporalFeature> m_stObjects;
    
    /**
     * 构建器
     */
    private SimpleFeatureBuilder m_builder=null;
    
    /**
     * 类型构建器
     
    private SimpleFeatureTypeBuilder m_typeBuilder;
    */
    
    /**
     * 
     */
    public TimePeriod m_timePeriod=new TimePeriod();

    /**
     * 
     */
    private String m_CRS;

    /**
     * 
     */
    private String m_stId;

    /**
     * 
     */
    private MBR m_mbr;
    
    private int m_srid=4326;

    /*
    //添加字段类型
    public SpatioTemporalFeatureSet setField(String typeNmae,Object typeClass) {
		return this;
	}
    */
    
    //获取空集合
    public SpatioTemporalFeatureSet getNullSet() {
		return null;
	}
    
    
    
    
    /**
     * @return
     */
    public void setFeatureType(SimpleFeatureType type) {
    	if(m_builder != null)return;
    	m_builder = new SimpleFeatureBuilder(type);  
    	//m_typeBuilder.setCharset(Charset.forName("GBK"));  
        return;
    } 
    
    public void setFeatureType(String typeSpec,GeomTypeEnum geomType,String epsgCode) throws SchemaException, NoSuchAuthorityCodeException, FactoryException{
    	if(m_builder != null)return;
    	String sridStr=epsgCode.split(":")[1];
    	SimpleFeatureType originalType = DataUtilities.createType("t1","the_geom:"+geomType.toString()+":srid="+sridStr+","+typeSpec);
    	m_builder = new SimpleFeatureBuilder(originalType);  
    	m_srid=Integer.parseInt(sridStr);
    	return;
    } 
    
    /**
     * @return
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    public void setFeatureType(String typeSpec,GeomTypeEnum geomType) throws SchemaException, NoSuchAuthorityCodeException, FactoryException{
    	setFeatureType(typeSpec, geomType, "EPSF:"+m_srid);
        return;
    } 
    
    /**
     * @return
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    public SimpleFeatureType getFeatureType() throws NoSuchAuthorityCodeException, FactoryException {
        return SimpleFeatureTypeBuilder.retype(m_builder.getFeatureType(), getCRS());
    } 
    
    public String getFeatureTypeName() {
    	return m_builder.getFeatureType().getTypeName();
	}
    
    public List<String> getFieldNames() {
    	List<AttributeDescriptor> list = m_builder.getFeatureType().getAttributeDescriptors();
    	List<String> names=new ArrayList<String>();
    	int i=1;
    	for (AttributeDescriptor attrides : list) {
    		if (--i<=0) continue;
    		names.add(attrides.getLocalName());
		}
    	return names;
	}
    
    public boolean isFieldExist(String name) {
    	List<AttributeDescriptor> list = m_builder.getFeatureType().getAttributeDescriptors();
    	for (AttributeDescriptor attrides : list) {
    		if(attrides.getLocalName().equals(name))return true;
		}
    	return false;
	}

    /**
     * @return
     */
    public TimePeriod getTimePeriod() {
        return m_timePeriod;
    }

    /**
     * @return
     */
    public GeomTypeEnum getGeometryType() {
        return m_stObjects.get(0).getGeometryType();
    }
    
    /**
     * @return
     */
    public String getGeometryTypeInWkt() {
        return m_stObjects.get(0).getGeometryTypeInWkt();
    }
    
    public List<SpatioTemporalFeature> getStFeatureList() {
    	return m_stObjects;
	}

    /**
     * @param SpatioTemporalObject 
     * @return
     * @throws TransformException 
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     * @throws MismatchedDimensionException 
     */
    public boolean addStObject(SpatioTemporalFeature obj) throws MismatchedDimensionException, NoSuchAuthorityCodeException, FactoryException, TransformException {
    	if(obj.getFeatureTypeName()==m_builder.getFeatureType().getTypeName())
    	{
    			obj.changeCRS(m_srid);
    			m_stObjects.add(obj);
    			if(obj.getTimePeriod().getBegin()<m_timePeriod.getBegin())m_timePeriod.setBegin(obj.getTimePeriod().getBegin());
    			if(obj.getTimePeriod().getEnd()>m_timePeriod.getEnd())m_timePeriod.setEnd(obj.getTimePeriod().getEnd());
    	}
    	else
    		return false;  //抛出类型不一致问题
        return true;
    }
    
    /**
     * @param Arrays[] 
     * @return
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    public boolean addStObject(SpatioTemporalFeature[] arr) throws NoSuchAuthorityCodeException, FactoryException {
    	if(arr.length>0)
    		for (SpatioTemporalFeature arr_stobject : arr) {
				if(arr_stobject.getFeatureType().getTypeName()==m_builder.getFeatureType().getTypeName()){
					m_stObjects.add(arr_stobject);
					
	    			if(arr_stobject.getTimePeriod().getBegin()<m_timePeriod.getBegin())m_timePeriod.setBegin(arr_stobject.getTimePeriod().getBegin());
	    			if(arr_stobject.getTimePeriod().getEnd()>m_timePeriod.getBegin())m_timePeriod.setEnd(arr_stobject.getTimePeriod().getEnd());
				}
			}
    	else 
    		return false;
    	return true;
	}
    
    /**
     * @param String 
     * @return
     */
    public SpatioTemporalFeature findStObject(String t) {
        // TODO implement here
    	//------------
    	for (SpatioTemporalFeature stobject : m_stObjects) {
    		
    	}
        return null;
    }

    /**
     * @param int num 
     * @param int index   
     * @return
     */
    public List<SpatioTemporalFeature> getStObeject(int num,int index  ) {
        return m_stObjects.subList(index, index+num);
    }
    
    /**
     * @param int num 
     * @return
     */
    public List<SpatioTemporalFeature> getStObeject(int num) {
        return m_stObjects.subList(0,num);
    }

    /**
     * @param int index 
     * @return
     */
    public SpatioTemporalFeature getStObjectByIndex(int index) {
        return m_stObjects.get(index);
    }
    
    /**
     * @param long id
     * @return
     */
    public SpatioTemporalFeature getStObjectById(long id) {
        for (SpatioTemporalFeature stfeature : m_stObjects) {
			if(stfeature.getStId()==id)
				return stfeature;
		}
    	return null;
    }

    /**
     * @param int index 
     * @return
     */
    public SpatioTemporalFeature removeStObject(int index) {
        return m_stObjects.remove(index);
    }

    /**
     * @param String
     */
    public boolean isAtrributeExist(String atrrName) {
    	return m_builder.getFeatureType().getDescriptor(atrrName)==null?false:true;
    }

    /**
     * @param String
     * @throws TransformException 
     * @throws MismatchedDimensionException 
     * @throws FactoryException 
     */
    public void changeCRS(String epsgStr) throws MismatchedDimensionException, TransformException, FactoryException {
    	//if(m_stId)
    	CoordinateReferenceSystem targetCRS = CRS.decode(epsgStr);
    	
    	//坐标转化
    	MathTransform tf = CRS.findMathTransform(getCRS(), targetCRS); //m_feature.getDefaultGeometry();
    	for (SpatioTemporalFeature stfeature : m_stObjects) {
        	stfeature.setGeometry(JTS.transform((Geometry)stfeature.getGeometry(),tf));
		}
    	m_srid=Integer.parseInt(epsgStr.split(":")[1]);
    }

    /**
     * @return
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    public CoordinateReferenceSystem getCRS() throws NoSuchAuthorityCodeException, FactoryException {
       // return m_builder.getFeatureType().getCoordinateReferenceSystem();
    	return CRS.decode("EPSG:"+m_srid);
    }
    
    //获取epsg编码
    public String getEpsgCode() {
		return "EPSG:"+m_srid;
	}

    /**
     * @return
     
    public MBR getBoundBox() {
        return null;
    }
    */
    
    /*
    public void setMBR(double minx,double miny,double maxx,double maxy) {
		
	}
	*/
    
    public BoundingBox getMBR() {
    	BoundingBox mbr=m_stObjects.get(0).getMBR();
    	for(int i=1,len=m_stObjects.size();i<len;++i)
    	{
    		mbr.include(m_stObjects.get(i).getMBR());
    	}
    	return mbr;
	}
    

    /**
     * @return
     */
    public int getStSetLength() {
        return m_stObjects.size();
    }
    
    
    public static void main(String[] args) throws Exception {
    	System.out.println("StObjectSet");
/*
  	    SimpleFeatureTypeBuilder m_typeBuilder = new SimpleFeatureTypeBuilder();
	  
  	  //set global state
  	    m_typeBuilder.setName( "t1" );
        m_typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
  	  
       // m_typeBuilder.add("geom",Point.class);
        
  	    m_typeBuilder.add("CATEGORY", String.class);  
  	    m_typeBuilder.add("CALL GROUPS", String.class);  
  	    m_typeBuilder.add("final_case_type", String.class); 
  	    m_typeBuilder.add("CASE DESC", String.class); 
  	    m_typeBuilder.add("occ_date", String.class); 
  	    m_typeBuilder.add("census_tract", Integer.class); 
  	    
  	    SimpleFeatureType type=m_typeBuilder.buildFeatureType();
 */
    	/*
    	final SimpleFeatureType type = DataUtilities.createType("shapefile","CATEGORY:String,CALL GROUPS:String,final_case_type:String,"
    			+ "CASE DESC:String,occ_date:String,census_tract:Integer");
  	    */
    	//时空对象集合
  	    SpatioTemporalFeatureSet st_objects= new SpatioTemporalFeatureSet();
  	    st_objects.setFeatureType("CATEGORY:String,CALL GROUPS:String,final_case_type:String,"
    			+ "CASE DESC:String,census_tract:Integer",GeomTypeEnum.Point,"EPSG:3857");
  	    SimpleFeatureType objsType=st_objects.getFeatureType();
		//读取文件
        try {
        
        StringBuffer sb= new StringBuffer("");
       
        //读取
        FileReader reader = new FileReader("C:\\Users\\dell\\Desktop\\NIJ2016_JAN01_JUL31.csv");
        BufferedReader br = new BufferedReader(reader);
           
        String str = null;
        br.readLine(); //去除文件头
        while((str = br.readLine()) != null) {
        	
        	String[] strs = str.split(",");
        	
        	String wktStr ="POINT ("+strs[5]+" "+strs[6]+")";
        	
        	System.out.println(st_objects.getStSetLength()+1+"---"+strs[4]+" "+str);
        	
      	    SpatioTemporalFeature stFtest=new SpatioTemporalFeature();
      	    stFtest.setFeatureType(objsType);//m_typeBuilder.buildFeatureType()
      	    stFtest.setAttribute("CATEGORY", strs[0].trim());
      	    stFtest.setAttribute("CALL GROUPS", strs[1].trim());
      	    stFtest.setAttribute("final_case_type", strs[2].trim());
      	    stFtest.setAttribute("CASE DESC", strs[3].trim());
      	    stFtest.setAttribute("census_tract", strs.length<8?0:Integer.parseInt(strs[7]));
      	    
      	    stFtest.setTimePeriod(strs[4],"yyyy/mm/dd");

      	    stFtest.setGeometry(wktStr);
  
      	    st_objects.addStObject(stFtest);
      	    
        }
       
        br.close();
        reader.close();               
        }
        catch(FileNotFoundException e) {
        	e.printStackTrace();
        }
        catch(IOException e) {
        	e.printStackTrace();
       }
      
        st_objects.changeCRS("EPSG:4326");
        System.out.println("----测试结束----");
        System.out.println("2222test1 over---"+st_objects.getCRS()+
        		"---"+st_objects.getFeatureType()+
        		"\n---"+st_objects.getGeometryType()
        		+"\n---"+st_objects.isAtrributeExist("census_tract")
        		+"\n      period"+TimePeriod.stampToDate(st_objects.getTimePeriod().getBegin(), "yyyy-mm-dd") 
        		  +"   "+TimePeriod.stampToDate(st_objects.getTimePeriod().getEnd(), "yyyy-mm-dd")
        		  +"     "+st_objects.getTimePeriod().getBegin()+"  "+st_objects.getTimePeriod().getEnd());
 	 
      /*
      try{    
    	  
    	  List<SimpleFeature> featureList=new ArrayList<SimpleFeature>();
    	  List<SpatioTemporalFeature> templist=st_objects.getStFeatureList();
    	  for (SpatioTemporalFeature st_object : templist) {
			featureList.add(st_object.getFeature());
		}

          SimpleFeatureCollection collection = new ListFeatureCollection(type, featureList);  
   
    
          File newFile = new File("D:/geotoolsTest/testPoi.shp");  
          ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();  
          Map<String, Serializable> params = new HashMap<String, Serializable>();  
          params.put("url", newFile.toURI().toURL());  
          params.put("create spatial index", Boolean.TRUE);  
          ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);  
          newDataStore.createSchema(type);  //m_typeBuilder.buildFeatureType()
          newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);  
    
          Transaction transaction = new DefaultTransaction("create");  
          String typeName = newDataStore.getTypeNames()[0];  
          SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);  
    
          if (featureSource instanceof SimpleFeatureStore) {  
              SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;  
              featureStore.setTransaction(transaction);  
              try {  
                  featureStore.addFeatures(collection);  
                  transaction.commit();  
              } catch (Exception problem) {  
                  problem.printStackTrace();  
              transaction.rollback();  
              } finally {  
                  transaction.close();  
              }  
          } else {  
              System.out.println(typeName + " does not support read/write access");  
          }  
      } catch (Exception e) {  
          e.printStackTrace();  
      }  
      
      */
    }

    	 
}