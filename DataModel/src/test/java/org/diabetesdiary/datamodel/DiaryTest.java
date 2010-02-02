/*
 * DiaryTest.java
 * JUnit based test
 *
 * Created on 23. březen 2006, 18:07
 */
package org.diabetesdiary.datamodel;

import java.sql.Date;
import junit.framework.*;
import org.diabetesdiary.datamodel.pojo.Patient;
import java.util.Collection;
import org.diabetesdiary.datamodel.api.Diary;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTest extends TestCase {

    public DiaryTest(String testName) {
        super(testName);
    }

    public void testNewPatientNullID() {
        System.out.println("testNewPatientNullID");
        Patient patient = new Patient(null, "Jiří", "Majer", true,
                Date.valueOf("1983-07-17"), Date.valueOf("2000-10-10"), "email", "Adresa", "+4200");

        Diary instance = DiaryImpl.getInstance();
        try {
            instance.newPatient(patient);
            fail("Exception wasn't invoked. The id_patient is null!");
        } catch (Throwable e) {
        }
    }

    public void testSameIDPatient() {
        System.out.println("testSameIDPatient");
        Patient patient = new Patient("NEWPATIENTTEST", "Jiří", "Majer", true,
                Date.valueOf("1983-07-17"), Date.valueOf("2000-01-01"), "email", "Adresa", "+4200");

        Diary instance = DiaryImpl.getInstance();
        try {
            instance.newPatient(patient);
            assertEquals(patient, instance.getPatient(patient.getIdPatient()));
            instance.newPatient(patient);
            fail("Patient with same id inserted");
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Test of getPatients method, of class org.diabetesdiary.DiaryImpl.
     */
    public void testGetPatients() {
        System.out.println("getPatients");

        Diary instance = DiaryImpl.getInstance();
        try {
            Collection expResult = null;
            Collection result = instance.getPatients();
            for (Object patient : result) {
                System.out.println(((Patient) patient).getName());
            }
            assertEquals(result.isEmpty(), false);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in getPatients");
        }
    }
}
