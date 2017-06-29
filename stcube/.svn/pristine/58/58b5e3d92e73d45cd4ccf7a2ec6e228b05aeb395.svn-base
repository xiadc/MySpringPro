/**
 * @author :dell
 * @date 2017年4月17日 上午9:25:17
 * @CopyRight:中国地质大学(武汉) 信息工程学院
 * Description:
 */
package com.lab.stcube.visualization.imp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.Null;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

import com.lab.stcube.model.GeomTypeEnum;
import com.lab.stcube.model.MBR;
import com.lab.stcube.model.SpatioTemporalFeature;
import com.lab.stcube.model.SpatioTemporalFeatureSet;
import com.lab.stcube.model.TimePeriod;


/**
 * @author dell
 *
 */
public class VisualizationImp{
	
	
	public Object getAllData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Map<String, Object> getTimeCount(SpatioTemporalFeatureSet dataSet) {
		//String[] bgArr = dataSet.getTimePeriod().getBeginAsString().split("-");
		//String[] edArr = dataSet.getTimePeriod().getEndAsString().split("-");
		Calendar bg = Calendar.getInstance();
		Calendar ed = Calendar.getInstance();
		
		int addtype=Calendar.MONTH;
		String dateformat="yyyy/MM";
		if(dataSet.getTimePeriod().getIntervalInDays()<32)
		{
			addtype = Calendar.DATE;
			dateformat="yyyy/MM/dd";
		}
		
		bg.setTime(new Date(dataSet.getTimePeriod().getBegin()));
		ed.setTime(new Date(dataSet.getTimePeriod().getEnd()));
		
		List<Long> stamplist=new ArrayList<Long>();    //时间段范围
		stamplist.add(dataSet.getTimePeriod().getBegin());
		
		while(bg.getTime().before(ed.getTime()))
		{
			//bg.add(Calendar.MONTH,1);
			bg.add(addtype, 1);
			bg.getTimeInMillis();
			stamplist.add(bg.getTimeInMillis());
		}
		
		List<SpatioTemporalFeature> list = dataSet.getStFeatureList();
		
		List<String> dateStrs=new ArrayList<String>();  //日期字符串
		int[] counts=new int[stamplist.size()-1]; //计数字符串
		
		//计数
		for (SpatioTemporalFeature stObj : list) {
		//	if(stObj.getTimePeriod().getBegin()>=bgStamp&&stObj.getTimePeriod().getBegin()<edStamp)
			//	++num;
			long t=stObj.getTimePeriod().getBegin();
			for(int i=1;i<stamplist.size();++i)
			{
				if(t<stamplist.get(i)){
					counts[i-1]=++counts[i-1];
					break;
				} 
			}
		}	
		
		for(int j=0;j<stamplist.size()-1;++j)
			dateStrs.add(TimePeriod.stampToDate(stamplist.get(j), dateformat));

		Map<String, Object> map=new HashMap<String, Object>();
		//转化成Integer数组
		Integer[] countobjs=new Integer[counts.length];
		for (int i=0 ; i<counts.length;++i) {
			countobjs[i]=new Integer(counts[i]);
		}	
		map.put("title", dateStrs);
		map.put("data", counts);
		return map;
	}
	
	public static List<Map<String, Object>> getBaseData(SpatioTemporalFeatureSet dataSet) throws Exception {
		List<SpatioTemporalFeature> list=dataSet.getStFeatureList();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (int i = 0,len=list.size(); i < len; i++) {
			Map<String,Object> map = new HashMap<String,Object>();  
			long idStr=list.get(i).getStId();
			map.put("id",idStr);
			
			String timeStr = list.get(i).getTimePeriod().getBeginAsString("yyyyMMdd");  //时间
			map.put("time", Integer.parseInt(timeStr));
			
			String[] arr_coord=list.get(i).getGeometry().toString().split(" ");
		//	if(arr_coord[0]!="POINT")throw new Exception("测试样例为点");
			
			String x = arr_coord[1].substring(1); 
			map.put("lng",Float.parseFloat(x));
			
			String y =arr_coord[2].substring(0, arr_coord[2].length()-1);
			map.put("lat", Float.parseFloat(y));
			
			resultList.add(map);
		}
		return resultList;
	}
	
	/**
	 * 返回字段统计
	 */
	public Object getFieldCount(String fieldname) {
		return null;
	}
	
	/**
	 * 返回某条数据
	 */
	public static Map<String,Object> getDataById(SpatioTemporalFeatureSet dataSet,int id) {
		SpatioTemporalFeature stobj=dataSet.getStObjectById(id);
		Map<String, Object> map=new HashMap<String, Object>();
		return null;
	}
	
	/**
	 * 返回某字段字段数据集合
	 */
	public Object getFieldData(String fieldname){
		return null;
	}
	
	public static Map<String, Object> getGridCount(SpatioTemporalFeatureSet dataSet,int gridMinLenNum)
	{
		//获取数据范围
		BoundingBox mbr = dataSet.getMBR();
		
		
		//获取网格间隔
		double interval_distance=0,xLen=mbr.getMaxX()-mbr.getMaxX(),yLen=mbr.getMaxY()-mbr.getMinY();
		int gridXNum=0,gridYNum=0;
		//获取x,y方向上的网格数量
		if(xLen>yLen)
		{
			interval_distance=(yLen)/gridMinLenNum;
			gridYNum=gridMinLenNum;
			gridXNum=(int) Math.ceil(xLen/interval_distance);
			
		}else {
			interval_distance=(xLen)/gridMinLenNum;
			gridXNum=gridMinLenNum;
			gridYNum=(int) Math.ceil(yLen/interval_distance);
		}
		
		//List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		List<SpatioTemporalFeature> stlist=dataSet.getStFeatureList();
		int[] numOfGrid=new int[gridXNum*gridYNum];
		
		for (int i = 0,len=stlist.size(); i < len; i++) {
			BoundingBox stmbr = stlist.get(i).getMBR();
			double[] cenPnt={(stmbr.getMaxX()+stmbr.getMinX())/2-mbr.getMinX(),(stmbr.getMaxY()+stmbr.getMinY())/2-mbr.getMinY()};
			++numOfGrid[(int)( (Math.floor(cenPnt[0]/interval_distance)+Math.floor(cenPnt[1]/interval_distance)*gridXNum) )];
		}
		
		Map<String,Object> dataMap=new HashMap<String, Object>();
		
		dataMap.put("bound",new double[]{mbr.getMinX(),mbr.getMinY(),mbr.getMaxX(),mbr.getMaxY()});
		dataMap.put("gridNum", new int[]{gridXNum,gridYNum});
		dataMap.put("data", numOfGrid);
		
	    //统计并插入坐标以及数量
		return dataMap;
	}
	
    public static void main(String[] args) throws Exception {
    	System.out.println("StObjectSet");
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
      	    
      	    stFtest.setTimePeriod(strs[4],"yyyy/MM/dd");

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
       
        VisualizationImp.getTimeCount(st_objects); System.out.println("--timeCount--");
        VisualizationImp.getBaseData(st_objects); System.out.println("--BaseData--");
    }

	
}











