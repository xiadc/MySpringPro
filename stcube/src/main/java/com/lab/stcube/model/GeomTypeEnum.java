/**
 * @author dell
 * @date 2017年4月13日 下午3:02:12
 * @CopyRight:中国地质大学(武汉) 信息工程学院
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
