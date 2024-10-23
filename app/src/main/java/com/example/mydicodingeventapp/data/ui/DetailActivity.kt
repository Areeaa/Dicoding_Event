package com.example.mydicodingeventapp.data.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mydicodingeventapp.data.response.Event
import com.example.mydicodingeventapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val eventId = intent.getStringExtra("EVENT_ID")

        //menampilkan progressbar
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE // menampilkan progress bar ketika loading
            } else {
                binding.progressBar.visibility = View.GONE // menghilangkan progressbar ketika tidak loading
            }
        }


        // tampilkan detail event
        eventId?.let {
            viewModel.loadDetail(it)

        }

        viewModel.event.observe(this){ event ->
            detailEvent(event)
        }


        binding.btnBrowse.setOnClickListener{
            viewModel.event.value?.link.let { link->

                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browseIntent)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }



    private fun detailEvent(event: Event?) {
        event?.let {

            with(binding){
                tvNameDetailEvent.text = it.name
                tvWaktuAcara.text = it.beginTime
                tvQuota.text = ((it.quota?: 0) - (it.registrants ?: 0)).toString()
                tvOwnerName.text = it.ownerName
                tvDetailEvent.text = HtmlCompat.fromHtml(it.description.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.ivPicture)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==android.R.id.home){
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
