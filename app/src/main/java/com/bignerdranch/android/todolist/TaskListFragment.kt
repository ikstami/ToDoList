@file:Suppress("DEPRECATION")
package com.bignerdranch.android.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import java.lang.Exception
import java.util.UUID
import kotlin.math.abs


class TaskListFragment : Fragment() {
    interface Callbacks {
        fun onTaskSelected(listId: UUID)
        fun setToolbarTitle(title: String)
    }
    private var callbacks: Callbacks? = null
    private lateinit var listRecyclerView: RecyclerView
    private var adapter: ListAdapter? = ListAdapter(emptyList())
    private lateinit var addButton: ImageButton
    private val listViewModel:
            TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callbacks?.setToolbarTitle("ToDo List")
        val view =
            inflater.inflate(R.layout.fragment_list, container, false)
        listRecyclerView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        addButton = view.findViewById(R.id.addButton) as ImageButton
        listRecyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { list ->
                list?.let {
                    Log.i("YourTag", "Got lists${list.size}")
                    updateUI(list)
                }
            })
    }
    override fun onStart() {
        try {
            super.onStart()
            addButton.setOnClickListener {
                val list = Task()
                listViewModel.addList(list)
                callbacks?.onTaskSelected(list.id)
            }
        }
        catch(e: Exception)
        {
            Log.d("YourTag", "Result: onStartTaskListF")
        }
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(lists: List<Task>) {
            adapter = ListAdapter(lists)
            listRecyclerView.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private inner class ListHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        private lateinit var list: Task
        private val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        private val priorityChip: Chip = itemView.findViewById(R.id.priority)

        init {
            val gestureDetector = GestureDetector(itemView.context, object : GestureDetector.OnGestureListener {
                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }


                override fun onShowPress(e: MotionEvent) {
                }

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    onClickedList(adapterPosition)
                    return true
                }

                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val SWIPE_THRESHOLD = 100
                    val SWIPE_VELOCITY_THRESHOLD = 100

                    val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
                    val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

                    if (abs(diffX) > abs(diffY) && abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Смахивание вправо
                            handleSwipeRight(adapterPosition)
                        } else {
                            // Смахивание влево
                            handleSwipeRight(adapterPosition)
                        }
                        return true
                    }
                    return false
                }
            })

            itemView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
        }

        @SuppressLint("ResourceAsColor")
        fun bind(list: Task) {
            this.list = list
            titleTextView.text = this.list.title
            if (this.list.priority==1){
                priorityChip.setChipBackgroundColorResource(R.color.red)
            }
            if (this.list.priority==2){
                priorityChip.setChipBackgroundColorResource(R.color.orange)
            }
            if (this.list.priority==3){
                priorityChip.setChipBackgroundColorResource(R.color.yellow)
            }
            priorityChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            priorityChip.text = this.list.priority.toString()
        }

        fun onClickedList(position: Int) {
            callbacks?.onTaskSelected(list.id)
        }
        fun handleSwipeRight(position: Int) {
            listViewModel.deleteList(list.id)
        }

    }


    private inner class ListAdapter(var lists: List<Task>)
        : RecyclerView.Adapter<ListHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ListHolder {

            val view = layoutInflater.inflate(R.layout.list_ite, parent, false)
            return ListHolder(view)
        }
        override fun getItemCount() = lists.size
        override fun onBindViewHolder(holder: ListHolder, position: Int) {
                val list = lists[position]
               holder.bind(list)
        }

    }
    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }
}
