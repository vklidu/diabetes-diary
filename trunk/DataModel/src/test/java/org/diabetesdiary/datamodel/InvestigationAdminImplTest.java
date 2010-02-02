/*
 * InvestigationAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */
package org.diabetesdiary.datamodel;

import junit.framework.*;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;

/**
 *
 * @author Jiri Majer
 */
public class InvestigationAdminImplTest extends TestCase {

    public InvestigationAdminImplTest(String testName) {
        super(testName);
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

    