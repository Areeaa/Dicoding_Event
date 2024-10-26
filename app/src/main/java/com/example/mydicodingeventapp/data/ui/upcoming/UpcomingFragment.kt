package com.example.mydicodingeventapp.data.ui.upcoming
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydicodingeventapp.data.ui.EventAdapter
import com.example.mydicodingeventapp.data.ui.EventViewModel
import com.example.mydicodingeventapp.databinding.FragmentUpcomingBinding


class UpcomingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        recyclerView = binding.rvEvent
        progressBar = binding.progressBar
        adapter = EventAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        // Observe events
        eventViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            progressBar.visibility = View.GONE // sembunyikan progress bar
            adapter.submitList(events)
        }

        // Setup search
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                searchBar.setText(query)
                searchView.hide()

                // tampilkan progressbar ketika pencarian
                progressBar.visibility = View.VISIBLE
                adapter.submitList(emptyList()) // tampilkan emptylist ketika pencarian
                eventViewModel.searchUpcomingEvents(query)

                false
            }
        }

        // Observe search results
        eventViewModel.events.observe(viewLifecycleOwner) { events ->

            if (events.isEmpty()) {
                binding.noEventsTextView.visibility = View.VISIBLE // menampilkan pesan
            } else {
                binding.noEventsTextView.visibility = View.GONE // sembunyikan pesan
            }
            adapter.submitList(events)
            progressBar.visibility = View.GONE // sembunyikan progressbar ketika hasil pencarian muncul
        }


        // Observe toast messages
        eventViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // panggil api untuk menampilkan event
        progressBar.visibility = View.VISIBLE
        eventViewModel.loadUpcomingEvent()

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