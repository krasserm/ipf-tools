/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tools.manager.jmxexplorer.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.tools.manager.connection.ui.tree.Node;
import org.openehealth.ipf.tools.manager.jmxexplorer.ui.properties.MBeanAttributePropertySource;
import org.openehealth.ipf.tools.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.tools.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.tools.manager.jmxexplorer.ui.tree.MBeanOperationNode;

/**
 * Defines what is to be filtered and how, when text in the filter field exists.
 * The filter fields are set in the properties.
 * {@link MBeanAttributePropertySource}
 * 
 * @see MBeanAttributePropertySource
 * @see PropertyDescriptor#setFilterFlags(String[])
 * 
 * @author Mitko Kolev
 */
public class TreeFilter extends PatternFilter {

    public TreeFilter() {
        this.setIncludeLeadingWildcard(true);
    }

    /**
     * @see org.eclipse.ui.dialogs.PatternFilter#isElementVisible(org.eclipse.jface.viewers.Viewer,
     *      java.lang.Object)
     */
    @Override
    public boolean isElementVisible(Viewer viewer, Object element) {
        if (element instanceof Node) {
            return matches((Node) element);
        } // should never be entered!
        return false;
    }

    private boolean matchesProperties(Node node) {
        IPropertySource ps = (IPropertySource) node
                .getAdapter(IPropertySource.class);
        IPropertyDescriptor[] descriptors = ps.getPropertyDescriptors();
        for (int t = 0; t < descriptors.length; t++) {
            IPropertyDescriptor descriptor = descriptors[t];
            String[] filterFlags = descriptor.getFilterFlags();
            if (filterFlags != null) {
                for (int k = 0; k < filterFlags.length; k++) {
                    if (wordMatches(filterFlags[k])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @see org.eclipse.ui.dialogs.PatternFilter#setPattern(java.lang.String)
     */
    @Override
    public void setPattern(String patternString) {
        super.setPattern(patternString);
    }

    private boolean matches(Node on) {
        if (on instanceof MBeanAttributeNode
                || on instanceof MBeanOperationNode) {
            if (matchesProperties(on)) {
                return true;
            }
        } else if (on instanceof MBeanNode) {
            MBeanNode node = (MBeanNode) on;
            if (wordMatches(node.getObjectName().toString())) {
                return true;
            }
        }
        boolean hasMatchingChildren = false;
        Node[] children = on.getChildren();
        for (int i = 0; i < children.length; i++) {
            Node child = children[i];
            hasMatchingChildren |= matches(child);
        }
        return hasMatchingChildren;
    }
}
