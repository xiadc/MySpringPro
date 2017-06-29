package com.lab.stcube.precompute.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.bson.Document;

import com.lab.stcube.dao.FeatureDao;
import com.lab.stcube.dao.imp.MongoFeatureDaoImp;
import com.lab.stcube.precompute.IPreCompute;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;


/**
 * 
 */
public class VectorPreCompute implements IPreCompute {

	MongoFeatureDaoImp mongoDao;
    /**
     * Default constructor
     */
    public VectorPreCompute() {
    	mongoDao = new MongoFeatureDaoImp();
    }

    /**
     * 
     */
    public void preCompute() {
        // TODO implement here
    	
    	MongoCollection<Document> preComputeCol = mongoDao.getCollection("crimePreCompute", "preComputeData2014");
    	
    	Document filter = new Document();
    	//filter.append("beginDate.day", 1);
    	FindIterable<Document> iter = mongoDao.findDocByFilter(filter)
    											.projection(new Document("beginDate", 1)
    													.append("_id", 1)
    													.append("wktData", 1));
    	//Set收集维度的具体值、去重，用null表示*（任意）
    	Set<Integer> yearSet = new HashSet<>();
    	yearSet.add(null);
    	Set<Integer> monthSet = new HashSet<>();
    	monthSet.add(null);
    	Set<Integer> daySet = new HashSet<>();
    	daySet.add(null);
    	//装载所有数据记录
    	List<Document> listDoc = new ArrayList<>();
    	
    	for (Document d : iter){
    		Document date = (Document) d.get("beginDate");
    		yearSet.add((Integer) date.get("year"));
    		monthSet.add((Integer) date.get("month"));
    		daySet.add((Integer) date.get("day"));
    		
    		Document doc = new Document();
    		doc.append("year", (Integer) date.get("year"));
    		doc.append("month", (Integer) date.get("month"));
    		doc.append("day", (Integer) date.get("day"));
    		doc.append("wktData", d.get("wktData"));
    		
    		listDoc.add(doc);
    	}
    	//时间维度的物化文件
    	File file = new File("DateDimention_all");
    	FileOutputStream out = null;
    	if (!file.exists()){
    		try {
				file.createNewFile();
				out = new FileOutputStream(file, true);
				//首行
				String firstColumn = "index#year#month#day#wktDataList \r\n";
		    	out.write(firstColumn.getBytes("utf-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		   	
    	int index = 1;
    	//*表示任意值
    	for (Integer year : yearSet){
    		for (Integer month : monthSet){
    			for (Integer day : daySet){
    				//数据立方体的单元cell，如：(1999 年, 北京, 火灾事件)※(事发数量)
    				Document cell = new Document();
    				List<Document> resultList = new ArrayList<>();
    				for (Document doc : listDoc){
    					Document result = new Document();    					
    					if (year == null){
    						result.append("year", "*");
    					}else {
    						int y = (int) doc.get("year");
    						if (y != year){
    							continue;
    						}else {
    							result.append("year", y);
    						}
    					}
    					
    					if (month == null){
    						result.append("month", "*");
    					}else {
    						int m = (int) doc.get("month");
    						if (m != month){
    							continue;
    						}else{
    							result.append("month", m);
    						}
    					}
    					
    					if (day == null){
    						result.append("day", "*");
    					}else {
    						int d = (int) doc.get("day");
    						if (d != day){
    							continue;
    						}else{
    							result.append("day", d);
    						}
    					}
    					
    					result.append("wktData", doc.get("wktData"));
    					resultList.add(result);
    				}
    				if (resultList.size() > 0){
    					//物化结果存储在数据库中
//    					Document dateDimension = new Document();
//    					dateDimension.append("year", resultList.get(0).get("year"));
//    					dateDimension.append("month", resultList.get(0).get("month"));
//    					dateDimension.append("day", resultList.get(0).get("day"));
//    					cell.append("dateDimention", dateDimension);
//    					cell.append("crimeCount", resultList.size());
//    					List<String> wktDataList = new ArrayList<>();
//        				for (Document res : resultList){
//        					wktDataList.add((String) res.get("wktData"));
//        				}
//        				cell.append("wktDataList", wktDataList);
//        				preComputeCol.insertOne(cell);
    					StringBuffer wktDataList = new StringBuffer();
    					for (Document res : resultList){
        					wktDataList.append((String) res.get("wktData") + ",");
        				}
    					
    					//物化结果存储在文件中
    					String column = index + "#" + resultList.get(0).get("year")
    							+ "#" + resultList.get(0).get("month") + "#" 
    							+ resultList.get(0).get("day") + "#" + wktDataList + "\r\n";
    					try {
							out.write(column.getBytes("utf-8"));
							index++;
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    				
    			}
    		}
    	}
    	try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//System.out.println(listDoc.size());
    	
    }
    
    public void preComputeAttribute(String dbName, String colName, String DimentionName, String [] attributes, String measure){
    	
    	MongoCollection<Document> col = mongoDao.getCollection(dbName, colName);
    	Document filter = new Document();
    	FindIterable<Document> iter = col.find(filter)
				.projection(new Document(DimentionName, 1)
						.append(measure, 1));
    	
    	List<Set<Object>> listSet = new ArrayList<Set<Object>>();
    	int num = attributes.length;
    	while (num > 0){
    		Set<Object> set = new HashSet<>();
    		set.add(null);
    		listSet.add(set);
    		num--;
    	}
    	List<Document> listDoc = new ArrayList<>();
    	
    	for (Document d : iter){
    		Document dimData = (Document) d.get(DimentionName);
    		
    		for (int i = 0; i < attributes.length; i++){
    			listSet.get(i).add(dimData.get(attributes[i]));
    		}    		
    		
    		Document doc = new Document();
    		
    		for (int i = 0; i < attributes.length; i++){
    			doc.append(attributes[i], dimData.get(attributes[i]));
    		}
    		doc.append(measure, d.get(measure));
    		
    		listDoc.add(doc);
    	}
    	
    	//物化文件
    	File file = new File(attributes[0]);
    	FileOutputStream out = null;
    	if (!file.exists()){
    		try {
				file.createNewFile();
				out = new FileOutputStream(file, true);			
				String attr = "";
				for (int i = 0; i < attributes.length; i++){
	    			attr += attributes[i] + "#";
	    		}
				//首行
				String firstColumn = "index#" + attr + "#" + measure + "\r\n";
		    	out.write(firstColumn.getBytes("utf-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	int index = 1;
    	//*表示任意值
    	
    	for (Object attr : listSet.get(0)){
    		Document cell = new Document();
			List<Document> resultList = new ArrayList<>();
			for (Document doc : listDoc){
				
				Document result = new Document();    					
				if (attr == null){
					result.append(attributes[0], "*");
				}else {
					Object y = doc.get(attributes[0]);
					if (!y.equals(attr) ){
						continue;
					}else {
						result.append(attributes[0], y);
					}
				}
				
				result.append("wktData", doc.get("wktData"));
				resultList.add(result);
			}
			if (resultList.size() > 0){
				StringBuffer wktDataList = new StringBuffer();
				for (Document res : resultList){
					wktDataList.append((String) res.get("wktData") + ",");
				}			
				//物化结果存储在文件中
				String column = index + "#" + resultList.get(0).get(attributes[0]) + "#" + wktDataList + "\r\n";
				try {
					out.write(column.getBytes("utf-8"));
					index++;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    	}
    	
    }
    
    public static void main(String[] args) {
    	
    	VectorPreCompute pre = new VectorPreCompute();
    	//物化
    	long start = System.currentTimeMillis();
    	//pre.preCompute();
    	String [] attribute = new String[]{"final_case_type"};
    	pre.preComputeAttribute("crime", "AmericanCrimeData_date", "attributes", attribute, "wktData");
    	long end = System.currentTimeMillis();
    	System.out.println(end-start);
    	
//    	MongoFeatureDaoImp mongoDao = new MongoFeatureDaoImp();
//    	//原始数据上进行聚合查询
//    	MongoCollection<Document> col = mongoDao.getCollection("sowhat", "AmericanCrimeData");
//    	//MongoCollection<Document> col = mongoDao.getCollection("crime", "AmericanCrimeData_date");
//    	List<Document> ls = new ArrayList<>();
//    	ls.add(new Document("$match", new Document("beginDate.month", 1)));
//    	ls.add(new Document("$group", new Document("_id", "$beginDate.month")
//    									.append("crimeCount", new Document("$sum", 1))
//    									.append("wktDataList", new Document("$push", "$wktData"))
//    									));
//    	long start1 = System.currentTimeMillis();
//    	AggregateIterable<Document> iterable = col.aggregate(ls);
//    	long end1 = System.currentTimeMillis();
//    	ArrayList<Document> valueResult1 = new ArrayList<Document>();
//    	for (Document row : iterable){
//    		valueResult1.add(row);
//    	}
//    	//物化后的数据上进行查询
//    	MongoCollection<Document> preComputeCol = mongoDao.getCollection("crimePreCompute", "preComputeData2014");   	
//    	Document filter = new Document();
//    	filter.append("dateDimention.year", "*");
//    	filter.append("dateDimention.month", 1);
//    	filter.append("dateDimention.day", "*");
//    	long start2 = System.currentTimeMillis();
//    	FindIterable<Document> it = preComputeCol.find(filter);
//    	long end2 = System.currentTimeMillis();
//    	ArrayList<Document> valueResult2 = new ArrayList<Document>();
//    	for (Document row : it){
//    		valueResult2.add(row);
//    	}
//    	System.out.println(end1-start1);
//    	System.out.println(end2-start2);
    	
	}

}