package com.azimbabu.zuulservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {

  public static final int FILTER_ORDER = 1;
  public static final boolean SHOULD_FILTER = true;

  private final FilterUtils filterUtils;

  @Autowired
  public TrackingFilter(FilterUtils filterUtils) {
    this.filterUtils = filterUtils;
  }

  @Override
  public String filterType() {
    return FilterUtils.PRE_FILTER_TYPE;
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
    if (isCorrelationIdPresent()) {
      log.info(
          "{} found in tracking filter: {}.",
          FilterUtils.CORRELATION_ID,
          filterUtils.getCorrelationId());
    } else {
      filterUtils.setCorrelationId(generateCorrelationId());
      log.info(
          "{} generated in tracking filter: {}.",
          FilterUtils.CORRELATION_ID,
          filterUtils.getCorrelationId());
    }

    RequestContext context = RequestContext.getCurrentContext();
    log.debug("Processing incoming request for {}.", context.getRequest().getRequestURI());
    return null;
  }

  private String generateCorrelationId() {
    return UUID.randomUUID().toString();
  }

  private boolean isCorrelationIdPresent() {
    return filterUtils.getCorrelationId() != null;
  }
}
