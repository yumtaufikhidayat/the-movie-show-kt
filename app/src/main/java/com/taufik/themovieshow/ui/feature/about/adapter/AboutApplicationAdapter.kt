package com.taufik.themovieshow.ui.feature.about.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemAboutBinding
import com.taufik.themovieshow.ui.feature.about.data.About
import es.dmoral.toasty.Toasty

class AboutApplicationAdapter: RecyclerView.Adapter<AboutApplicationAdapter.AboutViewHolder>() {

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
                    // do in app update
                    // see https://developer.android.com/guide/playcore/in-app-updates
                }

                1 -> {
                    // do rate by intent to play store
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
                        Log.e("errorEmail", "developerEmail: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = listAbout.size
}