/*
 * DiaryTest.java
 * JUnit based test
 *
 * Created on 23. březen 2006, 18:07
 */
package org.diabetesdiary.diary;

import java.util.List;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.Patient;
import org.hibernate.ObjectNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTest extends AbstractAppContextTest {

    @Autowired
    private transient DiaryRepository repos;

    @Test
    public void testGetPatients() {
        List<Patient> result = repos.getPatients();
        for (Patient patient : result) {
            System.out.println(patient.getName());
        }
    }

    @Test
    public void testGetPatient() {
        try {
            repos.getPatient(Long.MIN_VALUE);
            Assert.fail();
        } catch (ObjectNotFoundException e) {
        }
    }
}
