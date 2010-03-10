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
package org.diabetesdiary.calendar.option;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;

public class CalendarSettings {

    private FileObject settingsFolder;
    private FileObject settingsFile;
    private FileLock lock;
    private Properties settings;
    /** Configuration file base name*/
    public static String PROPERTIES_BASE = "calendaroptions";
    /** Configuration file extension*/
    public static String PROPERTIES_EXTENSION = "properties";
    /** Key under which the carbohydrate unit is stored in the properties file*/
    public static final String KEY_CARBOHYDRATE_UNIT = "properties.carbohydrate.unit";
    public static final String KEY_GLYKEMIE_LOW_NORMAL = "properties.glykemie.lownormal";
    public static final String KEY_GLYKEMIE_HIGH_NORMAL = "properties.glykemie.highnormal";
    public static final String CURRENT_PATIENT_ID = "properties.patient.id";
    /** There can only be one!*/
    private static CalendarSettings SINGLETON = null;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private CalendarSettings() {
        settings = new Properties();
        setDefaultValues();
        settingsFolder = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("Settings");
        if (settingsFolder == null) {
            try {
                settingsFolder = Repository.getDefault().getDefaultFileSystem().getRoot().createFolder("Settings");
                store();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            load();
        }
    }

    public static CalendarSettings getSettings() {
        if (SINGLETON == null) {
            SINGLETON = new CalendarSettings();
        }
        return SINGLETON;
    }

    public void setDefaultValues() {
        settings.clear();
        settings.put(KEY_CARBOHYDRATE_UNIT, "u12");
        settings.put(KEY_GLYKEMIE_LOW_NORMAL, "4");
        settings.put(KEY_GLYKEMIE_HIGH_NORMAL, "9");
    }

    public void store() {
        try {
            settingsFile = settingsFolder.getFileObject(PROPERTIES_BASE, PROPERTIES_EXTENSION);
            if (settingsFile == null) {
                settingsFile = settingsFolder.createData(PROPERTIES_BASE, PROPERTIES_EXTENSION);
            }

            lock = settingsFile.lock();
            OutputStream out = settingsFile.getOutputStream(lock);
            settings.storeToXML(out, "Configuration File for My Options");
            out.close();
            lock.releaseLock();
        } catch (IOException ex) {
            // TODO file can not be created , do something about it
            ex.printStackTrace();
        }
    }

    public void load() {
        settingsFile = settingsFolder.getFileObject(PROPERTIES_BASE, PROPERTIES_EXTENSION);
        if (settingsFile != null) {
            try {
                InputStream in = settingsFile.getInputStream();
                settings.loadFromXML(in);
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getValue(String key) {
        return settings.getProperty(key);
    }

    public Long getValueAsLong(String key) {
        return settings.getProperty(key) != null ? Long.valueOf(settings.getProperty(key)) : null;
    }

    public void setValue(String key, Long value) {
        setValue(key, value.toString());
    }

    public void setValue(String key, String value) {
        String oldValue = settings.getProperty(key);
        settings.setProperty(key, value.trim());
        pcs.firePropertyChange(key, oldValue, value.trim());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
