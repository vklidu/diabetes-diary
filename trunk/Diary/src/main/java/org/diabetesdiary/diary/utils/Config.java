/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diabetesdiary.diary.utils;

import java.io.File;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 *
 * @author Jirka Majer
 */
public class Config extends PropertyPlaceholderConfigurer {

    private Properties properties;
    private String dbUrl;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        this.properties = props;
        init();
    }

    protected void init() {
        String dbFile = System.getProperty("user.home") + File.separator + ".diabetesdiary" + File.separator + "1.2" + File.separator + "data";
        dbUrl = "jdbc:hsqldb:file:" + dbFile;
    }

    public String getDbUrl() {
        return dbUrl;
    }

}
