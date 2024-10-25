package com.example.mydicodingeventapp.data.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mydicodingeventapp.R
import com.example.mydicodingeventapp.data.database.FavouriteEvent
import com.example.mydicodingeventapp.data.response.Event
import com.example.mydicodingeventapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var isFavourite = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[DetailViewModel::class.java]
        val eventId = intent.getStringExtra("EVENT_ID")

        // Observe loading dan event detail
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        //tampilkan detailEvent
        viewModel.event.observe(this) { event ->
            detailEvent(event)
        }

        eventId?.let {
            viewModel.loadDetail(it)

            // Cek apakah event sudah ada di favorite
            viewModel.getFavouriteEventById(it).observe(this) { favouriteEvent ->
                if (favouriteEvent != null) {
                    isFavourite = true
                    binding.fabFavorite.setImageResource(R.drawable.favorite_24) // Ubah ikon ke full
                } else {
                    isFavourite = false
                    binding.fabFavorite.setImageResource(R.drawable.favourite_border_24) // Ubah ikon ke border
                }
            }
        }

        // Listener untuk menambahkan atau menghapus favorite
        binding.fabFavorite.setOnClickListener {
            viewModel.event.value?.let { event ->
                val favouriteEvent = FavouriteEvent(
                    id = event.id.toString(),
                    name = event.name ?: "",
                    mediaCover = event.mediaCover
                )

                if (isFavourite) {
                    // Hapus dari favorite
                    viewModel.delete(favouriteEvent)
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    // Tambah ke favorite
                    viewModel.insert(favouriteEvent)
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //listener untuk menuju ke web pendaftaran
        binding.btnBrowse.setOnClickListener{
            viewModel.event.value?.link.let { link->

                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browseIntent)
            }
        }

    }

    private fun detailEvent(event: Event?) {
        event?.let {
            with(binding) {
                tvNameDetailEvent.text = it.name
                tvWaktuAcara.text = it.beginTime
                tvQuota.text = ((it.quota ?: 0) - (it.registrants ?: 0)).toString()
                tvOwnerName.text = it.ownerName
                tvDetailEvent.text = HtmlCompat.fromHtml(it.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.ivPicture)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
