/*
 * Copyright (c) 2007, 2019 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.annotation.Component;
import com.sun.faces.annotation.Property;
import jakarta.el.ValueExpression;
import jakarta.faces.component.NamingContainer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.PhaseId;

/**
 * The Breadcrumbs component is used to display a list of hyperlinks, as an aid
 * to navigation.
 *
 * <p>
 * Breadcrumbs show the user's location within an application, and the physical
 * or logical path to the current page. The user can click hyperlinks in the
 * breadcrumbs to navigate to other, previously visited pages in the
 * application.
 *
 * <p>
 * A breadcrumb's hyperlinks may be specified in one of two ways:
 * <ul>
 * <li>Directly, as children of the breadcrumbs component.
 * <li>Indirectly via the {@code pages} attribute. The value must a value
 * binding expression that identifies an array of
 * {@code com.sun.webui.jsf.Hyperlink} components. Hyperlinks specified in this
 * manner are referred to as "bound" hyperlinks. Bound hyperlinks must be
 * uniquely identifiable by the value of their {@code id} attribute, and this
 * attribute must not be null. Bound hyperlinks should not be part of a view
 * tree, and the value of their {@code parent} property should be null.
 * </ul>
 * You should provide either child hyperlinks or bound hyperlinks, but not both.
 * If both are provided, bound hyperlinks are rendered, and any child hyperlinks
 * are ignored. Non-hyperlink children are also ignored.
 *
 * <p>
 * Bound hyperlinks are treated as child hyperlinks for all phases of request
 * processing, except that they are not asked to save or restore their state.
 *
 * <p>
 * The breadcrumbs component has an {@code immediate} property, the default
 * value of which is {@code true}. If the breadcrumbs is immediate, all action
 * events generated by child or bound hyperlinks will be treated as though they
 * too were immediate, whether or not the source hyperlink is immediate. If the
 * breadcrumbs is not immediate, action events will be processed according to
 * whether their source hyperlink is immediate or not.
 */
@Component(type = "com.sun.webui.jsf.Breadcrumbs",
        family = "com.sun.webui.jsf.Breadcrumbs",
        displayName = "Breadcrumbs",
        tagName = "breadcrumbs",
        helpKey = "projrave_ui_elements_palette_wdstk-jsf1.2_breadcrumbs",
        //CHECKSTYLE:OFF
        propertiesHelpKey = "projrave_ui_elements_palette_wdstk-jsf1.2_propsheets_breadcrumbs_props")
        //CHECKSTYLE:ON
public final class Breadcrumbs extends UIComponentBase
        implements NamingContainer {

    /**
     * An array of zero or more Hyperlink components that constitute the current
     * path. If this property is not null, any child Hyperlinks will be ignored.
     * The hyperlinks must have non-null, unique {@code id}s.
     */
    @Property(name = "pages",
            displayName = "Pages",
            category = "Behavior",
            //CHECKSTYLE:OFF
            editorClassName = "com.sun.rave.propertyeditors.binding.ValueBindingPropertyEditor")
            //CHECKSTYLE:ON
    private Hyperlink[] pages = null;

    /**
     * CSS style(s) to be applied to the outermost HTML element when this
     * component is rendered.
     */
    @Property(name = "style",
            displayName = "CSS Style(s)",
            category = "Appearance",
            editorClassName = "com.sun.jsfcl.std.css.CssStylePropertyEditor")
    private String style = null;

    /**
     * CSS style class(es) to be applied to the outermost HTML element when this
     * component is rendered.
     */
    @Property(name = "styleClass",
            displayName = "CSS Style Class(es)",
            category = "Appearance",
            //CHECKSTYLE:OFF
            editorClassName = "com.sun.rave.propertyeditors.StyleClassPropertyEditor")
            //CHECKSTYLE:ON
    private String styleClass = null;

    /**
     * Position of this element in the tabbing order of the current document.
     * Tabbing order determines the sequence in which elements receive focus
     * when the tab key is pressed. The value must be an integer between 0 and
     * 32767.
     */
    @Property(name = "tabIndex",
            displayName = "Tab Index",
            category = "Accessibility")
    private int tabIndex = Integer.MIN_VALUE;

    /**
     * tabIndex set flag.
     */
    private boolean tabIndexSet = false;

    /**
     * Indicates whether actions should be handled immediately when generated by
     * hyperlinks that are part of this component. The default value is true.
     */
    @Property(displayName = "Immediate",
            category = "Advanced")
    private boolean immediate = true;

    /**
     * immediate set flag.
     */
    private boolean immediateSet = false;

    /**
     * Indicates whether the component and its child components should be
     * viewable by the user in the rendered HTML page.
     */
    @Property(name = "visible",
            displayName = "Visible",
            category = "Behavior")
    private boolean visible = false;

    /**
     * visible set flag.
     */
    private boolean visibleSet = false;

    /**
     * Default constructor.
     */
    public Breadcrumbs() {
        super();
        setRendererType("com.sun.webui.jsf.Breadcrumbs");
    }

    @Override
    public String getFamily() {
        return "com.sun.webui.jsf.Breadcrumbs";
    }

    /**
     * The component identifier for this component. This value must be unique
     * within the closest parent component that is a naming container.
     * @param id id
     */
    @Property(name = "id")
    @Override
    public void setId(final String id) {
        super.setId(id);
    }

    /**
     * Use the rendered attribute to indicate whether the HTML code for the
     * component should be included in the rendered HTML page. If set to false,
     * the rendered HTML page does not include the HTML for the component. If
     * the component is not rendered, it is also not processed on any subsequent
     * form submission.
     * @param rendered rendered
     */
    @Property(name = "rendered")
    @Override
    public void setRendered(final boolean rendered) {
        super.setRendered(rendered);
    }

    /**
     * Get the array of zero or more Hyperlink components that constitute the
     * current path.
     *
     * @return links
     */
    public Hyperlink[] getPages() {
        if (this.pages != null) {
            return this.pages;
        }
        ValueExpression vb = getValueExpression("pages");
        if (vb != null) {
            return (Hyperlink[]) vb.getValue(getFacesContext().getELContext());
        }
        return null;
    }

    /**
     * Set the array of zero or more Hyperlink components that constitute the
     * current path.
     *
     * @param newPages links
     */
    public void setPages(final Hyperlink[] newPages) {
        this.pages = newPages;
    }

    /**
     * Returns the CSS style(s) to be applied to the outermost HTML element when
     * this component is rendered.
     *
     * @return String
     * @see #setStyle
     */
    public String getStyle() {
        if (this.style != null) {
            return this.style;
        }
        ValueExpression vb = getValueExpression("style");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext().getELContext());
        }
        return null;
    }

    /**
     * Set the CSS style(s) to be applied to the outermost HTML element when
     * this component is rendered.
     *
     * @param newStyle new value
     * @see #getStyle
     */
    public void setStyle(final String newStyle) {
        this.style = newStyle;
    }

    /**
     * Get the CSS style class(es) to be applied to the outermost HTML element
     * when this component is rendered.
     *
     * @return String
     * @see #setStyleClass
     */
    public String getStyleClass() {
        if (this.styleClass != null) {
            return this.styleClass;
        }
        ValueExpression vb = getValueExpression("styleClass");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext().getELContext());
        }
        return null;
    }

    /**
     * Set the CSS style class(es) to be applied to the outermost HTML element
     * when this component is rendered.
     *
     * @param newStyleClass new value
     * @see #getStyleClass()
     */
    public void setStyleClass(final String newStyleClass) {
        this.styleClass = newStyleClass;
    }

    /**
     * Get the position of this element in the tabbing order of the current
     * document.Tabbing order determines the sequence in which elements receive
     * focus when the tab key is pressed. The value must be an integer between 0
     * and 32767.
     *
     * @return int
     * @see #setTabIndex
     */
    public int getTabIndex() {
        if (this.tabIndexSet) {
            return this.tabIndex;
        }
        ValueExpression vb = getValueExpression("tabIndex");
        if (vb != null) {
            Object result = vb.getValue(getFacesContext().getELContext());
            if (result == null) {
                return Integer.MIN_VALUE;
            } else {
                return ((Integer) result);
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Set the position of this element in the tabbing order of the current
     * document.Tabbing order determines the sequence in which elements receive
     * focus when the tab key is pressed. The value must be an integer between 0
     * and 32767.
     *
     * @param newTabIndex tabIndex
     * @see #getTabIndex()
     */
    public void setTabIndex(final int newTabIndex) {
        this.tabIndex = newTabIndex;
        this.tabIndexSet = true;
    }

    /**
     * Get the immediate flag value.
     * @return {@code boolean}
     */
    public boolean isImmediate() {
        if (this.immediateSet) {
            return this.immediate;
        }
        ValueExpression vb = getValueExpression("immediate");
        if (vb != null) {
            Object result = vb.getValue(getFacesContext().getELContext());
            if (result == null) {
                return false;
            } else {
                return ((Boolean) result);
            }
        }
        return true;
    }

    /**
     * Set the immediate flag value.
     * @param newImmediate immediate
     */
    public void setImmediate(final boolean newImmediate) {
        this.immediate = newImmediate;
        this.immediateSet = true;
    }

    /**
     * Returns true if this component and its child components should be
     * viewable by the user in the rendered HTML page.
     *
     * @return {@code boolean}
     * @see #setVisible
     */
    public boolean isVisible() {
        if (this.visibleSet) {
            return this.visible;
        }
        ValueExpression vb = getValueExpression("visible");
        if (vb != null) {
            Object result = vb.getValue(getFacesContext().getELContext());
            if (result == null) {
                return false;
            } else {
                return ((Boolean) result);
            }
        }
        return true;
    }

    /**
     * Returns true if this component and its child components should be
     * viewable by the user in the rendered HTML page.
     *
     * @param newVisible new value
     * @see #isVisible
     */
    public void setVisible(final boolean newVisible) {
        this.visible = newVisible;
        this.visibleSet = true;
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (this.isRendered()) {
            if (this.getPages() != null) {
                for (Hyperlink hyperlink : this.getPages()) {
                    UIComponent parent = hyperlink.getParent();
                    hyperlink.setParent(this);
                    hyperlink.processDecodes(context);
                    hyperlink.setParent(parent);
                }
            } else {
                super.processDecodes(context);
            }
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (this.isRendered()) {
            if (this.getPages() != null) {
                for (Hyperlink hyperlink : this.getPages()) {
                    UIComponent parent = hyperlink.getParent();
                    hyperlink.setParent(this);
                    hyperlink.processValidators(context);
                    hyperlink.setParent(parent);
                }
            } else {
                super.processValidators(context);
            }
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (this.isRendered()) {
            if (this.getPages() != null) {
                for (Hyperlink hyperlink : this.getPages()) {
                    UIComponent parent = hyperlink.getParent();
                    hyperlink.setParent(this);
                    hyperlink.processUpdates(context);
                    hyperlink.setParent(parent);
                }
            } else {
                super.processUpdates(context);
            }
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (this.isImmediate()) {
            // If this breadcrumbs component is immediate, then treat all
            // action
            // events generated by all children hyperlinks, or all hyperlinks
            // bound
            // to the pages property, as though they were immediate.
            if (event instanceof ActionEvent) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
        }
        super.queueEvent(event);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public void restoreState(final FacesContext context, final Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        if (values[1] != null) {
            Object[] pageValues = (Object[]) values[1];
            this.pages = new Hyperlink[pageValues.length];
            for (int i = 0; i < pageValues.length; i++) {
                this.pages[i] = new Hyperlink();
                this.pages[i].restoreState(context, pageValues[i]);
            }
        }
        this.style = (String) values[2];
        this.styleClass = (String) values[3];
        this.tabIndex = ((Integer) values[4]);
        this.tabIndexSet = ((Boolean) values[5]);
        this.immediate = ((Boolean) values[6]);
        this.immediateSet = ((Boolean) values[7]);
        this.visible = ((Boolean) values[8]);
        this.visibleSet = ((Boolean) values[9]);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public Object saveState(final FacesContext context) {
        Object[] values = new Object[10];
        values[0] = super.saveState(context);
        if (this.pages != null) {
            Object[] pageValues = new Object[this.pages.length];
            for (int i = 0; i < this.pages.length; i++) {
                pageValues[i] = this.pages[i].saveState(context);
            }
            values[1] = pageValues;
        }
        values[2] = this.style;
        values[3] = this.styleClass;
        values[4] = this.tabIndex;
        if (this.tabIndexSet) {
            values[5] = Boolean.TRUE;
        } else {
            values[5] = Boolean.FALSE;
        }
        if (this.immediate) {
            values[6] = Boolean.TRUE;
        } else {
            values[6] = Boolean.FALSE;
        }
        if (this.immediateSet) {
            values[7] = Boolean.TRUE;
        } else {
            values[7] = Boolean.FALSE;
        }
        if (this.visible) {
            values[8] = Boolean.TRUE;
        } else {
            values[8] = Boolean.FALSE;
        }
        if (this.visibleSet) {
            values[9] = Boolean.TRUE;
        } else {
            values[9] = Boolean.FALSE;
        }
        return values;
    }
}
