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
package org.openehealth.ipf.tools.manager.flowmanager.impl;

import java.util.Date;

import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;
import org.openehealth.ipf.tools.manager.flowmanager.IFlowPartInfo;

/**
 * 
 * Bean which implements the IFlowPartInfo.
 * <p>
 * 
 * @author Mitko Kolev
 */
public class FlowPartInfoImpl implements IFlowPartInfo {

    private final FlowPartInfo adaptee;

    public FlowPartInfoImpl(FlowPartInfo info) {
        adaptee = info;
    }

    public int getContributionCount() {
        return adaptee.getContributionCount();
    }

    public Date getContributionTime() {
        return adaptee.getContributionTime();
    }

    public int getFilterCount() {
        return adaptee.getFilterCount();
    }

    public Date getFilterTime() {
        return adaptee.getFilterTime();
    }

    public String getPath() {
        return adaptee.getPath();
    }

    public long getPathDuration() {
        return adaptee.getPathDuration();
    }

    public String getStatus() {
        return adaptee.getStatus();
    }
}
