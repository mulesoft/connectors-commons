package com.mule.connectors.commons.rest.builder.request;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

public enum Method {
    GET {
        @Override
        public Response execute(Invocation.Builder requestBuilder, Object entity, String contentType) {
            return requestBuilder.get();
        }
    },
    POST {
        @Override
        public Response execute(Invocation.Builder requestBuilder, Object entity, String contentType) {
            return requestBuilder.post(Entity.entity(entity, contentType));
        }
    },
    PUT {
        @Override
        public Response execute(Invocation.Builder requestBuilder, Object entity, String contentType) {
            return requestBuilder.put(Entity.entity(entity, contentType));
        }
    },
    DELETE {
        @Override
        public Response execute(Invocation.Builder requestBuilder, Object entity, String contentType) {
            return requestBuilder.delete();
        }
    };

    public abstract Response execute(Invocation.Builder requestBuilder, Object entity, String contentType);
}
