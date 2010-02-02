/*
 * ActivityAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */

package org.diabetesdiary.datamodel;

import junit.framework.*;
import org.diabetesdiary.datamodel.api.ActivityAdministrator;

/**
 *
 * @author Jirka Majer
 */
public class ActivityAdminImplTest extends TestCase {

    public void testActivities() {
        ActivityAdministrator instance = ActivityAdminImpl.getInstance();
        instance.getActivityGroups();
    }
    
}
