/**
 * 
 */
package com.vcb.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 持久化对豄1�7
 * 
 * @author liudong
 */
@SuppressWarnings("unchecked")
public abstract class Pojo implements Serializable {

	public final static transient byte STATUS_PENDING = 0x00;
	public final static transient byte STATUS_NORMAL = 0x01;
	protected String _id;
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String tableName() {
		String tn = getClass().getSimpleName();
		if (tn.endsWith("Bean"))
			tn = tn.substring(0, tn.length() - 4);
		return tn;
	}

	protected String cacheRegion() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 列出要插入到数据库的域集合，子类可以覆盖此方泄1�7
	 * 
	 * @return
	 */
	public Map<String, Object> listInsertableFields() {
		try {
			Map<String, Object> props = BeanUtils.describe(this);
			props.remove("id");
			props.remove("class");
			for (String s : props.keySet()) {
				if (s.endsWith("_"))
					props.remove(s);
			}
			return props;
		} catch (Exception e) {
			throw new RuntimeException("Exception when Fetching fields of " + this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		// 不同的子类尽管ID是相同也是不相等的1�7
		if (!getClass().equals(obj.getClass()))
			return false;
		Pojo wb = (Pojo) obj;
		return wb.getId() == getId();
	}

	public String toJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// User user = mapper.readValue( File(), User.class);
		String str = mapper.writeValueAsString(this);
		return str;
	}

	public static Pojo jsonToObj(String json,Class cla) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Pojo p=(Pojo) mapper.readValue(json, cla);
		return p;
	}
}
