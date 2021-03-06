/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.webui.jsf.component;

import com.sun.data.provider.TableDataProvider;
import com.sun.rave.designtime.DesignContext;
import com.sun.rave.designtime.DesignBean;
import com.sun.rave.designtime.DesignProperty;
import com.sun.rave.designtime.Result;
import com.sun.webui.jsf.component.table.TableDesignHelper;
import com.sun.webui.jsf.component.util.DesignMessageUtil;
import com.sun.webui.jsf.design.AbstractDesignInfo;
import java.sql.ResultSet;

/**
 * DesignInfo for the <code>TableColumn</code> component. The following behavior is
 * implemented:
 * <ul>
 * <li>Upon component creation, pre-populate with one Static Text.</li>
 * </ul>
 *
 * @author Winston Prakash
 */
public class TableColumnDesignInfo extends AbstractDesignInfo {
    
    private static final String WIDTH_PROPERTY = "width";
    
    public TableColumnDesignInfo() {
        super(TableColumn.class);
    }
    
    public Result beanCreatedSetup(DesignBean tableColumnBean) {
        int colNo = tableColumnBean.getBeanParent().getChildBeanCount();
        String columnHeaderText = DesignMessageUtil.getMessage(TableColumnDesignInfo.class,"tableColumn.headerText", new Object[] {String.valueOf(colNo)});
        DesignProperty headerTextProperty = tableColumnBean.getProperty("headerText"); //NOI18N
        headerTextProperty.setValue(columnHeaderText);
        DesignProperty widthProperty = tableColumnBean.getProperty("width"); //NOI18N
        widthProperty.setValue(String.valueOf(200));
        DesignContext context = tableColumnBean.getDesignContext();
        if (context.canCreateBean(StaticText.class.getName(), tableColumnBean, null)) {
            DesignBean staticTextBean = context.createBean(StaticText.class.getName(), tableColumnBean, null);
            DesignProperty textProperty = staticTextBean.getProperty("text"); //NOI18N
            textProperty.setValue(staticTextBean.getBeanInfo().getBeanDescriptor().getDisplayName());
        }
        return Result.SUCCESS;
    }
    
    /** {@inheritDoc} */
    public Result beanDeletedCleanup(DesignBean bean) {
        // Adjust table width if table column width is et in pixels
        int oldColumnWidth = -1;
        Object oldValue = bean.getProperty(WIDTH_PROPERTY).getValue();
        if(oldValue != null){
            String oldColumnWidthStr = (String)oldValue;
            if (oldColumnWidthStr.indexOf("%") == -1) { //NOI18N
                try{
                    oldColumnWidth = Integer.parseInt(oldColumnWidthStr);
                }catch(Exception exc){
                }
            }
        }
        TableDesignHelper.adjustTableWidth(bean.getBeanParent().getBeanParent(), oldColumnWidth, 0);
        return Result.SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     * Accept only StaticText, Button or Field as Child
     */
    public boolean acceptChild(DesignBean parentBean, DesignBean childBean, Class childClass) {
        if(childClass.isAssignableFrom(StaticText.class) ||
                //childClass.isAssignableFrom(TableColumn.class) ||
                childClass.isAssignableFrom(Button.class) ||
                childClass.isAssignableFrom(TextField.class) ||
                childClass.isAssignableFrom(TextArea.class) ||
                childClass.isAssignableFrom(StaticText.class) ||
                childClass.isAssignableFrom(Label.class) ||
                childClass.isAssignableFrom(DropDown.class) ||
                childClass.isAssignableFrom(Hyperlink.class) ||
                childClass.isAssignableFrom(ImageHyperlink.class) ||
                childClass.isAssignableFrom(Checkbox.class) ||
                childClass.isAssignableFrom(RadioButton.class) ||
                childClass.isAssignableFrom(ImageComponent.class) ||
                //childClass.isAssignableFrom(RadioButtonGroup.class) ||
                //childClass.isAssignableFrom(CheckboxGroup.class) ||
                childClass.isAssignableFrom(PanelGroup.class) ||
                childClass.isAssignableFrom(Message.class)){
            return true;
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     * Accept only TableRowGroup as Parent
     */
    public boolean acceptParent(DesignBean parentBean, DesignBean childBean, Class parentClass) {
        return parentBean.getInstance().getClass().isAssignableFrom(TableRowGroup.class);
    }
    
    /**
     * Accept only Reult Set (may be not required in future) or  TableDataProvider as links
     *
     * {@inheritDoc}
     */
    public boolean acceptLink(DesignBean targetBean, DesignBean sourceBean, Class sourceClass) {
        return false;
    }
    
    /**
     * TBD - remove the earlier child and add the source bean as child
     *
     * {@inheritDoc}
     */
    
    public Result linkBeans(DesignBean targetBean, DesignBean sourceBean) {
        System.out.println(targetBean);
        System.out.println(sourceBean);
        return Result.SUCCESS;
    }
    
    /**
     * Modify the width of the table if the column width changes
     */
    public void propertyChanged(DesignProperty property, Object oldValue) {
        String propertyName = property.getPropertyDescriptor().getName();
        if(propertyName.equals(WIDTH_PROPERTY)){
            String columnWidth = (String)property.getValue();
            if(columnWidth != null){
                // If not a percentage, units are in pixels.
                // Ajust the table width only if the column width is specified in pixles
                if (columnWidth.indexOf("%") == -1){
                    int oldColumnWidth = -1;
                    if(oldValue != null){
                        String oldColumnWidthStr = (String)oldValue;
                        if (oldColumnWidthStr.indexOf("%") == -1) { //NOI18N
                            try{
                                oldColumnWidth = Integer.parseInt(oldColumnWidthStr);
                            }catch(Exception exc){
                            }
                        }
                    }
                    int newColumnWidth = -1;
                    try{
                        newColumnWidth = Integer.parseInt(columnWidth);
                    }catch(Exception exc){
                    }
                    //System.out.println("Adjusting Width of column - " + columnName);
                    // Adjust the table width to accomodate the change in width of the table column
                    TableDesignHelper.adjustTableWidth(property.getDesignBean().getBeanParent().getBeanParent(), oldColumnWidth, newColumnWidth);
                }
            }
        }
    }
}
