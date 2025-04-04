package com.example.reminderslist.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.reminderslist.utils.DataBaseManager

class TaskDAO (context: Context) {
    val dataBaseManager = DataBaseManager(context)
    val categoryDAO = CategoryDAO(context)

    fun insert(task: Task) {
        //Gets the data repository in write mode
        val db = dataBaseManager.writableDatabase

        //Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Task.COLUMN_NAME_TITLE, task.title)
            put(Task.COLUMN_NAME_DONE, task.done)
            put(Task.COLUMN_NAME_CATEGORY, task.category.id)
        }

        try {
            val newRowId = db.insert(Task.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted task with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun update(task: Task) {
        //Gets the data repository in write mode
        val db = dataBaseManager.writableDatabase

        //Create a new map of values, where columns namers are the keys
        val values = ContentValues().apply {
            put(Task.COLUMN_NAME_TITLE, task.title)
            put(Task.COLUMN_NAME_DONE, task.done)
            put(Task.COLUMN_NAME_CATEGORY, task.category.id)
        }

        try {
            val updateRows =
                db.update(Task.TABLE_NAME, values, "${Task.COLUMN_NAME_ID} = ${task.id}", null)

            Log.i("DATABASE", "Update task with id: ${task.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun delete(task: Task) {
        val db = dataBaseManager.writableDatabase

        try {
            val deleteRows = db.delete(Task.TABLE_NAME, "${Task.COLUMN_NAME_ID} = ${task.id}", null)

            Log.i("DATABASE", "Delete task with id: ${task.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun findById(id: Long): Task? {
        val db = dataBaseManager.writableDatabase

        val projection = arrayOf(
            Task.COLUMN_NAME_ID,
            Task.COLUMN_NAME_TITLE,
            Task.COLUMN_NAME_DONE,
            Task.COLUMN_NAME_CATEGORY
        )

        val selection = "${Task.COLUMN_NAME_ID} = $id"

        var task: Task? = null

        try {
            val cursor = db.query(
                Task.TABLE_NAME,  // the table to query
                projection,       // The array of columns to return (pass null to get all)
                selection,      //The columns for the WHERE clause
                null, // The values for the WHERE clause
                null,  // don't group the rows
                null,   // don't filter by row groups
                null     // The sort order
            )
            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))
                val category = categoryDAO.findById(categoryId)!!
                task = Task(id, title, done, category)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return task
    }

    fun findAll(): List<Task> {
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Task.COLUMN_NAME_ID,
            Task.COLUMN_NAME_TITLE,
            Task.COLUMN_NAME_DONE,
            Task.COLUMN_NAME_CATEGORY
        )

        var taskList: MutableList<Task> = mutableListOf()

        try {
            val cursor = db.query(
                Task.TABLE_NAME, // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))
                val category = categoryDAO.findById(categoryId)!!
                val task = Task(id, title, done, category)

                taskList.add(task)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return taskList
    }

    fun findAllByCategory(category: Category): List<Task> {
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Task.COLUMN_NAME_ID,
            Task.COLUMN_NAME_TITLE,
            Task.COLUMN_NAME_DONE,
            Task.COLUMN_NAME_CATEGORY
        )

        val selection = "${Task.COLUMN_NAME_CATEGORY} = ${category.id}"

        var taskList: MutableList<Task> = mutableListOf()

        try {
            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                Task.COLUMN_NAME_DONE               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))
                val category = categoryDAO.findById(categoryId)!!
                val task = Task(id, title, done, category)

                taskList.add(task)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return taskList
    }
}
