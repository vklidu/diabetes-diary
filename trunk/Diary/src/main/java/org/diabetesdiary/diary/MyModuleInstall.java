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
package org.diabetesdiary.diary;

import java.io.IOException;
import org.openide.modules.ModuleInstall;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 *
 * @author Jiri Majer
 */
public class MyModuleInstall extends ModuleInstall {

    private GenericApplicationContext ctx;

    @Override
    public synchronized void restored() {
        super.restored();
        this.ctx = new GenericApplicationContext() {

            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                return new PathMatchingResourcePatternResolverEx(this);
            }
        };

        //set Spring's classloader to context classloader
        this.ctx.setClassLoader(Thread.currentThread().getContextClassLoader());

        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
        ctx.refresh();
    }

    
    private static class PathMatchingResourcePatternResolverEx extends PathMatchingResourcePatternResolver {

        public PathMatchingResourcePatternResolverEx(ResourceLoader resourceLoader) {
            super(resourceLoader);
        }

        public PathMatchingResourcePatternResolverEx(ClassLoader classLoader) {
            super(classLoader);
        }

        public PathMatchingResourcePatternResolverEx() {
            super();
        }

        @Override
        protected boolean isJarResource(Resource resource) throws IOException {
            return super.isJarResource(resource) || "nbjcl".equals(resource.getURL().getProtocol());
        }
    }
}
