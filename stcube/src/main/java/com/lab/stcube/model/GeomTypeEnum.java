/**
 * @author dell
 * @date 2017��4��13�� ����3:02:12
 * @CopyRight:�й����ʴ�ѧ(�人) ��Ϣ����ѧԺ
 * Description:
 */
package com.lab.stcube.model;

/**
 * @author dell
 *
 */
public enum GeomTypeEnum {
	Point, LingString, Polygon, LineaRing;

	public static GeomTypeEnum getEnumFromString(String string) {
		if (string != null) {
			try {
				return Enum.valueOf(GeomTypeEnum.class, string.trim());
			} catch (IllegalArgumentException ex) {
			}
		}
		return null;
	}
	
	public static String getWktType(GeomTypeEnum type) {
		return type.toString().toUpperCase();
	}
}
