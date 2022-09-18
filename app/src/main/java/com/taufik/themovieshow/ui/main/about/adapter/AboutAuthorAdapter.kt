package com.taufik.themovieshow.ui.main.about.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.main.about.About
import com.taufik.themovieshow.databinding.ItemAboutBinding
import es.dmoral.toasty.Toasty

class AboutAuthorAdapter: RecyclerView.Adapter<AboutAuthorAdapter.AboutViewHolder>() {

    private val listAbout = ArrayList<About>()

    fun setAbout(about: List<About>){
        this.listAbout.clear()
        this.listAbout.addAll(about)
        notifyDataSetChanged()
    }

    inner class AboutViewHolder(private val binding: ItemAboutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(about: About) {

            with(binding){
                imgAbout.setImageResource(about.imgAbout)
                tvAboutTitle.text = about.titleAbout
                tvAboutDesc.text = about.descAbout
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        val aboutBinding = ItemAboutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AboutViewHolder(aboutBinding)
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {

        val pos = listAbout[position]
        holder.bind(pos)

        holder.itemView.setOnClickListener {
            when(position) {
                0 -> {
                    val urlLink = "https://linkedin.com/in/taufik-hidayat"
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLink))
                        holder.itemView.context.startActivity(Intent.createChooser(intent, "Open with:"))
                    } catch (e: Exception) {
                        Toasty.warning(
                                holder.itemView.context,
                                "Please install browser.",
                                Toast.LENGTH_SHORT, true
                        ).show()
                    }
                }

                1-> {
                    val githubLink = "https://github.com/yumtaufikhidayat/the-movie-show-kt"
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubLink))
                        holder.itemView.context.startActivity(Intent.createChooser(intent, "Open with:"))
                    } catch (e: Exception) {
                        Toasty.warning(
                                holder.itemView.context,
                                "Please install browser.",
                                Toast.LENGTH_SHORT, true
                        ).show()
                    }
                }

                2 -> {
                    val email = "yumtaufikhidayat@gmail.com"
                    try {
                        val intentEmail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, email)
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "")
                        intentEmail.putExtra(Intent.EXTRA_TEXT, "")
                        holder.itemView.context.startActivity(Intent.createChooser(intentEmail, "Send email"))
                    } catch (e: java.lang.Exception) {
                        Toasty.error(holder.itemView.context, "Please install email apps", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = listAbout.size
}