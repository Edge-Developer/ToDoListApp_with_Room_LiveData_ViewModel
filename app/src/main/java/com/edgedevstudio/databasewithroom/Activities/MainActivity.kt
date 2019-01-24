package com.edgedevstudio.databasewithroom.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edgedevstudio.databasewithroom.Async.AppExecutors
import com.edgedevstudio.databasewithroom.R
import com.edgedevstudio.databasewithroom.TaskAdapter
import com.edgedevstudio.databasewithroom.TaskAdapter.ItemClickListener
import com.edgedevstudio.databasewithroom.ViewModel.MainViewModel
import com.edgedevstudio.databasewithroom.database.AppDatabase
import com.edgedevstudio.databasewithroom.database.TaskEntry
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ItemClickListener {
    val TAG = "MainActivity"

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: TaskAdapter
    lateinit var mDB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks)

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = TaskAdapter(this, this)
        mRecyclerView.adapter = mAdapter

        val decoration = DividerItemDecoration(applicationContext, VERTICAL)
        mRecyclerView.addItemDecoration(decoration)

        if (true or false) {

        }

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Called when a user swipes left or right on a ViewHolder
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    // Here is where you'll implement swipe to delete
                    val position = viewHolder.adapterPosition
                    val tasks = mAdapter.tasks
                    AppExecutors.getsInstance().diskIO.execute {
                        mDB.tasksDao().deleteTask(tasks.get(position))
                    }
                }
            }).attachToRecyclerView(mRecyclerView)

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        val fabButton = fab
        fabButton.setOnClickListener {
            Log.d("MainActivity", "fabButton Clicked")
            // Create a new intent to start an AddTaskActivity
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        }
        mDB = AppDatabase.getsInstance(this)
        setupViewModel()
    }

    private fun setupViewModel() {
        Log.d(TAG, "Retrieve Task Method")

        val viewModel =
            ViewModelProviders
                .of(this)
                .get(MainViewModel::class.java)

        viewModel.tasks
            .observe(this,
                Observer<List<TaskEntry>> { taskEntries: List<TaskEntry>? ->
                    mAdapter.tasks = taskEntries
                    Log.d(TAG, "LiveData Update from Database")

                })
    }
}
