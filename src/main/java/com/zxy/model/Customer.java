
package com.zxy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "customer", type="doc", shards = 2, replicas = 1, refreshInterval = "30s")
public class Customer {

	//Id注解加上后，在Elasticsearch里相应于该列就是主键了，在查询时就可以直接用主键查询
	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "index_ansj_pinyin_analyzer", searchAnalyzer = "query_ansj_pinyin_analyzer")
	private String userName;

	@Field(type = FieldType.Text, analyzer = "index_ansj_pinyin_analyzer", searchAnalyzer = "query_ansj_pinyin_analyzer")
	private String address;

	@Field
	private int age;

	public Customer() {
	}

	public Customer(String userName, String address, int age) {
		this.userName = userName;
		this.address = address;
		this.age = age;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id='" + id + '\'' +
				", userName='" + userName + '\'' +
				", address='" + address + '\'' +
				", age=" + age +
				'}';
	}
}
