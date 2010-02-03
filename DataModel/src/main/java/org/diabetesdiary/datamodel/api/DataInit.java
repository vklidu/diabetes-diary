/*
 *   Copyright (C) 2006-2007 Jiri Majer. All Rights Reserved.
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   This code is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License version 2 only, as
 *   published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.diabetesdiary.datamodel.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.diabetesdiary.datamodel.DiaryRepositoryImpl;
import org.diabetesdiary.datamodel.DiaryServiceImpl;
import org.diabetesdiary.datamodel.pojo.ActivityDO;
import org.diabetesdiary.datamodel.pojo.ActivityGroupDO;
import org.diabetesdiary.datamodel.pojo.FoodDO;
import org.diabetesdiary.datamodel.pojo.FoodGroupDO;
import org.diabetesdiary.datamodel.pojo.FoodUnitDO;
import org.diabetesdiary.datamodel.pojo.InsulinDO;
import org.diabetesdiary.datamodel.pojo.InsulinTypeDO;
import org.diabetesdiary.datamodel.pojo.InvestigationDO;
import org.diabetesdiary.datamodel.pojo.InvestigationGroupDO;

/**
 *
 * @author Jiri Majer
 */
public class DataInit {
/*
    private static DiaryService service = DiaryServiceImpl.getInstance();
    private static DiaryRepository repo = DiaryRepositoryImpl.getInstance();

    private DataInit() {
    }

    public static void createExampleData() {

        //ulozim insuliny z csv
        if (HibernateUtil.getAllObjects(InsulinTypeDO.class, "example data error").size() < 1) {
            try {
                for (InsulinTypeDO type : getTypesFromCSV()) {
                    Set insulines = type.getInsulins();
                    type.setInsulins(null);
                    HibernateUtil.newObject(type, "err");
                    for (Object ins : insulines) {
                        HibernateUtil.newObject(ins, "err");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //ulozim vysetreni z csv
        if (HibernateUtil.getAllObjects(InvestigationDO.class, "example data error").size() < 1) {
            try {
                initInvestigations();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //ulozim jidlo z csv
        if (HibernateUtil.getAllObjects(FoodUnitDO.class, "Example data init error.").size() < 1) {
            try {
                fillFoodTable();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //ulozim aktivity z csv
        if (HibernateUtil.getAllObjects(ActivityGroupDO.class, "Example data init error.").size() < 1) {
            try {
                for (ActivityGroupDO group : getActivityGroupsFromCSV()) {
                    Set<ActivityDO> actvs = group.getActivities();
                    group.setActivities(null);
                    HibernateUtil.newObject(group, "err");
                    for (ActivityDO act : actvs) {
                        if (act.getPower() > 0) {
                            HibernateUtil.newObject(act, "err");
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void initInvestigations() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DataInit.class.getResourceAsStream("investigations.csv"), "utf-8"));
        InvestigationGroupDO lastGroup = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 5) {
                //is group
                if (line[0].length() > 0) {
                    lastGroup = new InvestigationGroupDO();
                    lastGroup.setName(line[0]);
                    HibernateUtil.newObject(lastGroup, "example error");
                } else {
                    InvestigationDO invest = new InvestigationDO();
                    invest.setGroup(lastGroup);
                    invest.setName(line[1]);
                    invest.setUnit(line[2]);
                    invest.setNormalMax(Double.valueOf(line[3]));
                    invest.setNormalMin(Double.valueOf(line[4]));
                    HibernateUtil.newObject(invest, "err");
                }
            }
        }
    }

    private static List<InsulinTypeDO> getTypesFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DataInit.class.getResourceAsStream("insulins.csv"), "utf-8"));
        List<InsulinTypeDO> result = new Vector<InsulinTypeDO>();
        InsulinTypeDO lastType = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 6) {
                if (line[0].length() > 0) {
                    lastType = new InsulinTypeDO();
                    lastType.setInsulins(new HashSet());
                    result.add(lastType);
                    lastType.setName(line[0]);
                    lastType.setDescription(line[1]);
                    lastType.setParameterA(getNotExceptionDouble(line[3]));
                    lastType.setParameterB(getNotExceptionDouble(line[4]));
                    lastType.setParameterS(getNotExceptionDouble(line[5]));
                } else {
                    InsulinDO ins = new InsulinDO();
                    ins.setType(lastType);
                    lastType.getInsulins().add(ins);
                    ins.setName(line[2]);
                }
            }
        }
        return result;
    }

    private static List<ActivityGroupDO> getActivityGroupsFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DataInit.class.getResourceAsStream("activities.csv"), "utf-8"));
        List<ActivityGroupDO> result = new Vector<ActivityGroupDO>();
        ActivityGroupDO lastGroup = null;
        int id = 1;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 3) {
                if (line[0].length() > 0) {
                    lastGroup = new ActivityGroupDO();
                    lastGroup.setActivities(new HashSet());
                    result.add(lastGroup);
                    lastGroup.setName(line[0]);
                    lastGroup.setDescription(line[1]);
                } else {
                    ActivityDO activity = new ActivityDO();
                    activity.setActivityGroup(lastGroup);
                    lastGroup.getActivities().add(activity);
                    activity.setName(line[1]);
                    activity.setPower(getNotExceptionDouble(line[2]));
                }
            }
        }
        return result;
    }

    private static String[] getCsvLine(String line) {
        StringTokenizer tok = new StringTokenizer(line, ";");
        String[] result = new String[tok.countTokens()];
        int i = 0;
        while (tok.hasMoreTokens()) {
            result[i++] = tok.nextToken().replaceAll("\"", "");
        }
        return result;
    }

    private static Double getNotExceptionDouble(String value) {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException nf) {
            return null;
        }
    }

    private static void fillFoodTable() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DataInit.class.getResourceAsStream("foods.csv"), "utf-8"));
        FoodGroupDO lastGroup = null;
        FoodGroupDO baseGroup = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 10) {
                try {
                    if (line[1].equals("null") && line[2].equals("null")) {
                        FoodGroupDO group = new FoodGroupDO();
                        group.setName(line[0]);
                        if (line[9].equals("null")) {
                            admin.newFoodGroup(group);
                            baseGroup = admin.getFoodGroup(group.getId());
                            lastGroup = baseGroup;
                        } else {
                            group.setParent(baseGroup);
                            admin.newFoodGroup(group);
                            lastGroup = admin.getFoodGroup(group.getId());
                        }

                    } else if (lastGroup != null) {
                        FoodDO food = new FoodDO();
                        food.setName(line[0]);
                        food.setEnergy(getNotExceptionDouble(line[1]));
                        food.setProtein(getNotExceptionDouble(line[2]));
                        food.setFat(getNotExceptionDouble(line[3]));
                        food.setSugar(getNotExceptionDouble(line[4]));
                        food.setCholesterol(getNotExceptionDouble(line[5]));
                        food.setRoughage(getNotExceptionDouble(line[6]));
                        food.setFoodGroup(lastGroup);
                        admin.newFood(food);

                        FoodUnitDO unit = new FoodUnitDO();
                        unit.setFood(food);
                        unit.setUnit(line[7]);
                        unit.setShortcut(line[7]);
                        unit.setName(line[7]);
                        double koef = Double.valueOf(line[8]);
                        if (koef != 100) {
                            food.setEnergy(food.getEnergy() * 100 / koef);
                            food.setProtein(food.getProtein() * 100 / koef);
                            food.setFat(food.getFat() * 100 / koef);
                            food.setSugar(food.getSugar() * 100 / koef);
                            if (food.getCholesterol() != null) {
                                food.setCholesterol(food.getCholesterol() * 100 / koef);
                            }
                            if (food.getRoughage() != null) {
                                food.setRoughage(food.getRoughage() * 100 / koef);
                            }
                            admin.updateFood(food);
                            if (line[7].equals("g")) {
                                throw new RuntimeException(food.getName() + "Bad koeficient gram to gram != 1");
                            }
                        }

                        if (line[7].equals("g") || line[7].equals("ml")) {
                            unit.setKoef(1d);
                        } else {
                            unit.setKoef(Double.valueOf(line[8]));
                        }

                        if (line[7].equals("g")) {
                            unit.setShortcut("g");
                            unit.setName("gram");
                        } else if (line[7].equals("ml")) {
                            unit.setShortcut("ml");
                            unit.setName("mililitr");
                        } else if (line[7].equals("ks")) {
                            unit.setShortcut("ks");
                            unit.setName("kus");
                        } else if (line[7].equals("u10")) {
                            unit.setShortcut("j.");
                            unit.setName("jednotka 10g");
                            unit.setKoef(10d);
                            //pridam jeste druhou jednotku
                            FoodUnitDO unit2 = new FoodUnitDO();
                            unit2.setFood(food);
                            unit2.setUnit("u12");
                            unit2.setKoef(12d);
                            unit2.setShortcut("j.");
                            unit2.setName("jednotka 12g");
                            HibernateUtil.newObject(unit2, "err");
                        }


                        HibernateUtil.newObject(unit, "err");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
 */
}
