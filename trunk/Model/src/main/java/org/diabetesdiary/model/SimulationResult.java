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



package org.diabetesdiary.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jiri Majer
 */
public class SimulationResult {
    private Map<Date, SRElement> profile;
    
    /**
     * Creates a new instance of SimulationResult
     */
    public SimulationResult() {
        profile = new HashMap<Date,SRElement>();
    }
    
    public void addResult(Date date, SRElement ins){
        if(profile.containsKey(date)){
            throw new IllegalArgumentException("The date was saved yet.");
        }else{
            profile.put(date,ins);
        }
    }

    public Map<Date, SRElement> getProfile() {
        return Collections.unmodifiableMap(profile);
    }
    
}
