package com.mofluid.interfaces;

import com.mofluid.utility_new.Callback;

public interface PaymentMethod {
  void pay(double amount, Callback callback);
}
