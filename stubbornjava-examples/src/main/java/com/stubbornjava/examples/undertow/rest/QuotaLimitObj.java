/**
 * 
 */
package com.stubbornjava.examples.undertow.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * This is the quota limits object for one resource.
 * @author darroyo
 *
 */
public class QuotaLimitObj {
    private final String limit;
    private final String resource;

    public QuotaLimitObj(
            @JsonProperty("limit") String limit,
            @JsonProperty("resource") String resource) {
        super();
        this.limit = limit;
        this.resource = resource;
    }
    
    /**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	
    public String toJSONString() {

    		StringBuilder s = new StringBuilder();
    		s.append("\"" + resource + "\"" );
    		s.append(":");
    		s.append("\"" + limit + "\"" );
       	    		
    		return s.toString();
    }


	private static final TypeReference<QuotaLimitObj> typeRef = new TypeReference<QuotaLimitObj>() {};
    public static TypeReference<QuotaLimitObj> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<QuotaLimitObj>> listTypeRef = new TypeReference<List<QuotaLimitObj>>() {};
    public static TypeReference<List<QuotaLimitObj>> listTypeRef() {
        return listTypeRef;
    }
}
