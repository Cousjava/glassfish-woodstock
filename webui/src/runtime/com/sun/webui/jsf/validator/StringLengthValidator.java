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
 * StringLengthValidator.java
 *
 * Created on February 11, 2005, 9:58 AM
 */
package com.sun.webui.jsf.validator;

import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import com.sun.webui.theme.Theme;
import com.sun.webui.jsf.util.ThemeUtilities;

/**
 *  <p>	Use this validator to check the number of characters in a string when
 *	you need to set the validation messages.</p>
 *
 * @author avk
 */
public class StringLengthValidator implements Validator, StateHolder {

    /**
     * <p>The converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "com.sun.webui.jsf.StringLength";
    /**
     * The message to use in case the value is too short. May include 
     * {0} for the minimum value.
     */
    private String tooShortMessage = null;
    /**
     * The message to use in case the value is too long. May include
     * {0} for the maximum value.
     */
    private String tooLongMessage;
    private int minLength = 0;
    private int maxLength = 0;
    private boolean minimumSet = false;
    private static final boolean DEBUG = false;

    /** Creates a new instance of StringLengthValidator */
    public StringLengthValidator() {
    }

    /**
     * Creates a new instance of StringLengthValidator.
     * @param max The maximum number of characters allowed in the string
     */
    public StringLengthValidator(int max) {
        maxLength = max;
    }

    /**
     * Creates a new instance of StringLengthValidator.
     * @param max The maximum number of characters allowed in the string
     * @param min The minimum number of characters allowed in the string
     */
    public StringLengthValidator(int max, int min) {
        this(max);
        minLength = min;
        minimumSet = true;
    }

    /**
     *	<p> Validate the value with regard to a <code>UIComponent</code> and a
     *	    <code>FacesContext</code>.</p>
     *
     *	@param	context	    The FacesContext
     *	@param	component   The component to be validated
     *	@param	value	    The submitted value of the component
     *
     * @exception ValidatorException if the value is not valid
     */
    public void validate(FacesContext context,
            UIComponent component,
            Object value) throws ValidatorException {

        if (DEBUG) {
            log("validate(" + String.valueOf(value) + ")");
        }

        if ((context == null) || (component == null)) {
            if (DEBUG) {
                log("\tContext or component is null");
            }
            throw new NullPointerException();
        }

        String string;
        if (value == null) {
            if (DEBUG) {
                log("\tValue is null!");
            }
            string = new String();
        } else {
            string = (String) value;

        }
        if (DEBUG) {
            log("\tValue is !" + string + "!");
        }
        if (string.length() > maxLength) {
            if (DEBUG) {
                log("\tString is longer than maxlength");
            }
            if (tooLongMessage == null) {
                Theme theme = ThemeUtilities.getTheme(context);
                tooLongMessage = theme.getMessage("StringLengthValidator.itemTooLong");
            }
            MessageFormat mf =
                    new MessageFormat(tooLongMessage,
                    context.getViewRoot().getLocale());
            Object[] params = {String.valueOf(maxLength)};
            FacesMessage msg = new FacesMessage(mf.format(params));
            throw new ValidatorException(msg);

        }
        if (minimumSet && string.length() < minLength) {
            if (DEBUG) {
                log("\tString is shorter than minlength");
            }
            if (tooShortMessage == null) {
                Theme theme = ThemeUtilities.getTheme(context);
                tooShortMessage = theme.getMessage("StringLengthValidator.itemTooLong");
            }
            MessageFormat mf =
                    new MessageFormat(tooShortMessage,
                    context.getViewRoot().getLocale());
            Object[] params = {String.valueOf(minLength)};
            FacesMessage msg = new FacesMessage(mf.format(params));
            throw new ValidatorException(msg);
        }
    }

    private String integerToString(UIComponent component, Integer toConvert) {
        String result = null;
        Converter converter = null;
        FacesContext context = FacesContext.getCurrentInstance();

        converter = (Converter) context.getApplication().createConverter("javax.faces.Number");
        result = converter.getAsString(context, component, toConvert);
        return result;
    }

    /**
     * Saves the state of the component into an object
     * @param context the FacesContext
     * @return the Object representing the state of the component
     */
    public Object saveState(FacesContext context) {

        Object values[] = new Object[5];
        values[0] = new Integer(maxLength);
        values[1] = new Integer(minLength);
        values[2] = minimumSet ? Boolean.TRUE : Boolean.FALSE;
        values[3] = tooLongMessage;
        values[4] = tooShortMessage;
        return (values);
    }

    /**
     * Restore the state of the component.
     * @param context The FacesContext
     * @param state the Object representing the state of the component
     */
    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        maxLength = ((Integer) values[0]).intValue();
        minLength = ((Integer) values[1]).intValue();
        minimumSet = ((Boolean) values[2]).booleanValue();
        if (values[3] != null) {
            tooLongMessage = values[3].toString();
        }
        if (values[4] != null) {
            tooShortMessage = values[4].toString();
        }
    }
    private boolean transientValue = false;

    /**
     * Returns false, this component needs to save state.
     * @return false
     */
    public boolean isTransient() {
        return false;
    }

    /**
     * Does nothing
     */
    public void setTransient(boolean transientValue) {
        return;
    }

    /**
     * Get the message to be used if the string is longer than the maxmimum number of characters.
     * @return the message to be used if the string is longer than the maxmimum number of characters
     */
    public String getTooLongMessage() {

        return this.tooLongMessage;
    }

    /**
     * Set the message to be used if the string is longer than the maximum number of characters.
     * @param tooLongMessage the message to be used if the string is longer than the maxmimum number of characters
     */
    public void setTooLongMessage(String tooLongMessage) {

        this.tooLongMessage = tooLongMessage;
    }

    /**
     * Get the message to be used if the string is shorter than the minimum number of characters.
     * @return the message to be used if the string is shorter than the minimum number of characters
     */
    public String getTooShortMessage() {

        return this.tooShortMessage;
    }

    /**
     * Set the message to be used if the string is shorter than the minimum number of characters.
     * @param tooShortMessage the message to be used if the string is shorter than the minimum number of characters
     */
    public void setTooShortMessage(String tooShortMessage) {

        this.tooShortMessage = tooShortMessage;
    }

    private void log(String s) {
        System.out.println(this.getClass().getName() + "::" + s); //NOI18N
    }
}
