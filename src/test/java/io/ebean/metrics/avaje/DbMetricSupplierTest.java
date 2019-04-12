package io.ebean.metrics.avaje;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import org.avaje.metric.MetricManager;
import org.avaje.metric.statistics.MetricStatistics;
import org.domain.Customer;
import org.domain.MyService;
import org.domain.Org;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DbMetricSupplierTest {

  private MyService myService = new MyService();

  @Test
  public void collectMetrics() {

    DatabaseConfig db = new DatabaseConfig();
    db.addClass(Customer.class);
    db.loadFromProperties();

    Database db1 = DatabaseFactory.create(db);


    DatabaseConfig xdb = new DatabaseConfig();
    xdb.setDefaultServer(false);
    xdb.setName("orgdb");
    xdb.addClass(Org.class);
    xdb.loadFromProperties();

    Database db2 = DatabaseFactory.create(xdb);


    MetricManager.addSupplier(new DbMetricSupplier(db1));
    MetricManager.addSupplier(new DbMetricSupplier(db2, "orgdb"));

    DB.find(Customer.class)
      .setLabel("allCustomer")
      .findList();

    db2.find(Org.class)
      .findList();

    myService.doit();

    List<MetricStatistics> metrics = MetricManager.collectNonEmptyMetrics();

    List<String> names = metrics.stream().map(MetricStatistics::getName).collect(Collectors.toList());

    assertThat(names).contains("db.txn.main", "db.query.Customer_allCustomer", "db.query.Customer_MyService.doit", "orgdb.txn.main", "orgdb.query.Org.findList");

  }
}
