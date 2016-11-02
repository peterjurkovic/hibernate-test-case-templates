/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs;

import java.util.List;

import org.apache.log4j.spi.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	static Logger LOG = org.slf4j.LoggerFactory.getLogger(ORMUnitTestCase.class);
	
	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
//				Foo.class,
//				Bar.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
				"report.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/bugs/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.USE_SECOND_LEVEL_CACHE, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.USE_QUERY_CACHE, Boolean.TRUE.toString()); 
		configuration.setProperty( AvailableSettings.USE_QUERY_CACHE, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory" );
		configuration.setProperty( "net.sf.ehcache.configurationResourceName", "ehcache.xml" );
		configuration.setProperty( "ehcache-hibernate.instance-name" , "report" );
		
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		int numberOfReports = 1010;
		generateReports( numberOfReports );
		List<Report> reports = readReports();
		readReports();
		Assert.assertEquals(numberOfReports, reports.size());
	}

	private List<Report> readReports() {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Report> reports = s.createCriteria(Report.class).list();
		LOG.info("Reports loaded: " + reports.size());
		reports.stream().forEach(r -> { 
			LOG.info(r.toString());
		});
		tx.commit();
		s.close();
		return reports;
	}

	private void generateReports(int number) {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		for(int i = 0; i < number;i++ )
			s.save(new Report("test" + i, i));

		tx.commit();
		s.close();
	}
}
