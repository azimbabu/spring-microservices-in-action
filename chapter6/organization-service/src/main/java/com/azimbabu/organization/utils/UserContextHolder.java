package com.azimbabu.organization.utils;

import static org.springframework.util.Assert.notNull;

public class UserContextHolder {
  private static final ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();

  public static final UserContext getUserContext() {
    UserContext userContext = userContextThreadLocal.get();
    if (userContext == null) {
      userContext = createEmptyContext();
      userContextThreadLocal.set(userContext);
    }
    return userContextThreadLocal.get();
  }

  public static final void setUserContext(UserContext userContext) {
    notNull(userContext, "Only non-null UserContext instances are premitted");
    userContextThreadLocal.set(userContext);
  }

  private static UserContext createEmptyContext() {
    return new UserContext();
  }
}
