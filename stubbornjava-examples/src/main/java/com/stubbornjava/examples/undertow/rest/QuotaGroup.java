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
public class QuotaGroup {
    private final String groupcontext;
    private final String groupid;

    public QuotaGroup(
            @JsonProperty("groupcontext") String groupcontext,
            @JsonProperty("groupid") String groupid) {
        super();
        this.groupcontext = groupcontext;
        this.groupid = groupid;
    }
    
    /**
	 * @return the groupcontext
	 */
	public String getGroupContext() {
		return groupcontext;
	}
	/**
	 * @return the groupid
	 */
	public String getGroupId() {
		return groupid;
	}
	
    public String toJSONString() {

    		StringBuilder s = new StringBuilder();
    		s.append("\"" + groupcontext + "\"" );
    		s.append(":");
    		s.append("\"" + groupid + "\"" );
       	    		
    		return s.toString();
    }


	private static final TypeReference<QuotaGroup> typeRef = new TypeReference<QuotaGroup>() {};
    public static TypeReference<QuotaGroup> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<QuotaGroup>> listTypeRef = new TypeReference<List<QuotaGroup>>() {};
    public static TypeReference<List<QuotaGroup>> listTypeRef() {
        return listTypeRef;
    }
}
