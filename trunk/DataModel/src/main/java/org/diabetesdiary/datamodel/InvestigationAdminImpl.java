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



package org.diabetesdiary.datamodel;

import java.util.List;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.InvestigationGroup;
import org.diabetesdiary.datamodel.util.HibernateUtil;

/**
 *
 * @author Jiri Majer
 */
public class InvestigationAdminImpl implements InvestigationAdministrator{
    
    private static final InvestigationAdministrator investAdmin = new InvestigationAdminImpl();
    
    
    public static InvestigationAdministrator getInstance(){
        return investAdmin;
    }

    public List<Investigation> getInvestigations() {
        return HibernateUtil.getAllObjects(Investigation.class,"Cannot obtain collection of Investigations.");        
    }

    public List<InvestigationGroup> getInvestigationGroups() {
        return HibernateUtil.getAllObjects(InvestigationGroup.class,"Cannot obtain collection of InvestigationGroups.");        
    }

    public Investigation getInvestigation(Integer idInvestigation) {
        return (Investigation) HibernateUtil.getObject(Investigation.class,idInvestigation,"Cannot obtain Investigation by Id.");        
    }

    public InvestigationGroup getInvestigationGroup(Integer idGroup) {
        return (InvestigationGroup) HibernateUtil.getObject(InvestigationGroup.class,idGroup,"Cannot obtain InvestigationGroup by Id.");        
    }

    public void newInvestigation(Investigation invest) {
        HibernateUtil.newObject(invest,"Cannot insert new Investigation.");
    }

    public void newInvestigationGroup(InvestigationGroup group) {
        HibernateUtil.newObject(group,"Cannot insert new InvestigationGroup.");
    }

    public void updateInvestigation(Investigation inv) {
        HibernateUtil.updateObject(inv,"Cannot update Investigation.");
    }

    public void updateGroup(InvestigationGroup group) {
        HibernateUtil.updateObject(group,"Cannot update InvestigationGroup.");
    }

    public void deleteFood(Investigation inv) {
        HibernateUtil.deleteObject(inv,"Cannot delete Investigation.");
    }

    public void deleteInvestigationGroup(InvestigationGroup group) {
        HibernateUtil.deleteObject(group,"Cannot delete InvestigationGroup.");
    }
    
}
