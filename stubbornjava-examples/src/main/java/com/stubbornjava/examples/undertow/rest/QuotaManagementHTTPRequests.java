package com.stubbornjava.examples.undertow.rest;

import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;

// {{start:requests}}
public class QuotaManagementHTTPRequests {

    public String id(HttpServerExchange exchange) {
        return Exchange.pathParams().pathParam(exchange, "id").orElse(null);
    }

    public QuotaAllocObj quotaAllocObj(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, QuotaAllocObj.typeRef());
    }

    public void exception(HttpServerExchange exchange) {
        boolean exception = Exchange.queryParams()
                                    .queryParamAsBoolean(exchange, "exception")
                                    .orElse(false);
        if (exception) {
            throw new RuntimeException("Some random exception. Could be anything!");
        }
    }
}
// {{end:requests}}
