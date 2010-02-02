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
import org.diabetesdiary.datamodel.api.ActivityAdministrator;
import org.diabetesdiary.datamodel.pojo.Activity;
import org.diabetesdiary.datamodel.pojo.ActivityGroup;
import org.diabetesdiary.datamodel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Jiri Majer
 */
public class ActivityAdminImpl implements ActivityAdministrator {

    private static final ActivityAdministrator admin = new ActivityAdminImpl();

    public static ActivityAdministrator getInstance() {
        return admin;
    }

    @Override
    public List<Activity> getActivities() {
        return HibernateUtil.getAllObjects(Activity.class, "Cannot obtain collection of Activities.");
    }

    @Override
    public List<ActivityGroup> getActivityGroups() {
        return HibernateUtil.getAllObjects(ActivityGroup.class, "Cannot obtain collection of ActivityGroups.");
    }

    @Override
    public Activity getActivity(Integer idActivity) {
        return (Activity) HibernateUtil.getObject(Activity.class, idActivity, "Cannot obtain Activity by id.");
    }

    @Override
    public ActivityGroup getActivityGroup(Integer idGroup) {
        return (ActivityGroup) HibernateUtil.getObject(ActivityGroup.class, idGroup, "Cannot obtain ActivityGroup by id.");
    }

    @Override
    public void newActivity(Activity activity) {
        HibernateUtil.newObject(activity, "Cannot create new Activity.");
    }

    @Override
    public void newActivityGroup(ActivityGroup group) {
        HibernateUtil.newObject(group, "Cannot create new ActivityGroup.");
    }

    @Override
    public void updateActivity(Activity activity) {
        HibernateUtil.updateObject(activity, "Cannot update Activity.");
    }

    @Override
    public void updateActivityGroup(ActivityGroup group) {
        HibernateUtil.updateObject(group, "Cannot update ActivityGroup.");
    }

    @Override
    public void deleteActivity(Activity activity) {
        HibernateUtil.deleteObject(activity, "Cannot delete Activity.");
    }

    @Override
    public void deleteActivityGroup(ActivityGroup group) {
        HibernateUtil.deleteObject(group, "Cannot delete ActivityGroup.");
    }

    @Override
    public List<Activity> getActivities(Integer idGroup) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(Activity.class).add(Restrictions.eq("activityGroup.id", idGroup)).list();
            return result;
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
