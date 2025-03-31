package com.example.reminderslist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminderslist.R
import com.example.reminderslist.adapters.TaskAdapter
import com.example.reminderslist.data.Task
import com.example.reminderslist.data.TaskDao
import com.example.reminderslist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var taskDAO: TaskDao
    lateinit var taskList: List<Task>
    lateinit var adapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = TaskDao(this)
        adapter = TaskAdapter(emptyList()) { position ->
            val task = taskList[position]

        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addTaskButton.setOnClickListener{
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("TASK_ID", taskId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        taskList = taskDAO.findAll()
        adapter.updateItems(taskList)
    }
}

