# Alarm-To-Service

A basic utility api for simple and advanced use-cases.

## Quick use : 

To schedule an alarm to start a service task : 
```kotlin
val calendar = Calendar.getInstance()
calendar.set(Calendar.HOUR_OF_DAY, 1)
calendar.set(Calendar.MINUTE, 16)
QuickSetup.startRepetitiveTask(TestTask::class.java, calendar.timeInMillis, applicationContext)
```
The service task test : 
```kotlin
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.Toast
import com.googledev.sohag.alarmtoservice.task.Task
import com.googledev.sohag.alarmtoservice.task.model.ExecutionModel

class TestTask: Task() {
    override fun onTaskCreated(executionModel: ExecutionModel) {
        Toast.makeText(applicationContext, "Hi from service", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    override fun onTaskDestroyed(executionModel: ExecutionModel) {
        Toast.makeText(applicationContext, "Hi from Destroyed", Toast.LENGTH_LONG).show()
    }
}
```
