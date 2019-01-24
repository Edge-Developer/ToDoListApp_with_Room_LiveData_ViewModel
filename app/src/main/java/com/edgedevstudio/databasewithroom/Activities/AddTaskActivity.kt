package com.edgedevstudio.databasewithroom.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.edgedevstudio.databasewithroom.Async.AppExecutors
import com.edgedevstudio.databasewithroom.R
import com.edgedevstudio.databasewithroom.ViewModel.AddTaskViewModel
import com.edgedevstudio.databasewithroom.ViewModel.AddTaskViewModelFactory
import com.edgedevstudio.databasewithroom.database.AppDatabase
import com.edgedevstudio.databasewithroom.database.TaskEntry
import java.util.*


class AddTaskActivity : AppCompatActivity() {
    // Fields for views
    lateinit var mEditText: EditText
    lateinit var mRadioGroup: RadioGroup
    lateinit var mButton: Button
    lateinit var mDb: AppDatabase

    private var mTaskId = DEFAULT_TASK_ID
    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */

    fun getPriorityFromViews(): Int {
        var priority = 1

        val checkedId = (findViewById(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when (checkedId) {
            R.id.radButton1 -> priority =
                    PRIORITY_HIGH
            R.id.radButton2 -> priority =
                    PRIORITY_MEDIUM
            R.id.radButton3 -> priority =
                    PRIORITY_LOW
        }
        return priority
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initViews()

        mDb = AppDatabase.getsInstance(applicationContext)

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID))
            mTaskId = savedInstanceState.getInt(
                INSTANCE_TASK_ID,
                DEFAULT_TASK_ID
            )

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText("Update")
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(
                    EXTRA_TASK_ID,
                    DEFAULT_TASK_ID
                )

                val factory = AddTaskViewModelFactory(mDb, mTaskId)
                val viewModel = ViewModelProviders
                    .of(this, factory)
                    .get(AddTaskViewModel::class.java)
                viewModel.task.observe(this, Observer<TaskEntry?> { task ->
                    viewModel.task.removeObservers(this)
                    populateUI(task)
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState)
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private fun initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription)
        mRadioGroup = findViewById(R.id.radioGroup)

        mButton = findViewById(R.id.saveButton)
        mButton.setOnClickListener { v -> onSaveButtonClicked() }
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private fun populateUI(task: TaskEntry?) {
        if (task == null) return
        mEditText.setText(task.description)
        setPriorityInViews(task.priority)
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {

        val description = mEditText.text.toString()
        val priority = getPriorityFromViews()
        val date = Date()
        val taskEntry = TaskEntry(description, priority, date)
        AppExecutors.getsInstance().diskIO.execute {
            kotlin.run {
                if (mTaskId == DEFAULT_TASK_ID)
                    mDb.tasksDao().insertTask(taskEntry)
                else {
                    taskEntry.id = mTaskId
                    mDb.tasksDao().updateTask(taskEntry)
                }

                finish()
            }
        }


    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton1
            )
            PRIORITY_MEDIUM -> (findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton2
            )
            PRIORITY_LOW -> (findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton3
            )
        }
    }

    companion object {

        // Extra for the task ID to be received in the intent
        val EXTRA_TASK_ID = "extraTaskId"
        // Extra for the task ID to be received after rotation
        val INSTANCE_TASK_ID = "instanceTaskId"
        // Constants for priority
        val PRIORITY_HIGH = 1
        val PRIORITY_MEDIUM = 2
        val PRIORITY_LOW = 3
        // Constant for default task id to be used when not in update mode
        private val DEFAULT_TASK_ID = -1
        // Constant for logging
        private val TAG = AddTaskActivity::class.java.simpleName
    }
}