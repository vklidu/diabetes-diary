/*
 * DiaryTest.java
 * JUnit based test
 *
 * Created on 23. březen 2006, 18:07
 */

package org.diabetesdiary;

import java.sql.Timestamp;
import java.sql.Date;
import junit.framework.*;
import org.diabetesdiary.datamodel.DiaryImpl;
import org.diabetesdiary.datamodel.FoodAdministratorImpl;
import org.diabetesdiary.datamodel.pojo.Patient;
import java.util.Collection;
import java.util.List;
import org.diabetesdiary.datamodel.api.Diary;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.pojo.Food;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordFoodPK;
import org.diabetesdiary.datamodel.pojo.GlykSeason;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTest extends TestCase {
    
    public DiaryTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DiaryTest.class);
        
        return suite;
    }
    
    /**
     * Test of newPatient method, of class org.diabetesdiary.DiaryImpl.
     */
    public void testNewPatientNullID() {
        System.out.println("testNewPatientNullID");
        Patient patient = new Patient(null,"Jiří","Majer",true,
                Date.valueOf("1983-07-17"),Date.valueOf("2000-10-10"),"email","Adresa","+4200");
        
        Diary instance = DiaryImpl.getInstance();
        try{
            instance.newPatient(patient);
            fail("Exception wasn't invoked. The id_patient is null!");
        }catch(RuntimeException e){
            //e.printStackTrace();
        }
    }
    
    public void testNewPatientOK() {
        System.out.println("testNewPatientOK");
        Patient patient = new Patient("830717/4117","Jiří","Majer",true,
                Date.valueOf("1983-07-17"),Date.valueOf("2000-01-01"),"email","Adresa","+4200");
        
        Diary instance = DiaryImpl.getInstance();
        try{
            instance.newPatient(patient);
        }catch(RuntimeException e){
            //e.printStackTrace();
            fail("Creating new patient fail!");
        }
    }
    
    public void testSameIDPatient() {
        System.out.println("testSameIDPatient");
        Patient patient = new Patient("NEWPATIENTTEST","Jiří","Majer",true,
                Date.valueOf("1983-07-17"),Date.valueOf("2000-01-01"),"email","Adresa","+4200");
        
        Diary instance = DiaryImpl.getInstance();
        try{
            instance.newPatient(patient);
            assertEquals(patient,instance.getPatient(patient.getIdPatient()));
            instance.newPatient(patient);
            fail("Patient with same id inserted");
        }catch(Exception e){
            //e.printStackTrace();
        }
    }
    
    /**
     * Test of getPatients method, of class org.diabetesdiary.DiaryImpl.
     */
    public void testGetPatients() {
        System.out.println("getPatients");
        
        Diary instance = DiaryImpl.getInstance();
        try{
            Collection expResult = null;
            Collection result = instance.getPatients();
            for(Object patient: result){
                System.out.println(((Patient)patient).getName());
            }
            assertEquals(result.isEmpty(), false);
        }catch(Exception e){
            e.printStackTrace();
            fail("Exception in getPatients");
        }
    }
    
    
    
    
}
