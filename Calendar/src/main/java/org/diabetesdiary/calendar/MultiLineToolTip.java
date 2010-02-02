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


package org.diabetesdiary.calendar;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;

/**
 *
 * @author Jiri Majer
 */

public class MultiLineToolTip extends JToolTip {
    private static final String uiClassID = "ToolTipUI";
    
    String tipText;
    JComponent component;
    
    public MultiLineToolTip() {
        updateUI();
    }
    
    public void updateUI() {
        setUI(MultiLineToolTipUI.createUI(this));
    }
    
    public void setColumns(int columns) {
        this.columns = columns;
        this.fixedwidth = 0;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public void setFixedWidth(int width) {
        this.fixedwidth = width;
        this.columns = 0;
    }
    
    public int getFixedWidth() {
        return fixedwidth;
    }
    
    protected int columns = 0;
    protected int fixedwidth = 0;
}



class MultiLineToolTipUI extends BasicToolTipUI {
    static MultiLineToolTipUI sharedInstance = new MultiLineToolTipUI();
    Font smallFont;
    static JToolTip tip;
    protected CellRendererPane rendererPane;
    
    private static JTextArea textArea ;
    
    public static ComponentUI createUI(JComponent c) {
        return sharedInstance;
    }
    
    public MultiLineToolTipUI() {
        super();
    }
    
    public void installUI(JComponent c) {
        super.installUI(c);
        tip = (JToolTip)c;
        rendererPane = new CellRendererPane();
        c.add(rendererPane);
    }
    
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        
        c.remove(rendererPane);
        rendererPane = null;
    }
    
    public void paint(Graphics g, JComponent c) {
        Dimension size = c.getSize();
        textArea.setBackground(c.getBackground());
        rendererPane.paintComponent(g, textArea, c, 1, 1,
                size.width - 1, size.height - 1, true);
    }
    
    public Dimension getPreferredSize(JComponent c) {
        String tipText = ((JToolTip)c).getTipText();
        if (tipText == null)
            return new Dimension(0,0);
        textArea = new JTextArea(tipText );
        rendererPane.removeAll();
        rendererPane.add(textArea );
        textArea.setWrapStyleWord(true);
        int width = ((MultiLineToolTip)c).getFixedWidth();
        int columns = ((MultiLineToolTip)c).getColumns();
        
        if( columns > 0 ) {
            textArea.setColumns(columns);
            textArea.setSize(0,0);
            textArea.setLineWrap(true);
            textArea.setSize( textArea.getPreferredSize() );
        } else if( width > 0 ) {
            textArea.setLineWrap(true);
            Dimension d = textArea.getPreferredSize();
            d.width = width;
            d.height++;
            textArea.setSize(d);
        } else
            textArea.setLineWrap(false);
        
        
        Dimension dim = textArea.getPreferredSize();
        
        dim.height += 1;
        dim.width += 1;
        return dim;
    }
    
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
    
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }
}