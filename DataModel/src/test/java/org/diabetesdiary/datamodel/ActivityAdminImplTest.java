/*
 * ActivityAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */

package org.diabetesdiary;

import junit.framework.*;
import java.util.Collection;
import org.diabetesdiary.datamodel.api.ActivityAdministrator;
import org.diabetesdiary.datamodel.ActivityAdminImpl;
import org.diabetesdiary.datamodel.pojo.Activity;
import org.diabetesdiary.datamodel.pojo.ActivityGroup;
import org.diabetesdiary.datamodel.util.HibernateUtil;

/**
 *
 * @author vklidu
 */
public class ActivityAdminImplTest extends TestCase {
    
    public ActivityAdminImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ActivityAdminImplTest.class);
        
        return suite;
    }

 
    /**
     * Test of getActivities method, of class org.diabetesdiary.ActivityAdminImpl.
     */
    public void testActivities() {
        System.out.println("Activities");
        ActivityAdministrator instance = ActivityAdminImpl.getInstance();
        instance.getActivityGroups();        
    }
   
    
}
