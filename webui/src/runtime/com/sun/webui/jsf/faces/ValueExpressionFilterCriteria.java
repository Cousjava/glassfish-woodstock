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

package com.sun.webui.jsf.faces;

import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import com.sun.data.provider.FilterCriteria;
import com.sun.data.provider.RowKey;
import com.sun.data.provider.TableDataFilter;
import com.sun.data.provider.TableDataProvider;
import com.sun.data.provider.impl.CompareFilterCriteria;
import com.sun.data.provider.impl.TableRowDataProvider;
import com.sun.faces.annotation.Component;
import com.sun.faces.annotation.Property;

/**
 * <p>The ValueExpressionFilterCriteria is an implementation of
 * {@link FilterCriteria} that compares the value of a {@link String}
 * (created with the specified value expression) with a predefined
 * <code>compareValue</code>.  A user may specify matches to include less than
 * (<), equal to (==), or greater than (>) the <code>compareValue</code> Object,
 * or any combination of the above.</p>
 *
 * @see TableDataProvider
 * @see TableDataFilter
 *
 * @author Joe Nuxoll, John Yeary
 */
@Component(isTag = false)
public class ValueExpressionFilterCriteria extends FilterCriteria {

    private static final long serialVersionUID = 8984072154367845774L;

    /**
     *
     */
    public ValueExpressionFilterCriteria() {
    }

    /**
     *
     * @param valueExpression String
     */
    public ValueExpressionFilterCriteria(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    /**
     *
     * @param compareValue The desired compare value
     */
    public ValueExpressionFilterCriteria(Object compareValue) {
        this.compareValue = compareValue;
    }

    /**
     *
     * @param valueExpression String
     * @param compareValue The desired compare value
     */
    public ValueExpressionFilterCriteria(String valueExpression, Object compareValue) {
        this.valueExpression = valueExpression;
        this.compareValue = compareValue;
    }

    /**
     *
     * @param valueExpression String
     * @param compareValue Object
     * @param matchLessThan boolean
     * @param matchEqualTo boolean
     * @param matchGreaterThan boolean
     */
    public ValueExpressionFilterCriteria(String valueExpression, Object compareValue,
            boolean matchLessThan, boolean matchEqualTo, boolean matchGreaterThan) {

        this.valueExpression = valueExpression;
        this.compareValue = compareValue;
        this.matchLessThan = matchLessThan;
        this.matchEqualTo = matchEqualTo;
        this.matchGreaterThan = matchGreaterThan;
    }

    /**
     *
     */
    public String getDisplayName() {
        String name = super.getDisplayName();
        if (name != null && !"".equals(name)) {
            return name;
        }

        // if there's no display name, make one...
        Object val = getCompareValue();
        StringBuffer sb = new StringBuffer();
        sb.append(isInclude() ? "Include [" : "Exclude [");
        sb.append(valueExpression != null ? valueExpression : "<no value expression>");
        sb.append("] ");
        boolean anyMatches = false;
        if (matchLessThan) {
            anyMatches = true;
            sb.append("is less than ");
        }
        if (matchEqualTo) {
            if (anyMatches) {
                sb.append("OR ");
            }
            anyMatches = true;
            sb.append("is equal to ");
        }
        if (matchGreaterThan) {
            if (anyMatches) {
                sb.append("OR ");
            }
            sb.append("is greater than ");
        }
        sb.append("[" + val + "]");
        return sb.toString();
    }

    /**
     *
     * @param valueExpression String
     */
    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    /**
     *
     * @return String
     */
    public String getValueExpression() {
        return valueExpression;
    }

    /**
     * Returns the request map variable key that will be used to store the
     * {@link TableRowDataProvider} for the current row being match tested.
     * This allows value expressions to refer to the "current" row during the
     * filter operation.
     *
     * @return String key to use for the {@link TableRowDataProvider}
     */
    public String getRequestMapKey() {
        return requestMapKey;
    }

    /**
     * Sets the request map variable key that will be used to store the
     * {@link TableRowDataProvider} for the current row being match tested.
     * This allows value expressions to refer to the "current" row during the
     * filter operation.
     *
     * @param requestMapKey String key to use for the {@link TableRowDataProvider}
     */
    public void setRequestMapKey(String requestMapKey) {
        this.requestMapKey = requestMapKey;
    }

    /**
     *
     * @param value Object
     */
    public void setCompareValue(Object value) {
        this.compareValue = value;
    }

    /**
     *
     * @return Object
     */
    public Object getCompareValue() {
        return compareValue;
    }
    /**
     * Storage for the compare locale
     */
    protected Locale compareLocale;

    /**
     *
     * @param compareLocale Locale
     */
    public void setCompareLocale(Locale compareLocale) {
        this.compareLocale = compareLocale;
    }

    /**
     *
     * @return Locale
     */
    public Locale getCompareLocale() {
        return compareLocale;
    }
    /**
     *
     */
    protected boolean matchEqualTo = true;

    /**
     *
     * @param matchEqualTo boolean
     */
    public void setMatchEqualTo(boolean matchEqualTo) {
        this.matchEqualTo = matchEqualTo;
    }

    /**
     *
     * @return boolean
     */
    public boolean isMatchEqualTo() {
        return matchEqualTo;
    }
    /**
     *
     */
    protected boolean matchLessThan = false;

    /**
     *
     * @param matchLessThan boolean
     */
    public void setMatchLessThan(boolean matchLessThan) {
        this.matchLessThan = matchLessThan;
    }

    /**
     *
     * @return boolean
     */
    public boolean isMatchLessThan() {
        return matchLessThan;
    }
    /**
     *
     */
    protected boolean matchGreaterThan = false;

    /**
     *
     * @param matchGreaterThan boolean
     */
    public void setMatchGreaterThan(boolean matchGreaterThan) {
        this.matchGreaterThan = matchGreaterThan;
    }

    /**
     *
     * @return boolean
     */
    public boolean isMatchGreaterThan() {
        return matchGreaterThan;
    }

    /**
     * <p>This method tests a match by comparing the <code>compareValue</code>
     * and the data value stored under the {@link String}.  The passed
     * TableDataProvider and RowKey parameters are ignored.  The
     * <code>matchLessThan</code>,  <code>matchEqualTo</code>, and
     * <code>matchGreaterThan</code> properties  are used to determine if a
     * match was found.  The <code>compareLocale</code> is used for String
     * comparisons.</p>
     *
     * {@inheritDoc}
     */
    public boolean match(TableDataProvider provider, RowKey row) {
        if (valueExpression == null || "".equals(valueExpression)) {
            return true;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueExpression valueBinding = facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), valueExpression, Object.class);

        if (valueBinding == null) {
            return true;
        }

        Map requestMap = facesContext.getExternalContext().getRequestMap();
        Object value = null;

        //FIXME synchronization on a non-final field
        synchronized (rowProviderLock) {

            Object storedRequestMapValue = null;
            if (requestMapKey != null && !"".equals(requestMapKey)) {
                storedRequestMapValue = requestMap.get(requestMapKey);
                if (rowProvider == null) {
                    rowProvider = new TableRowDataProvider();
                }
                rowProvider.setTableDataProvider(provider);
                rowProvider.setTableRow(row);
                requestMap.put(requestMapKey, rowProvider);
            }

            value = valueBinding.getValue(facesContext.getELContext());

            if (requestMapKey != null && !"".equals(requestMapKey)) {
                if (rowProvider != null) {
                    rowProvider.setTableDataProvider(null);
                    rowProvider.setTableRow(null);
                }
                requestMap.put(requestMapKey, storedRequestMapValue);
            }
        }

        int compare = CompareFilterCriteria.compare(value, compareValue, compareLocale);
        switch (compare) {
            case -1: // Less Than
                return matchLessThan;
            case 0: // Equal To
                return matchEqualTo;
            case 1: // Greater Than
                return matchGreaterThan;
        }
        return false; // This should never be reached
    }
    @Property(displayName = "Value Expression")
    private String valueExpression;
    @Property(displayName = "Compare Value")
    private Object compareValue;
    @Property(displayName = "Request Map Key")
    private String requestMapKey = "currentRow";
    private transient TableRowDataProvider rowProvider;
    private String rowProviderLock = "rowProviderLock"; // this is a monitor lock for rowProvider
}
