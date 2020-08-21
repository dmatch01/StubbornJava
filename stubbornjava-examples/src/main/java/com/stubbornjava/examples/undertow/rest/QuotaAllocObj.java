package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

// {{start:quotaDemand}}
public class QuotaAllocObj {
    private final String id;
    private final QuotaGroup[] groups;
    private final int[] demand;
    private final int priority;
    private final boolean preemptable;
    private final String[] preemptedIds;
    private final LocalDate dateCreated;

    public QuotaAllocObj(
            @JsonProperty("id") String id,
            @JsonProperty("groups") QuotaGroup[] groups,
            @JsonProperty("demand") int[] demand, 
            @JsonProperty("priority") int priority, 
            @JsonProperty("preemptable") boolean preemptable, 
            @JsonProperty("preemptedIds") String[] preemptedIds, 
            @JsonProperty("dateCreated") LocalDate dateCreated) {
        super();
        this.id = id;
        this.groups = groups;
        this.demand = demand;
        this.priority = priority;
        this.preemptable = preemptable;
        this.preemptedIds = preemptedIds;
        this.dateCreated = dateCreated;
    }

    /**
	 * @return the id
	 */
	public String getID() {
        return id;
    }

	/**
	 * @return the cpu
	 */
	public int[] getDemand() {
		return demand;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @return the preemptable
	 */
	public boolean isPreemptable() {
		return preemptable;
	}

	/**
	 * @return the preemptedJobs
	 */
	public String[] getPreemptedJobs() {
		return preemptedIds;
	}

	/**
	 * @return the groups
	 */
    public QuotaGroup[] getGroups() {
        return groups;
    }
    
    
	/**
	 * @return the groups as a map
	 */
    public Map<String, String> getGroupsMap() {
    		Map<String, String> groupsMap = new HashMap<String, String>();
    		for (int i = 0; i < groups.length; i++) {
    			groupsMap.put(groups[i].getGroupContext(), groups[i].getGroupId());
    		}
        return groupsMap;
    }
    
    /**
     * @return Integer[] from int[]
     */
    public Integer[] convertDemand(int[] demand) {
    		Integer[] newArray = new Integer[demand.length];
    		int i = 0;
    		for (int value : demand) {
    			newArray[i++] = Integer.valueOf(value);
    		}
    		return newArray;
    }
    
	/**
	 * @return the groups as a map
	 */
    public Map<String,  Integer[]> getGroupDemandsMap() {
    		Map<String, Integer[]> groupDemands = new HashMap<String, Integer[]>();
    		for (int i = 0; i < groups.length; i++) {
    			groupDemands.put(groups[i].getGroupContext(), convertDemand(demand));
    		}
        return groupDemands;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    private static final TypeReference<QuotaAllocObj> typeRef = new TypeReference<QuotaAllocObj>() {};
    public static TypeReference<QuotaAllocObj> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<QuotaAllocObj>> listTypeRef = new TypeReference<List<QuotaAllocObj>>() {};
    public static TypeReference<List<QuotaAllocObj>> listTypeRef() {
        return listTypeRef;
    }
    
    public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("id: ");
		s.append(id);
		s.append("\n");
		s.append("groups:\n");

		for (int i= 0; i < groups.length; i++) {
			s.append("  ");
			s.append(groups[i].toJSONString());
			s.append("\n");
		}
		
		s.append("demands: [");
		for (int i= 0; i < demand.length; i++) {
			if (i > 0)
				s.append(" ");
			s.append(demand[i]);
		}
		s.append("]\n");
		s.append("priority: ");
		s.append(priority);
		s.append("\n");
		
		s.append("preemptable: ");
		s.append(preemptable);
		s.append("\n");
		
		return s.toString();
    }
}
// {{end:quotaDemand}}