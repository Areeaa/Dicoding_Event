package com.example.mydicodingeventapp.data.ui
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydicodingeventapp.data.response.ListEventsItem
import com.example.mydicodingeventapp.databinding.EventListCarouselBinding


class EventAdapter2 : ListAdapter<ListEventsItem, EventAdapter2.MyViewHolder>(DIFF_CALLBACK){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventListCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener {view->
            val context = view.context
            val intent = Intent(context, DetailActivity::class.java)

            //kirim eventId ke Detail Fragment untuk ditampilkan detailnya
            intent.putExtra("EVENT_ID", event.id.toString())
            context.startActivity(intent)
        }
    }



    class MyViewHolder (private val binding: EventListCarouselBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            binding.descriptionEvent.text = event.name
            Glide.with(binding.imgEvent)
                .load(event.mediaCover)
                .into(binding.imgEvent)

        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}