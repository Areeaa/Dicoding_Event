package com.example.mydicodingeventapp.data.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mydicodingeventapp.data.ui.EventAdapter
import com.example.mydicodingeventapp.data.ui.EventViewModel
import com.example.mydicodingeventapp.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{ _, _, _ ->
                    val query = searchView.text.toString()
                    searchBar.setText(query)
                    searchView.hide()

                    eventViewModel.searchFinishedEvents(query)
                    false
                }

        }

        eventViewModel.events.observe(viewLifecycleOwner) { events ->

            if (events.isEmpty()) {
                // kosongkan tampilan ketika item yang dicari tidak ditemukan
                adapter.submitList(emptyList())
                binding.noEventsTextView.visibility = View.VISIBLE
            } else {
                adapter.submitList(events)
                //sembunyikan textview kosong yang ditampilkan hanya ketika item yang ditampilkan tidak tersedia
                binding.noEventsTextView.visibility = View.GONE
            }
        }

        eventViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }


        // Menghubungkan recyclerView dan progressBar dari binding
        recyclerView = binding.rvEvent
        progressBar = binding.progressBar

        adapter = EventAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        eventViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            progressBar.visibility = View.GONE

            adapter.submitList(events)
        }

        progressBar.visibility = View.VISIBLE
        eventViewModel.finishedEvent() // ambil logic api dari viewmodel

        return root
    }

    override fun onResume() {
        super.onResume()
        eventViewModel.resetToastMessage()
        binding.noEventsTextView.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}