package com.example.recipefinder.ui.home.detailsofincredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.databinding.FragmentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class DetailFragment : Fragment() {

    private var dishId: Long? = null
    private var dishName: String? = null
    private var dishImage: String? = null

    private var _binding: FragmentDetailBinding? = null
    private val selectedIngredients = mutableListOf<IngredientEntity>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.addToShoppinglist.setOnClickListener {
            showBottomSheetDialog()
        }

        arguments?.let {
            dishId = it.getLong("dish_id")
            dishName = it.getString("dish_name")
            dishImage = it.getString("dish_image")
        }

        binding.foodName.text = dishName

        Glide.with(requireContext())
            .load(dishImage)
            .centerCrop()
            .into(binding.dishPic)

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val instructionDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()
        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()
        val mealPlanDao = DishDatabase.getDatabase(requireContext()).mealPlanDao()

        if (dishId != null) {
            val viewModel = DetailViewModel(DishRepository(dishDao, ingredientDao, instructionDao, savedDishDao, shoppingListDao,mealPlanDao))

            viewLifecycleOwner.lifecycleScope.launch {
                val isSaved = withContext(Dispatchers.IO) {
                    viewModel.isDishSaved(dishId!!)
                }
                if (isSaved) {
                    binding.addToFavorite.setImageResource(R.drawable.likeheart)
                }

                binding.addToFavorite.setOnClickListener {
                    if (!isSaved) {
                        dishId?.let { id ->
                            viewModel.saveDishId(id)
                            binding.addToFavorite.setImageResource(R.drawable.likeheart)
                        }
                    }
                }
            }

            viewModel.fetchIngredients(dishId!!.toInt()) { ingredients ->
                displayIngredients(ingredients)
            }
            viewModel.fetchInstructionSteps(dishId!!.toInt()) { steps ->
                displayCookingSteps(steps)
            }
        }

        return view
    }
    private fun addIngredientToShoppingList(ingredient: IngredientEntity) {
        if (!selectedIngredients.contains(ingredient)) {
            selectedIngredients.add(ingredient)
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        val saveButton = bottomSheetView.findViewById<Button>(R.id.saveButton)
        val shoppingListRecyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.shoppingListRecyclerView)
        shoppingListRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        shoppingListRecyclerView.adapter = ShoppingListAdapter(selectedIngredients)

        saveButton.setOnClickListener {
            saveSelectedIngredients()
            bottomSheetDialog.dismiss() // Dismiss the dialog after saving
        }

        bottomSheetDialog.show()
    }

    private fun saveSelectedIngredients() {
        val viewModel = DetailViewModel(DishRepository(
            DishDatabase.getDatabase(requireContext()).dishDao(),
            DishDatabase.getDatabase(requireContext()).ingredientDao(),
            DishDatabase.getDatabase(requireContext()).instructionDao(),
            DishDatabase.getDatabase(requireContext()).savedDishDao(),
            DishDatabase.getDatabase(requireContext()).shoppingListDao(),
            DishDatabase.getDatabase(requireContext()).mealPlanDao()

        ))

        viewLifecycleOwner.lifecycleScope.launch {
            selectedIngredients.forEach { ingredient ->
                viewModel.saveShoppingListItem(ingredient)
            }
        }
    }



    private fun displayIngredients(ingredients: List<IngredientEntity>) {
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.ingredientsRecyclerView.adapter = IngredientAdapter(ingredients) { ingredient ->
            addIngredientToShoppingList(ingredient)
        }
        val ingredientList = ingredients.joinToString(separator = "\n") { ingredient ->
            "${ingredient.amount} ${ingredient.unit} ${ingredient.name.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }}"
        }
        binding.ingredientAmount.text = ingredientList
    }


    private fun displayCookingSteps(steps: List<InstructionEntity>) {
        val stepList = steps.joinToString(separator = "\n\n") { step ->
            "${step.stepNumber}. ${step.description.replaceFirstChar { it.uppercase(Locale.getDefault()) }}"
        }
        binding.Steps.text = stepList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
