package io.ebean.metrics.avaje;

import io.ebean.Database;
import io.ebean.meta.BasicMetricVisitor;
import io.ebean.meta.MetaOrmQueryMetric;
import io.ebean.meta.MetaQueryMetric;
import io.ebean.meta.MetaTimedMetric;
import org.avaje.metric.MetricSupplier;
import org.avaje.metric.statistics.MetricStatistics;
import org.avaje.metric.statistics.TimedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapts Ebean metrics to Avaje metrics.
 * <p>
 * This the Ebean database metrics to be included and reported with Avaje metrics.
 * </p>
 */
public class DbMetricSupplier implements MetricSupplier {

  private final Database database;

  private final String prefix;
  private final String queryPrefix;

  /**
   * Create with a Database using "db" as the prefix for metrics.
   */
  public DbMetricSupplier(Database database) {
    this.database = database;
    this.prefix = "db.";
    this.queryPrefix = "db.query.";
  }

  /**
   * Create with a Database and a prefix.
   */
  public DbMetricSupplier(Database database, String prefix) {
    this.database = database;
    this.prefix = prefix + ".";
    this.queryPrefix = prefix + ".query.";
  }

  @Override
  public List<MetricStatistics> collectMetrics() {

    List<MetricStatistics> list = new ArrayList<>();

    BasicMetricVisitor basic = database.getMetaInfoManager().visitBasic();
    for (MetaTimedMetric timedMetric : basic.getTimedMetrics()) {
      list.add(convert(timedMetric));
    }
    for (MetaOrmQueryMetric queryMetric : basic.getOrmQueryMetrics()) {
      list.add(convertQuery(queryMetric));
    }
    for (MetaQueryMetric queryMetric : basic.getDtoQueryMetrics()) {
      list.add(convertQuery(queryMetric));
    }

    return list;
  }

  private MetricStatistics convert(MetaTimedMetric metric) {
    return new TimedAdapter(prefix + metric.getName(), metric.getStartTime(), metric.getCount(), metric.getTotal(), metric.getMax());
  }

  private MetricStatistics convertQuery(MetaQueryMetric metric) {
    return new TimedAdapter(queryPrefix + metric.getName(), metric.getStartTime(), metric.getCount(), metric.getTotal(), metric.getMax());
  }
}
