/*
 * InvestigationAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */

package org.diabetesdiary;

import junit.framework.*;
import java.util.Collection;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;
import org.diabetesdiary.datamodel.InvestigationAdminImpl;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.InvestigationGroup;
import org.diabetesdiary.datamodel.util.HibernateUtil;

/**
 *
 * @author Jiri Majer
 */
public class InvestigationAdminImplTest extends TestCase {
    
    public InvestigationAdminImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(InvestigationAdminImplTest.class);
        
        return suite;
    }

    /**
     * Test of getInstance method, of class org.diabetesdiary.InvestigationAdminImpl.
     */
    public void testInvestigation() {
        System.out.println("getInstance");
        InvestigationAdministrator admin = InvestigationAdminImpl.getInstance();
        admin.getInvestigations();
    }

}

    