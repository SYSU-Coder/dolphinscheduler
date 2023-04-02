/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.server.master.processor.queue;

import org.apache.dolphinscheduler.common.enums.TaskEventType;
import org.apache.dolphinscheduler.common.utils.DateUtils;
import org.apache.dolphinscheduler.plugin.task.api.enums.TaskExecutionStatus;
import org.apache.dolphinscheduler.remote.command.task.TaskExecuteResultMessage;
import org.apache.dolphinscheduler.remote.command.task.TaskExecuteRunningMessage;
import org.apache.dolphinscheduler.remote.command.task.TaskRejectMessage;
import org.apache.dolphinscheduler.remote.command.task.TaskUpdatePidMessage;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.netty.channel.Channel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {

    /**
     * taskInstanceId
     */
    private int taskInstanceId;

    /**
     * worker address
     */
    private String workerAddress;

    /**
     * state
     */
    private TaskExecutionStatus state;

    /**
     * start time
     */
    private Date startTime;

    /**
     * end time
     */
    private Date endTime;

    /**
     * execute path
     */
    private String executePath;

    /**
     * log path
     */
    private String logPath;

    /**
     * processId
     */
    private int processId;

    /**
     * appIds
     */
    private String appIds;

    /**
     * ack / response
     */
    private TaskEventType event;

    /**
     * varPool
     */
    private String varPool;

    private int cacheTaskInstanceId;

    /**
     * channel
     */
    private Channel channel;

    private int processInstanceId;

    public static TaskEvent newDispatchEvent(int processInstanceId, int taskInstanceId, String workerAddress) {
        TaskEvent event = new TaskEvent();
        event.setProcessInstanceId(processInstanceId);
        event.setTaskInstanceId(taskInstanceId);
        event.setWorkerAddress(workerAddress);
        event.setEvent(TaskEventType.DISPATCH);
        return event;
    }

    public static TaskEvent newRunningEvent(TaskExecuteRunningMessage command, Channel channel, String workerAddress) {
        TaskEvent event = new TaskEvent();
        event.setProcessInstanceId(command.getProcessInstanceId());
        event.setTaskInstanceId(command.getTaskInstanceId());
        event.setState(command.getStatus());
        event.setStartTime(DateUtils.timeStampToDate(command.getStartTime()));
        event.setExecutePath(command.getExecutePath());
        event.setLogPath(command.getLogPath());
        event.setAppIds(command.getAppIds());
        event.setChannel(channel);
        event.setWorkerAddress(workerAddress);
        event.setEvent(TaskEventType.RUNNING);
        return event;
    }

    public static TaskEvent newResultEvent(TaskExecuteResultMessage command, Channel channel, String workerAddress) {
        TaskEvent event = new TaskEvent();
        event.setProcessInstanceId(command.getProcessInstanceId());
        event.setTaskInstanceId(command.getTaskInstanceId());
        event.setState(TaskExecutionStatus.of(command.getStatus()));
        event.setStartTime(DateUtils.timeStampToDate(command.getStartTime()));
        event.setExecutePath(command.getExecutePath());
        event.setLogPath(command.getLogPath());
        event.setEndTime(DateUtils.timeStampToDate(command.getEndTime()));
        event.setProcessId(command.getProcessId());
        event.setAppIds(command.getAppIds());
        event.setVarPool(command.getVarPool());
        event.setChannel(channel);
        event.setWorkerAddress(workerAddress);
        event.setEvent(TaskEventType.RESULT);
        return event;
    }

    public static TaskEvent newRecallEvent(TaskRejectMessage command, Channel channel) {
        TaskEvent event = new TaskEvent();
        event.setTaskInstanceId(command.getTaskInstanceId());
        event.setProcessInstanceId(command.getProcessInstanceId());
        event.setChannel(channel);
        event.setEvent(TaskEventType.WORKER_REJECT);
        return event;
    }

    public static TaskEvent newCacheEvent(int processInstanceId, int taskInstanceId, int cacheTaskInstanceId) {
        TaskEvent event = new TaskEvent();
        event.setProcessInstanceId(processInstanceId);
        event.setTaskInstanceId(taskInstanceId);
        event.setCacheTaskInstanceId(cacheTaskInstanceId);
        event.setEvent(TaskEventType.CACHE);
        return event;
    }

    public static TaskEvent newUpdatePidEvent(TaskUpdatePidMessage command, Channel channel, String workerAddress) {
        TaskEvent event = new TaskEvent();
        event.setProcessInstanceId(command.getProcessInstanceId());
        event.setTaskInstanceId(command.getTaskInstanceId());
        event.setStartTime(DateUtils.timeStampToDate(command.getStartTime()));
        event.setLogPath(command.getLogPath());
        event.setChannel(channel);
        event.setWorkerAddress(workerAddress);
        event.setEvent(TaskEventType.UPDATE_PID);
        return event;
    }
}
