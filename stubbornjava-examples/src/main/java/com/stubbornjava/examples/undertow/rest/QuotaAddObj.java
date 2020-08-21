package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

// {{start:quotaTreeNodeDefinition}}
public class QuotaAddObj {
    private final String id;
    private final String parent;
    private final String tree;
    private final String hard;
    private final QuotaLimitObj[] quota;
    private final LocalDate dateCreated;

    public QuotaAddObj(
            @JsonProperty("id") String id,
            @JsonProperty("parent") String parent,
            @JsonProperty("tree") String tree,
            @JsonProperty("hard") String hard, 
            @JsonProperty("quota") QuotaLimitObj[] quota, 
            @JsonProperty("dateCreated") LocalDate dateCreated) {
        super();
        this.id = id;
        this.parent = parent;
        this.tree = tree;
        this.hard = hard;
        this.quota = quota;
        this.dateCreated = dateCreated;
    }

    /**
	 * @return the id
	 */
	public String getID() {
        return id;
    }

	/**
	 * @return the parent
	 */
    public String getParent() {
        return parent;
    }

	/**
	 * @return the tree
	 */
    public String getTree() {
        return tree;
    }

    /**
	 * @return the quota restriction hard=true/hard=false(soft)
	 */
	public String hard() {
        return hard;
    }
	/**
	 * @return the list of quotas
	 */
	public QuotaLimitObj[] getQuota() {
		return quota;
	}
	

    public LocalDate getDateCreated() {
        return dateCreated;
    }
    
    public String nodeToJSONString() {

    		StringBuilder s = new StringBuilder();
    		s.append("{"); // json string
    		s.append("\"" + id + "\"" + ":");
       	s.append("{"); // id
		s.append("\"parent\":" + "\"" + parent + "\"");
		s.append(",");
		s.append("\"hard\":" + "\"" + hard + "\"");
		s.append(",");
		s.append("\"quota\":");
       	s.append("{"); // quota
		for (int i= 0; i < quota.length; i++) {
			if (i > 0 ) {
				s.append(",");
			}
			s.append(quota[i].toJSONString());
		}
		s.append("}");  // quota end  		
    		s.append("}");  // id end
    		s.append("}");  // json string end
    		
    		return s.toString();
    }

    private static final TypeReference<QuotaAddObj> typeRef = new TypeReference<QuotaAddObj>() {};
    public static TypeReference<QuotaAddObj> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<QuotaAddObj>> listTypeRef = new TypeReference<List<QuotaAddObj>>() {};
    public static TypeReference<List<QuotaAddObj>> listTypeRef() {
        return listTypeRef;
    }
}
// {{end:quotaTreNodeeDefinition}}