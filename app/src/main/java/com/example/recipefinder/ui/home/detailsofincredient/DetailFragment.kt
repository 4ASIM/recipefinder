package com.example.recipefinder.ui.home.detailsofincredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class DetailFragment : Fragment() {

    private var dishId: Long? = null
    private var dishName: String? = null
    private var dishImage: String? = null

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            dishId = it.getLong("dish_id")
            dishName = it.getString("dish_name")
            dishImage = it.getString("dish_image")
        }

        binding.foodName.text = dishName

        Glide.with(requireContext())
            .load(dishImage)
//            .placeholder(R.drawable.loading)
//            .error(R.drawable.error)
            .centerCrop()
            .into(binding.dishPic)

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val instructionDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()

        if (dishId != null) {
            val viewModel =
                DetailViewModel(DishRepository(dishDao, ingredientDao, instructionDao, savedDishDao))

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val drawerImage = requireActivity().findViewById<ImageView>(R.id.drawer_image)
//        val fragmentNameTextView = requireActivity().findViewById<TextView>(R.id.fragment_name_text_view)
//
//        drawerImage.visibility = View.GONE
//        fragmentNameTextView.visibility = View.GONE
    }
    private fun displayIngredients(ingredients: List<IngredientEntity>) {
        val ingredientList = ingredients.joinToString(separator = "\n") { it ->
            "${it.amount} ${it.unit} ${
                it.name.split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                }
            }"
        }
        binding.ingredientAmount.text = ingredientList

        val ingredientNames = ingredients.joinToString(separator = "\n") { it ->
            it.name.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }
        }
        binding.IngredeintsItems.text = ingredientNames
    }

    private fun displayCookingSteps(steps: List<InstructionEntity>) {
        val stepList = steps.joinToString(separator = "\n\n") { it ->
            "${it.stepNumber}. ${
                it.description.split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                }
            }"
        }
        binding.Steps.append(stepList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
