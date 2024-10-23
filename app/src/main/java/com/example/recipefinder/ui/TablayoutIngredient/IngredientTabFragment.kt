package com.example.recipefinder.ui.TablayoutIngredient

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipefinder.R

class IngredientTabFragment : Fragment() {

    companion object {
        fun newInstance() = IngredientTabFragment()
    }

    private lateinit var viewModel: IngredientTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredient_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IngredientTabViewModel::class.java)
        // TODO: Use the ViewModel
    }

}