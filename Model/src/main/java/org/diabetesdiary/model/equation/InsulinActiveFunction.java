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



package org.diabetesdiary.model.equation;

import java.util.List;
import org.diabetesdiary.model.Const;
import org.diabetesdiary.model.InsMonData;

/**
 *
 * @author Jiri Majer
 */
public class InsulinActiveFunction implements Function{    
    
    /** Creates a new instance of InsulinFunction */
    public InsulinActiveFunction() {        
    }
    
    public double count(double t, double ... x) {        
        return Const.K1 * x[0] - Const.K2 * x[1];
    }    
    
}
