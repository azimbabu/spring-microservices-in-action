package com.azimbabu.organization.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class UserContextFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    UserContextHolder.getUserContext()
        .setCorrelationId(request.getHeader(UserContext.CORRELATION_ID));
    UserContextHolder.getUserContext().setUserId(request.getHeader(UserContext.USER_ID));
    UserContextHolder.getUserContext().setAuthToken(request.getHeader(UserContext.AUTH_TOKEN));
    UserContextHolder.getUserContext().setOrganizationId(request.getHeader(UserContext.ORG_ID));

    log.debug(
        "Organization Service Incoming Correlation Id: {}",
        UserContextHolder.getUserContext().getCorrelationId());

    filterChain.doFilter(request, servletResponse);
  }

  @Override
  public void destroy() {}
}
