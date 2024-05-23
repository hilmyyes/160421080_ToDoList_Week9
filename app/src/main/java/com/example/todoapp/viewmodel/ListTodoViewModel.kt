package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Todo
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ListTodoViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    val todoLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun refresh() {
        loadingLD.postValue(true)
        todoLoadErrorLD.postValue(false)
        launch {
            val db = buildDb(getApplication())

            todoLD.postValue(db.todoDao().selectAllTodo())
            loadingLD.postValue(false)
        }



        //alternatif chatgpt
//        loadingLD.value = true
//        todoLoadErrorLD.value = false
//        viewModelScope.launch {
//            val db = TodoDatabase.buildDatabase(
//                getApplication()
//            )
//
//            val todos = withContext(Dispatchers.IO){
//                db.todoDao().selectAllTodo()
//            }
//
//            todoLD.value = todos
//            loadingLD.value = false
//        }
    }

    fun clearTask(todo: Todo) {
        launch {
            val db = buildDb(getApplication())
//            Delete di ubah menjadi status is_done
//            db.todoDao().deleteTodo(todo)
            db.todoDao().doneTask(todo.uuid);

            todoLD.postValue(db.todoDao().selectAllTodo())
        }
    }
    }






