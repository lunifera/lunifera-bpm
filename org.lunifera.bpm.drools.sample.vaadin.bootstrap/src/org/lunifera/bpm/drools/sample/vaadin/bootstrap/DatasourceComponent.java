package org.lunifera.bpm.drools.sample.vaadin.bootstrap;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jndi.JNDIConstants;

@Component(immediate = true)
public class DatasourceComponent {

	private static final String DERBY_JDBC_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String DBNAME_CARSTORE_DROOLS = "carstoreDBDrools";
	private static final String DROOLS_XA_DS = "DroolsXADs";
	private static final String DROOLS_DS_JTA = "DroolsDsJTA";
	private static final String DROOLS_DS = "DroolsDs";

	private DataSourceFactory dsf;
	private DataSource ds;
	private DataSource jtaDs;
	private XADataSource xaDs;
	private ServiceRegistration<DataSource> dsRegistry;
	private ServiceRegistration<DataSource> dsJtaRegistry;
	private ServiceRegistration<XADataSource> xaDsRegistry;

	@Activate
	protected void activate(ComponentContext context) {
		try {
			Properties dsProps = new Properties();
			dsProps.put(DataSourceFactory.JDBC_DATABASE_NAME,
					DBNAME_CARSTORE_DROOLS);
			dsProps.put(DataSourceFactory.JDBC_URL,
					"jdbc:derby:carstoreDBDrools;create=true");
			dsProps.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS,
					DERBY_JDBC_EMBEDDED_DRIVER);
			dsProps.put(DataSourceFactory.JDBC_USER, "app");
			dsProps.put(DataSourceFactory.JDBC_PASSWORD, "app");
			ds = dsf.createDataSource(dsProps);

			dsProps = new Properties();
			dsProps.put(DataSourceFactory.JDBC_DATABASE_NAME,
					DBNAME_CARSTORE_DROOLS);
			dsProps.put(DataSourceFactory.JDBC_URL,
					"jdbc:derby:carstoreDBDrools;create=true");
			dsProps.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS,
					DERBY_JDBC_EMBEDDED_DRIVER);
			dsProps.put(DataSourceFactory.JDBC_USER, "app");
			dsProps.put(DataSourceFactory.JDBC_PASSWORD, "app");
			jtaDs = dsf.createDataSource(dsProps);

			xaDs = dsf.createXADataSource(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(JNDIConstants.JNDI_SERVICENAME, DROOLS_DS);
		dsRegistry = context.getBundleContext().registerService(
				DataSource.class, ds, props);

		props = new Hashtable<String, String>();
		props.put(JNDIConstants.JNDI_SERVICENAME, DROOLS_DS_JTA);
		dsJtaRegistry = context.getBundleContext().registerService(
				DataSource.class, jtaDs, props);

		props = new Hashtable<String, String>();
		props.put(JNDIConstants.JNDI_SERVICENAME, DROOLS_XA_DS);
		xaDsRegistry = context.getBundleContext().registerService(
				XADataSource.class, xaDs, props);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	protected void setDatasourceFactory(DataSourceFactory dsf) {
		this.dsf = dsf;
	}

	@Deactivate
	protected void deactivate() {
		if (dsRegistry != null) {
			dsRegistry.unregister();
			dsRegistry = null;
		}

		if (dsJtaRegistry != null) {
			dsJtaRegistry.unregister();
			dsJtaRegistry = null;
		}

		if (xaDsRegistry != null) {
			xaDsRegistry.unregister();
			xaDsRegistry = null;
		}
	}

}
