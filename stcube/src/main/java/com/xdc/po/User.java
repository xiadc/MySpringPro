package com.xdc.po;
/** 
 * @author xiadc 
 * createtime：2017年4月10日 下午4:03:20 
 * 类说明 
 */
public class User {

	private Integer id;
	private String name;
	private Integer age;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		
		return id +" " + name + " " + age;
	}
	
}
