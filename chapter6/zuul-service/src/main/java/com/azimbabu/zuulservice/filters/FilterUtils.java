package com.azimbabu.zuulservice.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {
  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String AUTH_TOKEN = "tmx-auth-token";
  public static final String USER_ID = "tmx-user-id";
  public static final String ORG_ID = "tmx-org-id";
  public static final String PRE_FILTER_TYPE = "pre";
  public static final String ROUTE_FILTER_TYPE = "route";
  public static final String POST_FILTER_TYPE = "post";
  public static final String SERVICE_ID = "serviceId";

  public String getCorrelationId() {
    RequestContext context = RequestContext.getCurrentContext();

    if (context.getRequest().getHeader(CORRELATION_ID) != null) {
      return context.getRequest().getHeader(CORRELATION_ID);
    } else {
      return context.getZuulRequestHeaders().get(CORRELATION_ID);
    }
  }

  public void setCorrelationId(String correlationId) {
    RequestContext context = RequestContext.getCurrentContext();
    context.addZuulRequestHeader(CORRELATION_ID, correlationId);
  }

  public String getOrganizationId() {
    RequestContext context = RequestContext.getCurrentContext();

    if (context.getRequest().getHeader(ORG_ID) != null) {
      return context.getRequest().getHeader(ORG_ID);
    } else {
      return context.getZuulRequestHeaders().get(CORRELATION_ID);
    }
  }

  public void setOrganizationId(String organizationId) {
    RequestContext context = RequestContext.getCurrentContext();
    context.addZuulRequestHeader(ORG_ID, organizationId);
  }

  public String getUserId() {
    RequestContext context = RequestContext.getCurrentContext();

    if (context.getRequest().getHeader(USER_ID) != null) {
      return context.getRequest().getHeader(USER_ID);
    } else {
      return context.getZuulRequestHeaders().get(USER_ID);
    }
  }

  public void setUserId(String userId) {
    RequestContext context = RequestContext.getCurrentContext();
    context.addZuulRequestHeader(USER_ID, userId);
  }

  public String getAuthToken() {
    RequestContext context = RequestContext.getCurrentContext();

    if (context.getRequest().getHeader(AUTH_TOKEN) != null) {
      return context.getRequest().getHeader(AUTH_TOKEN);
    } else {
      return context.getZuulRequestHeaders().get(AUTH_TOKEN);
    }
  }

  public String getServiceId() {
    RequestContext context = RequestContext.getCurrentContext();
    return context.getOrDefault(SERVICE_ID, "").toString();
  }
}
