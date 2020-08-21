package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.stubbornjava.common.exceptions.Exceptions;

// {{start:dao}}
/*
 * In memory Dao. Less than ideal but just for an example.
 */
public class QuotaAllocationCacheDao {
    private final ConcurrentMap<String, QuotaAllocObj> quotaAllocationMap;

    public QuotaAllocationCacheDao() {
        this.quotaAllocationMap = new ConcurrentHashMap<>();
    }

    public QuotaAllocObj create(String id, QuotaGroup[] groups, int[] demand, int priority, boolean preemptable, String[] preemptedIds) {
    		QuotaAllocObj quotaAllocObj = new QuotaAllocObj(id, groups, demand, priority, preemptable, preemptedIds, LocalDate.now());

        // If we get a non null value that means the quota already exists in the Map.
        if (null != quotaAllocationMap.putIfAbsent(quotaAllocObj.getID(), quotaAllocObj)) {
           quotaAllocationMap.replace(quotaAllocObj.getID(), quotaAllocObj);
        }
        return quotaAllocObj;
    }

    public QuotaAllocObj get(String email) {
        return quotaAllocationMap.get(email);
    }

    // Alternate implementation to throw exceptions instead of return nulls for not found.
    public QuotaAllocObj getThrowNotFound(String email) {
        QuotaAllocObj quotaAllocObj = quotaAllocationMap.get(email);
        if (null == quotaAllocObj) {
            throw Exceptions.notFound(String.format("QuotaAllocObj %s not found", email));
        }
        return quotaAllocObj;
    }

    public QuotaAllocObj update(QuotaAllocObj quotaAllocObj) {
        // This means no quota existed so update failed. return null
        if (null == quotaAllocationMap.replace(quotaAllocObj.getID(), quotaAllocObj)) {
            return null;
        }
        // Update succeeded return the quota
        return quotaAllocObj;
    }

    public boolean delete(String email) {
        return null != quotaAllocationMap.remove(email);
    }

    public List<QuotaAllocObj> listQuotas() {
        return quotaAllocationMap.values()
                      .stream()
                      .sorted(Comparator.comparing((QuotaAllocObj u) -> u.getID()))
                      .collect(Collectors.toList());
    }
}
// {{end:dao}}
