dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
      dataSource {
        pooled = true
        dialect = 'org.hibernate.dialect.Oracle10gDialect'
        driverClassName = "oracle.jdbc.driver.OracleDriver"
        url = "jdbc:oracle:thin:@172.16.80.22:1521:prod18"
        username = "consultas_exc"
        password = "mercadolibre"
        properties {
          // Check connections
          validationQuery = "select 1 from dual"
          testOnBorrow = true
          testOnReturn = false
          testWhileIdle = false
          // Evict idle connections
          timeBetweenEvictionRunsMillis = 1000 * 60
          numTestsPerEvictionRun = 3
          minEvictableIdleTimeMillis = 1000 * 60 * 5
          // Connection pool
          maxActive = 5
          maxIdle = 5
          minIdle = 2
          initialSize = 2
          maxWait = 1000
        }
      }
        dataSource_nw {
          pooled = true
          logSql = true
          dbCreate = "create-drop"
          url = "jdbc:mysql://localhost:3306/nuevo_mundo"
          driverClassName = "com.mysql.jdbc.Driver"
          username = "root"
          password = ""
      }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
