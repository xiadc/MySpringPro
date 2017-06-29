package com.lab.stcube.algorithm.imp;





import java.util.ArrayList;
import java.util.List;

import com.lab.stcube.algorithm.IAlgorithm;


/**
 * 
 */
public class MSOLAPAlgorithm implements IAlgorithm {

    /**
     * Default constructor
     */
    public MSOLAPAlgorithm() {
    }

    /**
     * @param obj 
     * @param param 
     * @return
     */ 
   // public SpatioTemporalFeatureSet rollup(SpatioTemporalFeatureSet obj, Param param) {
        // TODO implement here
  //      return null;    
  //  }

    
    /** 
    * @author xiadc
    * createTime：2017年5月22日 下午3:23:47 
    * @param bytes 输入立方体三维数组
    * @return 在时间维度上卷后平均灰度值的二维数组
    * 在时间维度上卷求平均灰度值
    * 连续型栅格数据立方体的三个维度为：x，y，时间（年），度量值为灰度
    */ 
    public int[][] rollUpForTimeDimension(byte[][][] bytes){
    	int sum = 0;
    	//存储上卷结果，返回二维数组
    	int[][] avgRes = new int[bytes.length][bytes[0].length];
    	//第一维：x
    	for (int i = 0; i < bytes.length; i++) {	
    		//第二维：y
    		for (int j = 0; j < bytes[i].length; j++) {
				//第三维：时间维
    			sum = 0;
    			for (int j2 = 0; j2 < bytes[i][j].length; j2=j2+4) {
					//按时间维上卷
    				sum += fromBytesToInt(bytes[i][j],j2);
				}
    			avgRes[i][j] = sum/(bytes[i][j].length/4);
			}
		}
    	return avgRes;
    }
    
    /** 
    * @author xiadc
    * createTime：2017年5月22日 下午5:19:22 
    * @param bytes
    *  在空间维度上卷求什么
    */ 
    public void rollUpForSpaceDimension(byte[][][] bytes){
    	//旋转此立方体，使时间维成为第一维度，方便后续处理
    	byte[][][] pivotBytes = pivot(bytes);
    	
    	for (int i = 0; i < pivotBytes.length; i++) {
			//针对每一年处理,如何处理？求灰度均值？还是怎么处理？？
    		
    		for (int j = 0; j < pivotBytes[i].length; j++) {
    			for (int j2 = 0; j2 < pivotBytes[i][j].length; j2++) {
    					
				}
			}
		}
    }
    
    /** 
    * @author xiadc
    * createTime：2017年5月22日 下午3:24:07 
    * @param bytes 立方体的字节数组表示形式
    * 下钻求什么？
    */ 
    public void drillDown(byte[][][] bytes){ 
    	
    }
    
    
    
    /** 
    * @author xiadc
    * createTime：2017年5月22日 下午3:52:15 
    * @param bytes
    * @return 将x维与时间维互换后的三维字节数据
    * 旋转 ,x维与时间维互换
    */ 
    public byte[][][] pivot(byte[][][] bytes){
    	byte[][][] byteRes = new byte[bytes[0][0].length][bytes[0].length][bytes.length];
    	
    	for (int i = 0; i < byteRes.length; i++) {
			for (int j = 0; j < byteRes[0].length; j++) {
				for (int j2 = 0; j2 < byteRes[0][0].length; j2++) {
					byteRes[i][j][j2] = bytes[j2][j][i];
				}
			}
		}
    	return byteRes;
    }
    
    /** 
    * @author xiadc
    * createTime：2017年5月22日 下午3:20:03 
    * @param b
    * @param start
    * @return
    * 私有方法，将byte转换为int 
    */ 
    private int fromBytesToInt(byte[] b,int start){
    	int ch4 = b[start];
    	int ch3 = b[start+1];
    	int ch2 = b[start+2];
    	int ch1 = b[start+3];
    	
    	return ch4+(ch3<<8)+(ch2<<16)+(ch1<<24);
    }
    
    /**
     * @param obj 
     * @param param 
     * @return
     */
  //  public SpatioTemporalFeatureSet drilldown(SpatioTemporalFeatureSet obj, Param param) {
        // TODO implement here
 //       return null;
  //  }

}