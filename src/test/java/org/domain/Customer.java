package org.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

  @Id
  long id;

  String name;
}
