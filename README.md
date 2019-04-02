# ebean-avajemetrics
An adapter such that the metrics from Ebean can be supplied and reported via Avaje metrics


## Example use

```java
Database db = DB.getDefault();
MetricManager.addSupplier(new DbMetricSupplier(db));
```


## Example - register multiple databases

```java

Database db1 = DB.getDefault();
Database db2 = DB.byName("central");

// metric names prefixed with "db"
MetricManager.addSupplier(new DbMetricSupplier(db1));

// metric names prefixed with "centraldb"
MetricManager.addSupplier(new DbMetricSupplier(db2, "centraldb"));

```
