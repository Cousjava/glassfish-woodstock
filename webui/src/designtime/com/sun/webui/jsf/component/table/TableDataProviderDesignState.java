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

/*
 * TableDataProviderDesignState.java
 * Created on May 4, 2005, 12:56 PM
 * Version 1.0
 */

package com.sun.webui.jsf.component.table;

import com.sun.data.provider.FieldKey;
import com.sun.data.provider.TableDataProvider;
import com.sun.rave.designtime.DesignBean;
import com.sun.webui.jsf.component.Checkbox;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultListModel;
import org.openide.ErrorManager;

/**
 * Data structure to hold design state of a data provider
 * @author Winston Prakash
 */
public class TableDataProviderDesignState {
    
    boolean dataProviderBroken = false;
    private static int newColumnNameCount = 0;
    
    private DesignBean dataProviderBean;
    
    private DefaultListModel selectedColumnListModel = new DefaultListModel();
    private DefaultListModel availableColumnListModel = new DefaultListModel();
    
    Map columnsDesignStates = null;
    
    /** Creates a new instance of TableDataProviderDesignState */
    public TableDataProviderDesignState(DesignBean modelBean) {
        if(!(modelBean.getInstance()  instanceof TableDataProvider)){
            throw new IllegalArgumentException(dataProviderBean.getInstanceName() + " not a table data provider.");
        }
        dataProviderBean = modelBean;
        // Check if this is a broken DP
        try{
            ((TableDataProvider) dataProviderBean.getInstance()).getFieldKeys();
        }catch (Exception exc){
            ErrorManager.getDefault().notify(exc);
            dataProviderBroken = true;
        }
    }
    
    public boolean isBroken(){
        return dataProviderBroken;
    }
    
    /**
     * Get the Data Model Bean
     */
    public DesignBean getDataProviderBean(){
        return dataProviderBean;
    }
    
    public String getUniqueColumnName(String baseName){
        int colNameCount = selectedColumnListModel.size() + 1;
        boolean found = false;
        String  newName =  baseName + colNameCount; //NOI18N
        do  {
            if(selectedColumnListModel.contains(newName)){
                found = true;
            }else if(availableColumnListModel.contains(newName)){
                found = true;
            }else{
                found = false;
            }
            if(found){
                newName =  baseName + colNameCount++; //NOI18N
            }
        }while(found);
        return newName;
    }
    
    /**
     * Set the available column vector
     */
    public void setSelectedColumnListModel(DefaultListModel listModel){
        selectedColumnListModel =  listModel;
    }
    
    /**
     * Get the Selected table column information
     */
    public DefaultListModel getSelectedColumnListModel(){
        return selectedColumnListModel;
    }
    
    /**
     * Set the available column vector
     */
    public void setAvailableColumnListModel(DefaultListModel listModel){
        availableColumnListModel =  listModel;
    }
    
    /**
     * Get the Selected table column information
     */
    public DefaultListModel getAvailableColumnListModel(){
        return availableColumnListModel;
    }
    
    /**
     * Add the TableColumnDesignState to the selectedColumnsDesignStates design state
     * The name is added to the selected column list model.
     */
    public void addColumnDesignStates(TableColumnDesignState colDesignState){
        if(columnsDesignStates == null) columnsDesignStates = new HashMap();
        columnsDesignStates.put(colDesignState.getName(), colDesignState);
        selectedColumnListModel.addElement(colDesignState.getName());
    }
    
    /*
     * Set the Table Column Design states
     */
    public void setColumnDesignStates(Map colDesignStates){
        columnsDesignStates = colDesignStates;
    }
    
   /*
    * Get the Table Column Design states
    */
    public Map getColumnDesignStates(){
        return columnsDesignStates;
    }
    
    public TableColumnDesignState getTableColumnDesignState(String columnName){
        if(columnsDesignStates != null){
            TableColumnDesignState tableColumnDesignState =  (TableColumnDesignState) columnsDesignStates.get(columnName);
            return tableColumnDesignState;
        }else{
            return null;
        }
    }
    
    /**
     * Remove or add items from Selected Column List and Available Column List
     */
    public void setSelectedColumnNames(Vector selectedColumnNames){
        for(int i=0; i < selectedColumnNames.size(); i++){
            selectedColumnListModel.addElement(selectedColumnNames.get(i));
        }
    }
    
    /**
     * Get the Selected Column
     */
    public Vector getSelectedColumnNames(){
        Vector columnNames = new Vector();
        for (int i=0; i< selectedColumnListModel.size(); i++){
            //String columnName = selectedColumnListModel.getElementAt(i).toString();
            columnNames.add(selectedColumnListModel.getElementAt(i));
        }
        return columnNames;
    }
    
    /**
     * Get the Selected Column
     */
    public Vector getAvailableColumnNames(){
        Vector columnNames = new Vector();
        for (int i=0; i< availableColumnListModel.size(); i++){
            //String columnName = selectedColumnListModel.getElementAt(i).toString();
            columnNames.add(availableColumnListModel.getElementAt(i));
        }
        return columnNames;
    }
    
    /**
     * Populate the Selected Column. All the columns of the data model are
     * selected by defaullt
     */
    public void initialize(){
        if (dataProviderBroken) return;
        TableDataProvider tdp = (TableDataProvider) dataProviderBean.getInstance();
        FieldKey[] columns = tdp.getFieldKeys();
        
        if((columns != null) && (columns.length > 0)){
            if(columnsDesignStates == null){
                columnsDesignStates = new HashMap();
                // Populate the selected column list and create corresponding TableColumnDesignState
                for (int i=0; i< columns.length; i++){
                    //Skip FieldKey of type "Class" - 6309491
                    if((tdp.getType(columns[i]) != null) && tdp.getType(columns[i]).toString().indexOf("java.lang.Class") == -1){
                        String columnName = columns[i].getDisplayName();
                        selectedColumnListModel.addElement(columnName);
                        TableColumnDesignState tableColumnDesignState = new TableColumnDesignState(columnName);
                        tableColumnDesignState.setColumnType(tdp.getType(columns[i]));
                        if(tableColumnDesignState.getColumnType().isAssignableFrom(Boolean.class)){
                            tableColumnDesignState.setChildType(Checkbox.class);
                        }
                        columnsDesignStates.put(columnName, tableColumnDesignState);
                    }
                }
            }else{
                // Populate the available column list and create the corresponding TableColumnDesignState
                for (int i=0; i< columns.length; i++){
                    //Skip FieldKey of type "Class" - 6309491
                    if(tdp.getType(columns[i]).toString().indexOf("java.lang.Class") == -1){
                        String columnName = columns[i].getDisplayName();
                        if(!selectedColumnListModel.contains(columnName)){
                            availableColumnListModel.addElement(columnName);
                            TableColumnDesignState tableColumnDesignState = new TableColumnDesignState(columnName);
                            tableColumnDesignState.setColumnType(tdp.getType(columns[i]));
                            if(tableColumnDesignState.getColumnType().isAssignableFrom(Boolean.class)){
                                tableColumnDesignState.setChildType(Checkbox.class);
                            }
                            columnsDesignStates.put(columnName, tableColumnDesignState);
                        }
                    }
                }
                
            }
        }
        
    }
}
