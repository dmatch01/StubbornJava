package com.stubbornjava.examples.undertow.rest;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.quota.web.MultiQuotaService;
import com.ibm.quota.core.AllocationResponse;
import com.stubbornjava.common.Env;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
// {{start:routes}}
public class QuotaManagementRoutes {
    private static final QuotaAllocationCacheDao quotaAllocationCacheDao = new QuotaAllocationCacheDao();
    private static final QuotaManagementHTTPRequests quotaManagementHTTPRequests = new QuotaManagementHTTPRequests();
    private static final MultiQuotaService quotaService = MultiQuotaService.getInstance();
    // {{start:logger}}
    private static final Logger log = LoggerFactory.getLogger(Env.class);

    public static void addQuota(HttpServerExchange exchange) {
    		QuotaAddObj quotaAddReqInput = quotaManagementHTTPRequests.quotaAddObj(exchange);
    		log.info("[addQuota] Adding node {} to tree: {}", quotaAddReqInput.nodeToJSONString(), quotaAddReqInput.getTree());
    		quotaService.createTreeAddNodes(quotaAddReqInput.getTree(), quotaAddReqInput.nodeToJSONString());
    	    exchange.setStatusCode(StatusCodes.OK);
    	    exchange.endExchange();
    }
    
    public static void allocateQuota(HttpServerExchange exchange) {
        QuotaAllocObj quotaAllocReqInput = quotaManagementHTTPRequests.quotaAllocObj(exchange);
        String id = quotaAllocReqInput.getID();
        QuotaGroup[] groups = quotaAllocReqInput.getGroups();
        Map<String, String> groupsMap = quotaAllocReqInput.getGroupsMap();
        int[] demand = quotaAllocReqInput.getDemand();
        Map<String, Integer[]> demandsMap = quotaAllocReqInput.getGroupDemandsMap();
        int priority = quotaAllocReqInput.getPriority();
        boolean preemptable = quotaAllocReqInput.isPreemptable();

        //Check for initialize tree
        List<JSONObject> quotaTrees = quotaService.getJson();
        if ((quotaTrees == null) || (quotaTrees.size() <= 0)) {
            log.info("[allocateQuota] Quota Manager not initialized.  Attempting to initialize Quota Manager.");
        		quotaService.createTreeFinish();
        }

        log.info("[allocateQuota] Requesting allocation for job: {} request: \n{}", id, quotaAllocReqInput.toString());
        int type = 0;
        AllocationResponse allocResp = quotaService.allocConsumer(id, groupsMap, demandsMap, priority, type, preemptable);
        if (allocResp.isAllocated() == false) {
            ApiHandlers.badRequest(exchange, String.format("QuotaAllocObj %s request failed.", quotaAllocReqInput.getID()));
            return;
        }
        exchange.setStatusCode(StatusCodes.OK);
        String[] preemptedIds = new String[0];
        if ((allocResp != null) && (allocResp.getPreemptedIds().length > 0)) {
        		preemptedIds = allocResp.getPreemptedIds();
        }
        // Add to cache and get response body.
        QuotaAllocObj quotaAllocObjResponse = quotaAllocationCacheDao.create(id, groups, demand, priority, preemptable, preemptedIds);
        Exchange.body().sendJson(exchange, quotaAllocObjResponse);
    }
 
    public static void releaseQuota(HttpServerExchange exchange) {
    		String id  = quotaManagementHTTPRequests.id(exchange);
        
        //Check for initialize tree
        List<JSONObject> quotaTrees = quotaService.getJson();
        if ((quotaTrees == null) || (quotaTrees.size() <= 0)) {
            log.info("[allocateQuota] Quota Manager not initialized.  Attempting to initialize Quota Manager.");
        		quotaService.createTreeFinish();
        }

        log.info("[releaseQuota] Releasing allocation for job: {}", id);
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

    /*
     * Respond the JSONObject of the current tree in quotaService.
     */
    public static void getJson(HttpServerExchange exchange) {
        // Add to cache and get response body.
        List<JSONObject> jsonTrees = quotaService.getJson();
        if ((jsonTrees == null) || (jsonTrees.size() <= 0)){
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonTree = (JSONObject) parser.parse("{\"empty_key\":\"to_be_deleted\"}");
				jsonTree.remove("empty_key", "to_be_deleted");
				log.warn("Quota manager is not initialized, producing empty json: {}.", jsonTree.toJSONString());
			} catch (ParseException e) {
		         ApiHandlers.badRequest(exchange, String.format("Get emptu Quota Manager JSON request failed: {}", e.toString()));    
				return;
			}
        }
        Exchange.headers().setHeader(exchange, "Access-Control-Allow-Origin", "*");
        Exchange.body().sendJson(exchange, jsonTrees);
    }

    public static void listQuotas(HttpServerExchange exchange) {
        List<QuotaAllocObj> quotaAllocObjs = quotaAllocationCacheDao.listQuotas();
        Exchange.body().sendJson(exchange, quotaAllocObjs);
    }
}
//{{end:routes}}

