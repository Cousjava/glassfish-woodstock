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

package com.sun.webui.jsf.component.propertyeditors;

import com.sun.rave.propertyeditors.SelectOneDomainEditor;
import com.sun.rave.propertyeditors.domains.Domain;
import com.sun.rave.propertyeditors.domains.Element;
import com.sun.webui.jsf.component.util.DesignMessageUtil;
import com.sun.webui.jsf.component.ProgressBar;

public class ProgressBarTypesEditor extends SelectOneDomainEditor {
    
    public ProgressBarTypesEditor() {
        super(new ProgressBarTypesDomain());
    }
    
    static class ProgressBarTypesDomain extends Domain {
        
        private static Element[] elements = new Element[] {
            new Element(ProgressBar.DETERMINATE, 
                    DesignMessageUtil.getMessage(ProgressBarTypesDomain.class, "ProgressBar.determinate")), //NOI18N
            new Element(ProgressBar.INDETERMINATE, 
                    DesignMessageUtil.getMessage(ProgressBarTypesDomain.class, "ProgressBar.indeterminate")), //NOI18N
            new Element(ProgressBar.BUSY_INDICATOR, 
                    DesignMessageUtil.getMessage(ProgressBarTypesDomain.class, "ProgressBar.busy")) //NOI18N
        };
        
        public Element[] getElements() {
            return ProgressBarTypesDomain.elements;
        }
        
    }
}
