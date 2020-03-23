package com.azimbabu.license.hystrix;

import com.azimbabu.license.utils.UserContext;
import com.azimbabu.license.utils.UserContextHolder;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {
  private final Callable<V> delegate;
  private UserContext originalUserContext;

  public DelegatingUserContextCallable(Callable<V> delegate, UserContext originalUserContext) {
    this.delegate = delegate;
    this.originalUserContext = originalUserContext;
  }

  @Override
  public V call() throws Exception {
    UserContextHolder.setUserContext(originalUserContext);

    try {
      return delegate.call();
    } finally {
      this.originalUserContext = null;
    }
  }
}
