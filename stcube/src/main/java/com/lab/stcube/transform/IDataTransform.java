package com.lab.stcube.transform;

import java.util.*;

import com.lab.stcube.model.SpatioTemporalFeatureSet;

/**
 * 
 */
public interface IDataTransform {

    /**
     * 
     */
    public SpatioTemporalFeatureSet transferDataToSTFeatureSet();

    /**
     * 
     */
    public void persistSTFeatureSet();

}