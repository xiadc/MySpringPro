/**
 * @author :dell
 * @date 2017年4月19日 上午9:53:38
 * @CopyRight:中国地质大学(武汉) 信息工程学院
 * Description:
 */
package com.lab.stcube.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HttpServletBean;

import com.lab.stcube.model.GeomTypeEnum;
import com.lab.stcube.model.SpatioTemporalFeature;
import com.lab.stcube.model.SpatioTemporalFeatureSet;
import com.lab.stcube.service.TestService;
import com.lab.stcube.visualization.imp.VisualizationImp;

/**
 * @author dell
 * 
 *
 */
@Controller  
public class TestController {  
	
	@Autowired
	private TestService testService;	
	
    @RequestMapping("/data/basedata")  
    public @ResponseBody List<Map<String,Object>> basedata() throws Exception{  
    	
    	//时空对象集合
    	
    		SpatioTemporalFeatureSet st_objects= testService.getBasedata();  
    		List<Map<String,Object>> listMap =  VisualizationImp.getBaseData(st_objects);
    		
    		listMap.add(VisualizationImp.getTimeCount(st_objects));
    		return listMap;
    		//return VisualizationImp.getBaseData(st_objects); 		  	               
    }  

    @RequestMapping("/data/basedata1")   
    public @ResponseBody List<Map<String, Object>> basedata1() throws Exception{  
    	
    	//时空对象集合
    	
    	List<Map<String, Object>> st_objects= testService.getBasedata1();  
    		return st_objects;
    		//return VisualizationImp.getBaseData(st_objects); 		  	               
    }  
   
  /*  @RequestMapping("/data/timecount")  
    public @ResponseBody Map<String, Object> timecount() throws Exception{  
    
    	SpatioTemporalFeatureSet st_objects= testService.getBasedata(); 
        //VisualizationImp.getTimeCount(st_objects); System.out.println("--timeCount--");
       // VisualizationImp.getBaseData(st_objects); System.out.println("--BaseData--");
    	//st_objects.changeCRS("EPSG:3857");
        
        return VisualizationImp.getTimeCount(st_objects);  
    	
    }  */
    
}

