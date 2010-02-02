/*
 * FoodAdministratorTest.java
 * JUnit based test
 *
 * Created on 6. duben 2006, 18:42
 */

package org.diabetesdiary;

import junit.framework.*;
import java.util.Collection;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.FoodAdministratorImpl;
import org.diabetesdiary.datamodel.pojo.Food;
import org.diabetesdiary.datamodel.pojo.FoodGroup;
import org.diabetesdiary.datamodel.pojo.FoodUnit;
import org.diabetesdiary.datamodel.pojo.FoodUnitPK;

/**
 *
 * @author Jiri Majer
 */
public class FoodAdministratorTest extends TestCase {
    
    public FoodAdministratorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FoodAdministratorTest.class);
        
        return suite;
    }
    
    
    /**
     * Test of getFoods method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testGetFoods() {
        try{
            System.out.println("getFoods");
            Food food = new Food();
            food.setEnergy(Double.valueOf(1000));
            food.setFat(Double.valueOf(20));
            food.setName("Broskev");
            food.setProtein(Double.valueOf(10));
            food.setSugar(Double.valueOf(30));
            FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
            
            foodAdministrator.newFood(food);
            Collection result = foodAdministrator.getFoods();
            assertTrue(result.contains(food));
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
    
    /**
     * Test of getFood method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testGetFood() {
        System.out.println("getFood");
        Food food = new Food();
        food.setIdFood(Integer.valueOf(Integer.MAX_VALUE));
        food.setName("Malina");
        food.setEnergy(Double.valueOf(100));
        food.setFat(Double.valueOf(20));
        food.setProtein(Double.valueOf(10));
        food.setSugar(Double.valueOf(30));
        
        
        FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
        foodAdministrator.newFood(food);
        Food result = foodAdministrator.getFood(food.getIdFood());
        assertEquals(food, result);
    }
    
    
    /**
     * Test of newFoodGroup method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testNewFoodGroup() {
        System.out.println("newFoodGroup");
        
        FoodGroup group = new FoodGroup();
        group.setName("Zelenina");        
        FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
        foodAdministrator.newFoodGroup(group);
        assertEquals(group,foodAdministrator.getFoodGroup(group.getId()));
    }
    
    /**
     * Test of updateFood method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testUpdateFood() {
        System.out.println("updateFood");
        
        Food food = new Food();
        food.setEnergy(Double.valueOf(1000));
        food.setFat(Double.valueOf(20));
        food.setName("Broskev");
        food.setProtein(Double.valueOf(10));
        food.setSugar(Double.valueOf(30));
        
        FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
        foodAdministrator.newFood(food);
        
        food = foodAdministrator.getFood(food.getIdFood());
        FoodUnit unit = new FoodUnit();
        unit.setId(new FoodUnitPK(food.getIdFood(),"hrst"));
        unit.setKoef(Double.valueOf(1));
        food.getUnits().add(unit);
        food.setName("NEW");
        foodAdministrator.updateFood(food);
        
        assertEquals(food.getName(),foodAdministrator.getFood(food.getIdFood()).getName());
    }
    
    /**
     * Test of updateGroup method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testUpdateGroup() {
        System.out.println("updateGroup");
        
        FoodGroup group = new FoodGroup();
        group.setName("Mleko");        
        try{
            FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
            foodAdministrator.newFoodGroup(group);
            group = foodAdministrator.getFoodGroup(group.getId());
            group.setName("Ovoce");           
            foodAdministrator.updateGroup(group);
            
            FoodGroup group2 = new FoodGroup();
            FoodGroup group3 = new FoodGroup();            
            group3.setName("Podskupina 2");                        
            group2.setName("Podskupina");            
            group.getGroups().add(group2);
            group.getGroups().add(group3);
            foodAdministrator.updateGroup(group);            
            assertEquals(group.getName(),foodAdministrator.getFoodGroup(group.getId()).getName());
        }catch(Exception e){
            e.printStackTrace();
            fail("ee");
        }
    }
    
    /**
     * Test of deleteFood method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testDeleteFood() {
        System.out.println("deleteFood");
        
        Food food = new Food();        
        FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
        foodAdministrator.newFood(food);
        food = foodAdministrator.getFood(food.getIdFood());
        foodAdministrator.deleteFood(food);
        assertNull(foodAdministrator.getFood(food.getIdFood()));
    }
    
    /**
     * Test of deleteFoodGroup method, of class org.diabetesdiary.FoodAdministrator.
     */
    public void testDeleteFoodGroup() {
        System.out.println("deleteFoodGroup");
        
        FoodGroup group = new FoodGroup();
        group.setName("test");
        FoodAdministrator foodAdministrator = FoodAdministratorImpl.getInstance();
        foodAdministrator.newFoodGroup(group);
        assertNotNull(foodAdministrator.getFoodGroup(group.getId()));
        foodAdministrator.deleteFoodGroup(group);
        assertNull(foodAdministrator.getFoodGroup(group.getId()));
    }
    
}
