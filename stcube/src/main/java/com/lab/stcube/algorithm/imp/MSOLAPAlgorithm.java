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
    * createTime��2017��5��22�� ����3:23:47 
    * @param bytes ������������ά����
    * @return ��ʱ��ά���Ͼ��ƽ���Ҷ�ֵ�Ķ�ά����
    * ��ʱ��ά���Ͼ���ƽ���Ҷ�ֵ
    * ������դ�����������������ά��Ϊ��x��y��ʱ�䣨�꣩������ֵΪ�Ҷ�
    */ 
    public int[][] rollUpForTimeDimension(byte[][][] bytes){
    	int sum = 0;
    	//�洢�Ͼ��������ض�ά����
    	int[][] avgRes = new int[bytes.length][bytes[0].length];
    	//��һά��x
    	for (int i = 0; i < bytes.length; i++) {	
    		//�ڶ�ά��y
    		for (int j = 0; j < bytes[i].length; j++) {
				//����ά��ʱ��ά
    			sum = 0;
    			for (int j2 = 0; j2 < bytes[i][j].length; j2=j2+4) {
					//��ʱ��ά�Ͼ�
    				sum += fromBytesToInt(bytes[i][j],j2);
				}
    			avgRes[i][j] = sum/(bytes[i][j].length/4);
			}
		}
    	return avgRes;
    }
    
    /** 
    * @author xiadc
    * createTime��2017��5��22�� ����5:19:22 
    * @param bytes
    *  �ڿռ�ά���Ͼ���ʲô
    */ 
    public void rollUpForSpaceDimension(byte[][][] bytes){
    	//��ת�������壬ʹʱ��ά��Ϊ��һά�ȣ������������
    	byte[][][] pivotBytes = pivot(bytes);
    	
    	for (int i = 0; i < pivotBytes.length; i++) {
			//���ÿһ�괦��,��δ�����ҶȾ�ֵ��������ô������
    		
    		for (int j = 0; j < pivotBytes[i].length; j++) {
    			for (int j2 = 0; j2 < pivotBytes[i][j].length; j2++) {
    					
				}
			}
		}
    }
    
    /** 
    * @author xiadc
    * createTime��2017��5��22�� ����3:24:07 
    * @param bytes ��������ֽ������ʾ��ʽ
    * ������ʲô��
    */ 
    public void drillDown(byte[][][] bytes){ 
    	
    }
    
    
    
    /** 
    * @author xiadc
    * createTime��2017��5��22�� ����3:52:15 
    * @param bytes
    * @return ��xά��ʱ��ά���������ά�ֽ�����
    * ��ת ,xά��ʱ��ά����
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
    * createTime��2017��5��22�� ����3:20:03 
    * @param b
    * @param start
    * @return
    * ˽�з�������byteת��Ϊint 
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