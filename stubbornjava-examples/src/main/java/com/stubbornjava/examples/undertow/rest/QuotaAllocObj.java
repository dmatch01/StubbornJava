package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

// {{start:quotaDemand}}
public class QuotaAllocObj {
    private final String id;
    private final String group;
    private final int demand;
    private final int priority;
    private final boolean preemptable;
    private final LocalDate dateCreated;

    public QuotaAllocObj(
            @JsonProperty("id") String id,
            @JsonProperty("group") String group,
            @JsonProperty("demand") int demand, 
            @JsonProperty("priority") int priority, 
            @JsonProperty("preemptable") boolean preemptable, 
            @JsonProperty("dateCreated") LocalDate dateCreated) {
        super();
        this.id = id;
        this.group = group;
        this.demand = demand;
        this.priority = priority;
        this.preemptable = preemptable;
        this.dateCreated = dateCreated;
    }

    /**
	 * @return the id
	 */
	public String getID() {
        return id;
    }

	/**
	 * @return the demand
	 */
	public int getDemand() {
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
	 * @return the group
	 */
    public String getGroup() {
        return group;
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
}
// {{end:quotaDemand}}