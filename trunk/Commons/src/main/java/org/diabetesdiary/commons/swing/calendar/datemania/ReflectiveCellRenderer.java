package org.diabetesdiary.commons.swing.calendar.datemania;
import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class ReflectiveCellRenderer extends DefaultListCellRenderer
{
  public ReflectiveCellRenderer(Class objectClass, String methodName)
  {
    try
    {
      _method = objectClass.getMethod(methodName, (Class[])null);
    } catch (Exception e)
    {
    }
  }
  
  public ReflectiveCellRenderer(Class objectClass, String methodName, Class[] paramClasses, Object[] params)
  {
    try
    {
      _method = objectClass.getMethod(methodName, paramClasses);
      _parameters = params;
    } catch (Exception e)
    {
    }
  }
  
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected,
        cellHasFocus);
    
    try
    {
      if (_method != null)
      {
        Object ret = _method.invoke(value, _parameters);
        if (ret != null)
          label.setText(ret.toString());
      }
    } catch (Exception e)
    {
    }
    
    return label;
  }
  
  private Method _method;
  private Object[] _parameters;
}
