/*
 * InsulinAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */

package org.diabetesdiary;

import junit.framework.*;
import java.util.Collection;
import org.diabetesdiary.datamodel.api.InsulinAdministrator;
import org.diabetesdiary.datamodel.InsulinAdminImpl;
import org.diabetesdiary.datamodel.pojo.Insulin;
import org.diabetesdiary.datamodel.pojo.InsulinType;

/**
 *
 * @author vklidu
 */
public class InsulinAdminImplTest extends TestCase {
    
    public InsulinAdminImplTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(InsulinAdminImplTest.class);
        
        return suite;
    }
    
    
    /**
     * Test of getInsuliness method, of class org.diabetesdiary.InsulinAdminImpl.
     */
    public void testInsulines() {
        System.out.println("Insulines");
        
        InsulinAdministrator instance = InsulinAdminImpl.getInstance();
        InsulinType type = new InsulinType();
        type.setDescription("Pokusny inzulin");
        type.setName("Test");
        instance.newInsulinType(type);
        type = instance.getInsulinType(type.getId());
    }

    
}
