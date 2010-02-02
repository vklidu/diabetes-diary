/*
 * InsulinAdminImplTest.java
 * JUnit based test
 *
 * Created on 27. duben 2006, 16:43
 */
package org.diabetesdiary.datamodel;

import junit.framework.*;
import org.diabetesdiary.datamodel.api.InsulinAdministrator;
import org.diabetesdiary.datamodel.pojo.InsulinType;

/**
 *
 * @author vklidu
 */
public class InsulinAdminImplTest extends TestCase {

    public InsulinAdminImplTest(String testName) {
        super(testName);
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
        type.setParameterA(1.0);
        type.setParameterB(1.0);
        type.setParameterS(1.0);
        instance.newInsulinType(type);
        type = instance.getInsulinType(type.getId());
    }
}
