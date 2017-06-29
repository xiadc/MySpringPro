package com.lab.stcube.dao;

import java.util.*;

import org.bson.conversions.Bson;

import com.lab.stcube.model.SpatioTemporalFeature;
import com.lab.stcube.model.SpatioTemporalFeatureSet;

/**
 * 
 */
public interface FeatureDao {

    /**
     * 
     */
    public boolean deleteSTFeature(SpatioTemporalFeature stFeature);

    /**
     * 
     */
    public boolean deleteSTFeatureSet(SpatioTemporalFeatureSet stFeatureSet);

    /**
     * @param property
     */
    public SpatioTemporalFeatureSet querySTObjectByProperty(String property);

    /**
     * @param properties
     */
    public SpatioTemporalFeatureSet querySTObjectByProperties(String[] properties);

    public SpatioTemporalFeatureSet queryAllSpatioTemporalFeature();
    
    /**
     * @param xMin x最小值
     * @param xMax x最大值
     * @param yMin y最小值
     * @param yMax y最大值
     * @param gridCount 需要被切割的行列数
     * @return 返回瓦片每个像素点的坐标及count，count为0的像素点不返回。
     */
    public List<Map<String, Object>> countTile2d(double xMin, double xMax, double yMin, double yMax, int gridCount);

 
    /**
     * @param stFeature
     */
    public boolean addSTFeature(SpatioTemporalFeature stFeature);

    /**
     * 
     */
    public boolean addSTFeatureSet(SpatioTemporalFeatureSet stFeatureSet);
    
    
    public void updateSTFeature(Bson filter, Bson update);

}