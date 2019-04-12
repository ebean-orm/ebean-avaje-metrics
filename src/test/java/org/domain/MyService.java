package org.domain;

import org.domain.query.QCustomer;

import java.util.List;

public class MyService {

  public List<Customer> doit() {

    return new QCustomer()
      .name.startsWith("foo")
      .findList();
  }
}
