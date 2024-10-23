package com.example.recipefinder.ui.dashboard
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.databinding.FavouriteDashboardBinding

class FavouriteFragment : Fragment() {

    private var _binding: FavouriteDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var recipeAdapter: RecipeAdapter
    private val searchHandler = Handler(Looper.getMainLooper()) // For debounce mechanism
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        _binding = FavouriteDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recipeAdapter = RecipeAdapter(listOf())
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
        binding.rvFavorite.adapter = recipeAdapter

        viewModel.searchRecipes("", "")

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            recipeAdapter.updateRecipes(recipes)

            if (recipes.isEmpty()) {
                binding.noRecordsFound.visibility = View.VISIBLE
            } else {
                binding.noRecordsFound.visibility = View.GONE
            }
        })

        // Set up SearchView listener for real-time filtering
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    newText?.let {
                        viewModel.searchRecipes(it, "")
                    }
                }
                searchHandler.postDelayed(searchRunnable!!, 300)
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
    }
}
