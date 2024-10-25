package com.example.mydicodingeventapp.data.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydicodingeventapp.data.ui.EventAdapter
import com.example.mydicodingeventapp.data.ui.EventAdapter2
import com.example.mydicodingeventapp.data.ui.EventViewModel
import com.example.mydicodingeventapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var adapter1: EventAdapter
    private lateinit var adapter2: EventAdapter2
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as? AppCompatActivity)?.supportActionBar?.show()


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menghubungkan recyclerView dan progressBar dari binding
        recyclerView1 = binding.rvEvent
        recyclerView2 = binding.rvEvent2
        progressBar = binding.progressBar

        adapter1 = EventAdapter()
        adapter2 = EventAdapter2()

        recyclerView1.adapter = adapter1
        recyclerView1.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        // Observe upcoming events
        eventViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            progressBar.visibility = View.GONE
            val limitedEvent = events.take(5)
            adapter1.submitList(limitedEvent)
        }

        // Observe finished events
        eventViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            progressBar.visibility = View.GONE
            val limitedEvent = events.take(5)
            adapter2.submitList(limitedEvent)
        }


        progressBar.visibility = View.VISIBLE
        eventViewModel.loadUpcomingEvent()
        eventViewModel.finishedEvent()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}