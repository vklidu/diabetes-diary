/*
 *   Copyright (C) 2006-2010 Jiri Majer. All Rights Reserved.
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
package org.diabetesdiary.print.pdf;

import com.itextpdf.text.Rectangle;

/**
 *
 * @author Jirka Majer
 */
public enum PageSize {

    A0(com.itextpdf.text.PageSize.A0),
    A1(com.itextpdf.text.PageSize.A1),
    A2(com.itextpdf.text.PageSize.A2),
    A3(com.itextpdf.text.PageSize.A3),
    A4(com.itextpdf.text.PageSize.A4),
    A5(com.itextpdf.text.PageSize.A5),
    
    B0(com.itextpdf.text.PageSize.B0),
    B1(com.itextpdf.text.PageSize.B1),
    B2(com.itextpdf.text.PageSize.B2),
    B3(com.itextpdf.text.PageSize.B3),
    B4(com.itextpdf.text.PageSize.B4),
    B5(com.itextpdf.text.PageSize.B5);
    
    private final Rectangle itextSize;

    private PageSize(Rectangle size) {
        this.itextSize = size;
    }

    public Rectangle getPageSize() {
        return itextSize;
    }
}
