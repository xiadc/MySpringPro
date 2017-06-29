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
     * @param xMin x��Сֵ
     * @param xMax x���ֵ
     * @param yMin y��Сֵ
     * @param yMax y���ֵ
     * @param gridCount ��Ҫ���и��������
     * @return ������Ƭÿ�����ص�����꼰count��countΪ0�����ص㲻���ء�
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