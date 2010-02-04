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
package org.diabetesdiary.diary.service.db;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

/**
 * Mame 4 zakladni druhy inzulinu. Deli se podle rychlosti vstrebavani do tela
 * a nastupem ucinku. (rapid, stredni, pomaly, analog)
 * @hibernate.class table="insulin_type"
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "insulin_type")
public class InsulinTypeDO extends AbstractDO {

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, name="parameter_s")
    private Double parameterS;

    @Column(nullable=false)
    private Double parameterA;

    @Column(nullable=false)
    private Double parameterB;

    @Column(nullable=false)
    private String description;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="type")
    private Set<InsulinDO> insulins = new HashSet<InsulinDO>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getParameterS() {
        return parameterS;
    }

    public void setParameterS(Double parameterS) {
        this.parameterS = parameterS;
    }

    public Double getParameterA() {
        return parameterA;
    }

    public void setParameterA(Double parameterA) {
        this.parameterA = parameterA;
    }

    public Double getParameterB() {
        return parameterB;
    }

    public void setParameterB(Double parameterB) {
        this.parameterB = parameterB;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InsulinDO> getInsulins() {
        return insulins;
    }

    public void setInsulins(Set<InsulinDO> insulins) {
        this.insulins = insulins;
    }

    @Override
    public String toString() {
        return name;
    }

}
