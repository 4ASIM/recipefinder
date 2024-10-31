package com.example.recipefinder.ui.Shoppinglist
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.databinding.FragmentShoppingListBinding


class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)

        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()
        val repository = DishRepository(
            DishDatabase.getDatabase(requireContext()).dishDao(),
            DishDatabase.getDatabase(requireContext()).ingredientDao(),
            DishDatabase.getDatabase(requireContext()).instructionDao(),
            DishDatabase.getDatabase(requireContext()).savedDishDao(),
            DishDatabase.getDatabase(requireContext()).shoppingListDao(),
            DishDatabase.getDatabase(requireContext()).mealPlanDao()
        )

        viewModel = ViewModelProvider(this, ShoppingListViewModelFactory(shoppingListDao)).get(ShoppingListViewModel::class.java)

        shoppingListAdapter = ShoppingListAdapter(requireContext(), emptyList(), binding.noRecordsFound) { item ->
            viewModel.deleteShoppingListItem(item)
        }

        binding.rvShoppingList.adapter = shoppingListAdapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(context)

        viewModel.shoppingList.observe(viewLifecycleOwner) { updatedItems ->
            shoppingListAdapter.updateItems(updatedItems)
        }
        viewModel.getShoppingListItems()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
