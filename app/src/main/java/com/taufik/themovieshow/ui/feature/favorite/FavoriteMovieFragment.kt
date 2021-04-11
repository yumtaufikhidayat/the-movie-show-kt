package com.taufik.themovieshow.ui.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding

class FavoriteMovieFragment : Fragment() {

    private lateinit var favoriteMovieBinding: FragmentFavoriteMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favoriteMovieBinding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return favoriteMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}