package com.azimbabu.license.utils;

import org.springframework.util.Assert;

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

    private static UserContext createEmptyContext() {
        return new UserContext();
    }

    public static final void setUserContext(UserContext userContext) {
        Assert.notNull(userContext, "Only non-null UserContext instances are premitted");
        userContextThreadLocal.set(userContext);
    }
}
