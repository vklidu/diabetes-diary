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

import java.util.List;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.InvestigationGroup;

/**
 *
 * @author Jiri Majer
 */
public interface InvestigationAdministrator {
    
    public List<Investigation> getInvestigations();
    
    public List<InvestigationGroup> getInvestigationGroups();
    
    public Investigation getInvestigation(Integer idInvestigation);
    
    public InvestigationGroup getInvestigationGroup(Integer idGroup);
    
    public void newInvestigation(Investigation invest);
    
    public void newInvestigationGroup(InvestigationGroup group);
 
    public void updateInvestigation(Investigation inv);
    
    public void updateGroup(InvestigationGroup group);
    
    public void deleteFood(Investigation inv);
    
    public void deleteInvestigationGroup(InvestigationGroup group);
}
