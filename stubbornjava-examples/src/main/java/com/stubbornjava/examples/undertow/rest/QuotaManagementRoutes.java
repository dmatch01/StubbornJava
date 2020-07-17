package com.stubbornjava.examples.undertow.rest;

import java.util.List;

import com.ibm.quota.web.QuotaService;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
// {{start:routes}}
public class QuotaManagementRoutes {
    private static final QuotaAllocationCacheDao quotaAllocationCacheDao = new QuotaAllocationCacheDao();
    private static final QuotaManagementHTTPRequests quotaManagementHTTPRequests = new QuotaManagementHTTPRequests();
    private static final QuotaService quotaService = QuotaService.getInstance();
   
    public static void allocateQuota(HttpServerExchange exchange) {
        QuotaAllocObj quotaAllocReqInput = quotaManagementHTTPRequests.quotaAllocObj(exchange);
        String id = quotaAllocReqInput.getID();
        String group = quotaAllocReqInput.getGroup();
        int demand = quotaAllocReqInput.getDemand();
        int priority = quotaAllocReqInput.getPriority();
        boolean preemptable = quotaAllocReqInput.isPreemptable();
        
        boolean alloc = quotaService.allocConsumer(id, group, demand, priority, preemptable);
        if (alloc == false) {
            ApiHandlers.badRequest(exchange, String.format("QuotaAllocObj %s already exists.", quotaAllocReqInput.getID()));
            return;
        }
        exchange.setStatusCode(StatusCodes.OK);
        // Add to cache and get response body.
        QuotaAllocObj quotaAllocObjResponse = quotaAllocationCacheDao.create(id, group, demand, priority, preemptable);
        Exchange.body().sendJson(exchange, quotaAllocObjResponse);
    }
 
    public static void releaseQuota(HttpServerExchange exchange) {
    	String id  = quotaManagementHTTPRequests.id(exchange);
        
        boolean release = quotaService.releaseConsumer(id);

        // If you care about it you can handle it.
        if (false == release) {
            ApiHandlers.notFound(exchange, String.format("QuotaAllocObj {} not found.", id));
            return;
        }
        
        // Delete from cache
        quotaAllocationCacheDao.delete(id);
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
        exchange.endExchange();
    }

    public static void listQuotas(HttpServerExchange exchange) {
        List<QuotaAllocObj> quotaAllocObjs = quotaAllocationCacheDao.listQuotas();
        Exchange.body().sendJson(exchange, quotaAllocObjs);
    }
}
//{{end:routes}}

