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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;
import org.openehealth.ipf.tools.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.tools.manager.flowmanager.IFlowPartInfo;

/**
 * Provides implementation of IFlowIfo. The implementation provides uses an
 * adapter.
 * <p>
 * 
 * @see IFlowInfo
 * 
 * @author Mitko Kolev
 */
public class FlowInfoImpl implements IFlowInfo {

    FlowInfo adaptee;

    public FlowInfoImpl(FlowInfo target) {
        this.adaptee = target;
    }

    public int getAckCount() {
        return adaptee.getAckCount();
    }


    public int getAckCountExpected() {
        return adaptee.getAckCountExpected();
    }

    public String getApplication() {
        return adaptee.getApplication();
    }

    public Date getCreationTime() {
        return adaptee.getCreationTime();
    }

    public Long getIdentifier() {
        return adaptee.getIdentifier();
    }

    public int getNakCount() {
        return adaptee.getNakCount();
    }

    public List<IFlowPartInfo> getPartInfos() {
        ArrayList<IFlowPartInfo> partAdapters = new ArrayList<IFlowPartInfo>();
        for (FlowPartInfo info : adaptee.getPartInfos()) {
            partAdapters.add(new FlowPartInfoImpl(info));
        }
        return partAdapters;
    }

    public int getReplayCount() {
        return adaptee.getReplayCount();
    }

    public Date getReplayTime() {
        return adaptee.getReplayTime();
    }

    public String getStatus() {
        return adaptee.getStatus();
    }

    public boolean isReplayable() {
        return adaptee.isReplayable();
    }

}
