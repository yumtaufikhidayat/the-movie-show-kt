package com.taufik.themovieshow.ui.main.about.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.databinding.ItemAboutBinding
import es.dmoral.toasty.Toasty

class AboutAuthorAdapter(val context: Context): ListAdapter<com.taufik.themovieshow.model.response.about.About, AboutAuthorAdapter.AboutViewHolder>(AboutAuthorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder(ItemAboutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AboutViewHolder(private val binding: ItemAboutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(about: com.taufik.themovieshow.model.response.about.About) = with(binding) {
            imgAbout.setImageResource(about.imgAbout)
            tvAboutTitle.text = about.titleAbout
            tvAboutDesc.text = about.descAbout

            itemView.setOnClickListener {
                when(adapterPosition) {
                    0 -> {
                        val urlLink = "https://linkedin.com/in/taufik-hidayat"
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLink))
                            itemView.context.startActivity(Intent.createChooser(intent, "Open with:"))
                        } catch (e: Exception) {
                            showToasty("Please install browser app")
                        }
                    }

                    1-> {
                        val githubLink = "https://github.com/yumtaufikhidayat/the-movie-show-kt"
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubLink))
                            itemView.context.startActivity(Intent.createChooser(intent, "Open with:"))
                        } catch (e: Exception) {
                            showToasty("Please install browser app")
                        }
                    }

                    2 -> {
                        val email = "yumtaufikhidayat@gmail.com"
                        try {
                            val intentEmail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
                            intentEmail.apply {
                                putExtra(Intent.EXTRA_EMAIL, email)
                                putExtra(Intent.EXTRA_SUBJECT, "")
                                putExtra(Intent.EXTRA_TEXT, "")
                            }
                            itemView.context.startActivity(Intent.createChooser(intentEmail, "Send email"))
                        } catch (e: java.lang.Exception) {
                            showToasty("Please install email app")
                        }
                    }
                }
            }
        }
    }

    private fun showToasty(message: String) = Toasty.error(context, message, Toast.LENGTH_SHORT).show()

    object AboutAuthorDiffCallback: DiffUtil.ItemCallback<com.taufik.themovieshow.model.response.about.About>() {
        override fun areItemsTheSame(oldItem: com.taufik.themovieshow.model.response.about.About, newItem: com.taufik.themovieshow.model.response.about.About): Boolean = oldItem.titleAbout == newItem.titleAbout
        override fun areContentsTheSame(oldItem: com.taufik.themovieshow.model.response.about.About, newItem: com.taufik.themovieshow.model.response.about.About): Boolean = oldItem == newItem
    }
}