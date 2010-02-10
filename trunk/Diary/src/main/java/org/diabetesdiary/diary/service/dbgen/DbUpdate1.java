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
package org.diabetesdiary.diary.service.dbgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import org.diabetesdiary.diary.domain.WKFood;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.ActivityGroupDO;
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodGroupDO;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InsulinTypeDO;
import org.diabetesdiary.diary.service.db.InvestigationDO;
import org.diabetesdiary.diary.service.db.InvestigationGroupDO;

/**
 * @author Jirka Majer
 */
public class DbUpdate1 extends AbstractDbUpdate {

    @Override
    public void update() {
        try {
            initInsulinesFromCSV();
            initInvestigationsFromCSV();
            initFoodsFromCSV();
            initActivitiesFromCSV();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initInvestigationsFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DbUpdate1.class.getResourceAsStream("investigations.csv"), "utf-8"));
        InvestigationGroupDO lastGroup = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 6) {
                //is group
                if (line[0].length() > 0) {
                    lastGroup = new InvestigationGroupDO();
                    lastGroup.setName(line[0]);
                    getSession().save(lastGroup);
                } else if (lastGroup != null) {
                    InvestigationDO invest = new InvestigationDO();
                    invest.setGroup(lastGroup);
                    if (!line[5].equals("null")) {
                        invest.setWkinvest(WKInvest.valueOf(line[5]));
                    }
                    invest.setName(line[1]);
                    invest.setUnit(line[2]);
                    invest.setNormalMax(Double.valueOf(line[3]));
                    invest.setNormalMin(Double.valueOf(line[4]));
                    getSession().save(invest);
                }
            }
        }
    }

    private void initInsulinesFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DbUpdate1.class.getResourceAsStream("insulins.csv"), "utf-8"));
        InsulinTypeDO lastType = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 6) {
                if (line[0].length() > 0) {
                    lastType = new InsulinTypeDO();
                    lastType.setName(line[0]);
                    lastType.setDescription(line[1]);
                    lastType.setParameterA(getNotExceptionDouble(line[3]));
                    lastType.setParameterB(getNotExceptionDouble(line[4]));
                    lastType.setParameterS(getNotExceptionDouble(line[5]));
                    getSession().save(lastType);
                } else {
                    InsulinDO ins = new InsulinDO();
                    ins.setType(lastType);
                    ins.setName(line[2]);
                    getSession().save(ins);
                }
            }
        }
    }

    private void initActivitiesFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DbUpdate1.class.getResourceAsStream("activities.csv"), "utf-8"));
        ActivityGroupDO lastGroup = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length == 3) {
                if (line[0].length() > 0) {
                    lastGroup = new ActivityGroupDO();
                    lastGroup.setName(line[0]);
                    lastGroup.setDescription(line[1]);
                    getSession().save(lastGroup);
                } else {
                    ActivityDO activity = new ActivityDO();
                    activity.setActivityGroup(lastGroup);
                    activity.setName(line[1]);
                    activity.setPower(getNotExceptionDouble(line[2]));
                    getSession().save(activity);
                }
            }
        }
    }

    private void initFoodsFromCSV() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DbUpdate1.class.getResourceAsStream("foods.csv"), "utf-8"));
        FoodGroupDO lastGroup = null;
        FoodGroupDO baseGroup = null;
        while (reader.ready()) {
            String[] line = getCsvLine(reader.readLine());
            if (line.length >= 10) {
                if (line[1].equals("null") && line[2].equals("null")) {
                    FoodGroupDO group = new FoodGroupDO();
                    group.setName(line[0]);
                    if (line[9].equals("null")) {
                        getSession().save(group);
                        baseGroup = group;
                        lastGroup = baseGroup;
                    } else {
                        group.setParent(baseGroup);
                        getSession().save(group);
                        lastGroup = group;
                    }

                } else if (lastGroup != null) {
                    FoodDO food = new FoodDO();
                    food.setName(line[0]);
                    if (line.length > 10 && !line[10].equals("null")) {
                        food.setWkfood(WKFood.valueOf(line[10]));
                    }
                    food.setEnergy(getNotExceptionDouble(line[1]));
                    food.setProtein(getNotExceptionDouble(line[2]));
                    food.setFat(getNotExceptionDouble(line[3]));
                    food.setSugar(getNotExceptionDouble(line[4]));
                    food.setCholesterol(getNotExceptionDouble(line[5]));
                    food.setRoughage(getNotExceptionDouble(line[6]));
                    food.setFoodGroup(lastGroup);

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
                        if (line[7].equals("g")) {
                            throw new RuntimeException(food.getName() + "Bad koeficient gram to gram != 1");
                        }
                    }
                    getSession().save(food);

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
                        getSession().save(unit2);
                    }
                    getSession().save(unit);
                }
            }
        }
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
}
