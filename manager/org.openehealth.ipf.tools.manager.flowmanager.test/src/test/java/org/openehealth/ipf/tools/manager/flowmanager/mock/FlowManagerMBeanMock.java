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
package org.openehealth.ipf.tools.manager.flowmanager.mock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.tools.manager.flowmanager.IFlowManagerMBean;

/**
 * Basic implementation of the IFlowManagerMBean interface.
 * 
 * 
 * @author Mitko Kolev
 */
public class FlowManagerMBeanMock implements IFlowManagerMBean {

    private String maxFlows;

    private String application;

    private boolean isEnableCleaunup;

    private boolean isEnableFiltering;

    public FlowInfo findFlow(long flowId) {
        FlowInfo info = new FlowInfo();
        info.setIdentifier(flowId);
        return info;
    }

    public List<FlowInfo> findLastErrorFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        info.setNakCount(1);
        info.setStatus("ERROR");
        flows.add(info);
        return flows;
    }

    public List<FlowInfo> findLastFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        flows.add(info);
        return flows;
    }

    public List<FlowInfo> findLastUnackFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        info.setAckCountExpected(2);
        info.setAckCount(1);
        info.setStatus("CLEAN");
        flows.add(info);
        return flows;
    }

    public String getApplication() {
        return application;
    }

    public String getMaxFlows() {
        return maxFlows;
    }

    public String getUpperTimeLimit() {
        return null;
    }

    public boolean isEnableCleanup() {
        return isEnableCleaunup;
    }

    public boolean isEnableFiltering() {
        return this.isEnableFiltering;
    }

    public void replayFlow(long flowId) {

    }

    public int replayLastErrorFlows(String last) {
        return 0;
    }

    public int replayLastFlows(String last) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int replayLastUnackFlows(String last) {
        return 0;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setEnableCleanup(boolean enableCleanup) {
        this.isEnableCleaunup = enableCleanup;

    }

    public void setEnableFiltering(boolean enableFiltering) {
        this.isEnableFiltering = enableFiltering;

    }

    public void setMaxFlows(String maxFlows) {
        try {
            this.maxFlows = maxFlows;
            new Integer(maxFlows);
        } catch (NumberFormatException e) {
            throw e;
        }

    }

    public void setUpperTimeLimit(String upperTimeLimit) throws ParseException {
    }

    public void setUpperTimeLimitToCurrentTime() {
    }

    public String findFlowMessageText(long flowId) {
        return null;
    }

    public String findFlowPartMessageText(long flowId, String flowPartInfoPath) {
        return null;
    }

    public List<FlowInfo> findLastErrorFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastErrorFlows(last);
    }

    public List<FlowInfo> findLastFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastFlows(last);
    }

    public List<FlowInfo> findLastUnackFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastUnackFlows(last);
    }

}
