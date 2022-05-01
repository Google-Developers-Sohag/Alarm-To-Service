package com.googledev.sohag.alarmtoservice.core.util;

import android.content.Context;
import android.os.Parcel;

import com.googledev.sohag.alarmtoservice.task.Task;
import com.googledev.sohag.alarmtoservice.task.model.ExecutionModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Quick setup java utility for the work system.
 *
 * @author pavl_g.
 */
public final class QuickSetup {
    public static void startOneTimeTask(final Class<? extends Task> task,
                                        final long triggerTime,
                                        final Context context) {
        final ArrayList<Class<? extends Task>> tasks = new ArrayList<>();
        tasks.add(task);
        startOneTimeTasks(tasks, triggerTime, context, false);
    }

    public static void startRepetitiveTask(final Class<? extends Task> task,
                                           final long triggerTime,
                                           final Context context) {
        final ArrayList<Class<? extends Task>> tasks = new ArrayList<>();
        tasks.add(task);
        startRepetitiveTasks(tasks, triggerTime, context, false);
    }

    public static void startRepetitiveTasks(final ArrayList<Class<? extends Task>> tasks,
                                            final long triggerTime,
                                            final Context context,
                                            final boolean areSynchronous) {
        final ExecutionModel executionModel = setupIgnoreBatteryExecutionModel(tasks, triggerTime, areSynchronous);
        executionModel.setRepetitiveEvent(true);
        TaskManager.INSTANCE.scheduleRepetitively(executionModel, context);
    }

    public static void startOneTimeTasks(final ArrayList<Class<? extends Task>> tasks,
                                         final long triggerTime,
                                         final Context context,
                                         final boolean areSynchronous) {
        final ExecutionModel executionModel = setupIgnoreBatteryExecutionModel(tasks, triggerTime, areSynchronous);
        executionModel.setRepetitiveEvent(true);
        TaskManager.INSTANCE.scheduleOnce(executionModel, context);
    }

    private static ExecutionModel setupBatterySaverExecutionModel(final ArrayList<Class<? extends Task>> tasks,
                                                                  final long triggerTime,
                                                                  final TimeUnit unit,
                                                                  final boolean areSynchronous) {
        final ExecutionModel executionModel = ExecutionModel.CREATOR.createFromParcel(Parcel.obtain());
        executionModel.setTasks(tasks);
        executionModel.setTriggerTime(triggerTime);
        executionModel.setAreSynchronousTasks(areSynchronous);
        executionModel.setOfLowBatteryEvent(false);
        executionModel.setOfIdleEvent(false);
        return executionModel;
    }

    private static ExecutionModel setupIgnoreBatteryExecutionModel(final ArrayList<Class<? extends Task>> tasks,
                                                                   final long triggerTime,
                                                                   final boolean areSynchronous) {
        final ExecutionModel executionModel = ExecutionModel.CREATOR.createFromParcel(Parcel.obtain());
        executionModel.setTasks(tasks);
        executionModel.setTriggerTime(triggerTime);
        executionModel.setAreSynchronousTasks(areSynchronous);
        executionModel.setOfLowBatteryEvent(true);
        executionModel.setOfIdleEvent(true);
        return executionModel;
    }
}
