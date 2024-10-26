package com.example.mydicodingeventapp.data.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydicodingeventapp.data.response.ListEventsItem
import com.example.mydicodingeventapp.data.ui.EventAdapter
import com.example.mydicodingeventapp.data.ui.ViewModelFactory
import com.example.mydicodingeventapp.databinding.FragmentFavoriteBinding



class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // inisialisasi recycler view, progressbar, dan adapter
        recyclerView = binding.rvEvent
        adapter = EventAdapter()
        recyclerView.adapter = adapter

        //atur layout
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        // inisialisasi viewmodel menggunakan viewmodelfactory
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]


        // observe favorit event
        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->

            val items = arrayListOf<ListEventsItem>()
            favoriteEvents.map {
                val item = ListEventsItem(id = it.id.toInt(), name = it.name, mediaCover = it.mediaCover)
                items.add(item)
            }
            adapter.submitList(items)
        }

        return root
    }
}

