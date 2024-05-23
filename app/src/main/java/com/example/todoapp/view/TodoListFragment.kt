package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.viewmodel.ListTodoViewModel


class TodoListFragment : Fragment() {
    private lateinit var viewModel: ListTodoViewModel
    private val todoListAdapter  = TodoListAdapter(arrayListOf(),
        { item -> viewModel.clearTask(item) })


    private lateinit var binding: FragmentTodoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)

        //view model refresh bermasalah:

        viewModel.refresh()

        binding.recViewTodo.layoutManager = LinearLayoutManager(context)
        binding.recViewTodo.adapter = todoListAdapter

        binding.fabAddTodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            todoListAdapter.updateTodoList(it)
            if(it.isEmpty()) {
                binding.recViewTodo?.visibility = View.GONE
                binding.txtError?.visibility = View.VISIBLE
                binding.txtError.setText("Your todo still empty.")
            } else {
                binding.recViewTodo?.visibility = View.VISIBLE
            }
        })

        //udah ok
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.progressLoad?.visibility = View.GONE
            } else {
                binding.progressLoad?.visibility = View.VISIBLE
            }
        })

        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.txtError?.visibility = View.GONE
            } else {
                binding.txtError?.visibility = View.VISIBLE
            }
        })



    }
}