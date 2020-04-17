package com.azimbabu.zuulservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResponseFilter extends ZuulFilter {
  public static final int FILTER_ORDER = 1;
  public static final boolean SHOULD_FILTER = true;

  private FilterUtils filterUtils;

  @Autowired
  public ResponseFilter(FilterUtils filterUtils) {
    this.filterUtils = filterUtils;
  }

  @Override
  public String filterType() {
    return FilterUtils.POST_FILTER_TYPE;
  }

  @Override
  public int filterOrder() {
    return FILTER_ORDER;
  }

  @Override
  public boolean shouldFilter() {
    return SHOULD_FILTER;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext context = RequestContext.getCurrentContext();

    log.info(
        "Adding the correlation id to the outbound headers. {}", filterUtils.getCorrelationId());
    context.getResponse().addHeader(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId());
    log.info("Completing outgoing request for {}.", context.getRequest().getRequestURI());

    return null;
  }
}
