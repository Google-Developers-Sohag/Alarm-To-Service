package com.googledev.sohag.alarmtoservice.task.model;

import android.os.Parcelable;

import com.googledev.sohag.alarmtoservice.task.Task;

import java.util.ArrayList;

public interface AdvancedParcel extends Parcelable {

    default ArrayList<Class<? extends Task>> deserializeFromStringArray(final Object[] tasksString) {
        final ArrayList<Class<? extends Task>> tasks = new ArrayList<>();
        for (final Object task : tasksString) {
            try {
                tasks.add((Class<? extends Task>) Class.forName((String) task));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    default ArrayList<String> serializeToStringArray(final ArrayList<Class<? extends Task>> tasks) {
        final ArrayList<String> tasksString = new ArrayList<>();
        for (final Class<? extends Task> task : tasks) {
            tasksString.add(task.getName());
        }
        return tasksString;
    }
}
