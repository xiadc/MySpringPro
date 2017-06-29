package com.lab.stcube.dao.imp;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.geotools.feature.SchemaException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

import com.lab.stcube.dao.FeatureDao;
import com.lab.stcube.model.GeomTypeEnum;
import com.lab.stcube.model.SpatioTemporalFeature;
import com.lab.stcube.model.SpatioTemporalFeatureSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.lab.stcube.utils.DateUtil;
import com.mongodb.MongoClient;
import com.mongodb.QueryOperators;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.vividsolutions.jts.io.ParseException;

/**
 * 
 */
@Component
public class MongoFeatureDaoImp implements FeatureDao {

	private MongoClient mongoClient;
	private MongoCollection<Document> collection;

	public MongoFeatureDaoImp() {
		// TODO Auto-generated constructor stub
		Properties prop1 = new Properties();
		Properties prop2 = new Properties();
		InputStream in1 = this.getClass().getClassLoader()
				.getResourceAsStream("db.properties");
		InputStream in2 = this.getClass().getClassLoader()
				.getResourceAsStream("dataSourcePath.properties");
		try {
			prop1.load(in1);
			prop2.load(in2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String server1 = prop1.getProperty("mongodb.server1");
		String server2 = prop1.getProperty("mongodb.server2");
		String server3 = prop1.getProperty("mongodb.server3");
		int port = Integer.parseInt(prop1.getProperty("mongodb.port"));
		String databaseName = prop2.getProperty("mongoDatabase");
		String collectionName = prop2.getProperty("collectionName");

		mongoClient = new MongoClient(Arrays.asList(new ServerAddress(server1,
				port), new ServerAddress(server2, port), new ServerAddress(
				server3, port)));
		mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
		collection = mongoClient.getDatabase(databaseName).getCollection(
				collectionName);
	}

	@Override
	public boolean deleteSTFeature(SpatioTemporalFeature stFeature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSTFeatureSet(SpatioTemporalFeatureSet stFeatureSet) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpatioTemporalFeatureSet querySTObjectByProperty(String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpatioTemporalFeatureSet querySTObjectByProperties(
			String[] properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addSTFeature(SpatioTemporalFeature stFeature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addSTFeatureSet(SpatioTemporalFeatureSet stFeatureSet) {
		// TODO Auto-generated method stub
		List<SpatioTemporalFeature> featureList = stFeatureSet
				.getStFeatureList();
		for (SpatioTemporalFeature feature : featureList) {
			String geoType = feature.getGeometryTypeInWkt();
			String epsgCode = feature.getEpsgCode();
			String wktData = feature.getGeometryInWkt();
			Long beginTime = feature.getTimePeriod().getBegin();
			Long endTime = feature.getTimePeriod().getEnd();
			List<String> attributes = feature.getFieldNames();
			Document d = new Document();
			d.append("geoType", geoType);
			d.append("epsgCode", epsgCode);
			d.append("wktData", wktData);
			d.append("beginTime", beginTime);
			d.append("endTime", endTime);
			d.append("beginDate", DateUtil.getDateDocutment(beginTime));
			d.append("endDate", DateUtil.getDateDocutment(endTime));
			Document sd = new Document();
			for (String att : attributes) {
				sd.append(att, feature.getAttribute(att));
			}
			d.append("attributes", sd);
			collection.insertOne(d);
		}
		return true;
	}

	@Override
	public SpatioTemporalFeatureSet queryAllSpatioTemporalFeature() {
		// TODO Auto-generated method stub	
		FindIterable<Document> iter = collection.find();	
		return documentsToFeatureSet(iter);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Map<String, Object>> countTile2d(double xMin, double xMax, double yMin, double yMax, int gridCount) {
		String map = "function Map() {" +
				"var x_min = " + xMin + ";" +
				"var y_min = " + yMin + ";" +
				"var x_max = " + xMax + ";" +
				"var y_max = " + yMax + ";" +
				"var grid_count = " + gridCount + ";" +
				"var grid_width = (x_max - x_min) / grid_count;" +
				"var grid_hight = (y_max - y_min) / grid_count;" +
				"if (this.loc.coordinates[0] > x_min && this.loc.coordinates[0] <= x_max" +
				"	&& this.loc.coordinates[1] > y_min && this.loc.coordinates[1] <= y_max) {" +
				"	var grid_x = parseInt((this.loc.coordinates[0] - x_min) / grid_width);" +
				"	var grid_y = parseInt((this.loc.coordinates[1] - y_min) / grid_hight);" +
				"	emit(" +
				"	[grid_x, grid_y]," +
				"	1" +
				"	)" +
				"}" +
			"}";
		String reduce = "function Reduce(key, values) {" +
				"return values.length;" +
			"}";
//		String finalize = "function Finalize(key, reduced) {return reduced;}";	// 可以不写吗？

		Properties prop1 = new Properties();
		Properties prop2 = new Properties();
		InputStream in1 = this.getClass().getClassLoader()
				.getResourceAsStream("db.properties");
		InputStream in2 = this.getClass().getClassLoader()
				.getResourceAsStream("dataSourcePath.properties");
		try {
			prop1.load(in1);
			prop2.load(in2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String databaseName = prop2.getProperty("mongoDatabase");
		String collectionName = prop2.getProperty("collectionName");
		DBCollection dbCollection = mongoClient.getDB(databaseName).getCollection(collectionName);
		BasicDBObject queryObject = new BasicDBObject().append( "loc", new BasicDBObject("$geoWithin"
				, new BasicDBObject(QueryOperators.BOX, new double[][] {{76.26000, 61.3000},{77.37000, 65.4000}})));
		MapReduceCommand cmd = new MapReduceCommand(dbCollection , map, reduce,  
	         null, MapReduceCommand.OutputType.INLINE, queryObject);
//		cmd.setFinalize(finalize);
		MapReduceOutput out = dbCollection.mapReduce(cmd);
		Iterable<DBObject> mrResult = out.results();
		List<Map<String, Object>> dict = dbObjectToJSON(mrResult);
		return dict;
	}

	/**
	 * @param iterDBObject 包含查询结果的迭代器。
	 * 迭代器的内容须包含"_id"和"value"，_id须为长度为2的double数组，value为double，标识count值。
	 * @return 返回瓦片每个像素点的坐标及count，count为0的像素点不返回。
	 */
	private List<Map<String, Object>> dbObjectToJSON(
			Iterable<DBObject> iterDBObject) {
		List<Map<String, Object>> dict = new ArrayList<Map<String, Object>>();
		iterDBObject.forEach(new Consumer<DBObject>(){
			@Override
			public void accept(DBObject point) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("x", ((ArrayList<Integer>)point.get("_id")).get(0));
				item.put("y", ((ArrayList<Integer>)point.get("_id")).get(1));
				item.put("count", ((Double)point.get("value")));
				dict.add(item);
				System.out.println(point.get("_id"));
				System.out.println(point.get("value"));
			}
		});
		System.out.println(dict.size());
		return dict;
	}
	

	@Override
	public void updateSTFeature(Bson filter, Bson update) {
		// TODO Auto-generated method stub
		Document op = new Document();
		op.append("$set", update);
		UpdateResult rs = collection.updateMany(filter, op);
		System.out.print(rs.getModifiedCount());
	}
	
	/*
	 * 将从mongodb中查询出来的数据转换为featureSet
	 */
	SpatioTemporalFeatureSet documentsToFeatureSet(FindIterable<Document> iter){
		SpatioTemporalFeatureSet st_objects = new SpatioTemporalFeatureSet();
		int flag = 0;
		try {
			for (Document document : iter) {
				Document attrs = (Document) document.get("attributes");
				if (flag == 0) {
					String typeSpec = "";
					for (String attr : attrs.keySet()) {
						typeSpec += attr + ":String,";
					}
					typeSpec = typeSpec.substring(0, typeSpec.length() - 1);
					GeomTypeEnum geomType = null;
					switch ((String) document.get("geoType")) {
					case "POINT":
						geomType = GeomTypeEnum.Point;
						// case "lingString":
						// geomType = GeomTypeEnum.LingString;
						// case "polygon":
						// geomType = GeomTypeEnum.Polygon;
						// case "lineaRing":
						// geomType = GeomTypeEnum.LineaRing;
					}
					String CRS = (String) document.get("epsgCode");

					st_objects.setFeatureType(typeSpec, geomType, CRS);

					flag = 1;
				}
				String wktStr = (String) document.get("wktData");
				long beginTime = (long) document.get("beginTime");
				long endTime = (long) document.get("endTime");
				SpatioTemporalFeature stFtest = new SpatioTemporalFeature();
				stFtest.setFeatureType(st_objects.getFeatureType());
				for (String attr : attrs.keySet()) {
					stFtest.setAttribute(attr, attrs.get(attr));
				}
				stFtest.setTimePeriod(beginTime, endTime);	
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MismatchedDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st_objects;
	}
	
	public FindIterable<Document> findDocByFilter(Bson filter){
		return collection.find(filter);	
	}
	
	public MongoCollection<Document> getCollection(String DBName, String collectionName){
		return mongoClient.getDatabase(DBName).getCollection(
				collectionName);
	}

	public static void main(String[] args) {
		MongoFeatureDaoImp dao = new MongoFeatureDaoImp();
//		dao.queryAllSpatioTemporalFeature();
		//dao.countTile2d(7626000, 7737000, 613000, 714000, 10);
		//dao.queryAllSpatioTemporalFeature();
		Document filter = new Document();
		Document update = new Document();
		update.append("epsgCode", "epsg:2269");
		dao.updateSTFeature(filter, update);
	}


}