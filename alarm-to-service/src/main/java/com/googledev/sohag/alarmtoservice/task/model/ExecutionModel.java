package com.googledev.sohag.alarmtoservice.task.model;

import android.os.Parcel;

import com.googledev.sohag.alarmtoservice.task.Task;

import java.util.ArrayList;

public final class ExecutionModel implements AdvancedParcel {

    private ArrayList<Class<? extends Task>> tasks;
    private long triggerTime;
    private boolean areSynchronousTasks;
    private boolean isRepetitiveEvent;
    private boolean isOfLowBatteryEvent;
    private boolean isOfIdleEvent;

    protected ExecutionModel(Parcel in) {
        tasks = deserializeFromStringArray(in.readArray(this.getClass().getClassLoader()));
        triggerTime = in.readLong();
        areSynchronousTasks = in.readByte() != 0;
        isRepetitiveEvent = in.readByte() != 0;
        isOfLowBatteryEvent = in.readByte() != 0;
        isOfIdleEvent = in.readByte() != 0;
    }

    public static final Creator<ExecutionModel> CREATOR = new Creator<ExecutionModel>() {
        @Override
        public ExecutionModel createFromParcel(Parcel in) {
            return new ExecutionModel(in);
        }

        @Override
        public ExecutionModel[] newArray(int size) {
            return new ExecutionModel[size];
        }
    };

    public ArrayList<Class<? extends Task>> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Class<? extends Task>> tasks) {
        this.tasks = tasks;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public boolean areSynchronousTasks() {
        return areSynchronousTasks;
    }

    public void setAreSynchronousTasks(boolean areSynchronousTasks) {
        this.areSynchronousTasks = areSynchronousTasks;
    }

    public boolean isRepetitiveEvent() {
        return isRepetitiveEvent;
    }

    public void setRepetitiveEvent(boolean repetitiveEvent) {
        isRepetitiveEvent = repetitiveEvent;
    }

    public boolean isOfLowBatteryEvent() {
        return isOfLowBatteryEvent;
    }

    public void setOfLowBatteryEvent(boolean ofLowBatteryEvent) {
        isOfLowBatteryEvent = ofLowBatteryEvent;
    }

    public boolean isOfIdleEvent() {
        return isOfIdleEvent;
    }

    public void setOfIdleEvent(boolean ofIdleEvent) {
        isOfIdleEvent = ofIdleEvent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(serializeToStringArray(tasks).toArray());
        dest.writeLong(triggerTime);
        dest.writeByte((byte) (areSynchronousTasks ? 1 : 0));
        dest.writeByte((byte) (isRepetitiveEvent ? 1 : 0));
        dest.writeByte((byte) (isOfLowBatteryEvent ? 1 : 0));
        dest.writeByte((byte) (isOfIdleEvent ? 1 : 0));
    }
}
