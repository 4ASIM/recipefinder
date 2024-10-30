package com.example.recipefinder.ui.MealPlanner
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.R
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.databinding.FragmentMealPlannerBinding
import com.example.recipefinder.databinding.MealplannerBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class MealPlanner : Fragment() {

    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealPlannerViewModel: MealPlannerViewModel
    private lateinit var mealPlannerAdapter: MealPlannerAdapter
    private var firstLoad = true // Flag to track initial load

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlannerBinding.inflate(inflater, container, false)

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()
        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()

        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao, savedDishDao, shoppingListDao)
        mealPlannerViewModel = ViewModelProvider(this, MealPlannerViewModelFactory(repository))
            .get(MealPlannerViewModel::class.java)

        // Set minDate and maxDate for the calendar
        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        binding.calendar.minDate = currentDate

        calendar.add(Calendar.DAY_OF_MONTH, 6)
        val maxDate = calendar.timeInMillis
        binding.calendar.maxDate = maxDate

        // Add a placeholder as the first item
        val mealTimes = listOf(
            "Select a time",
            "Breakfast: 6:00 a.m. - 9:00 a.m.",
            "Mid-Morning Snack: 10:00 a.m. - 11:00 a.m.",
            "Lunch: 12:00 p.m. - 2:00 p.m.",
            "Afternoon Snack: 3:00 p.m. - 5:00 p.m.",
            "Dinner: 6:00 p.m. - 8:00 p.m.",
            "Evening Snack or Supper: 8:00 p.m. - 10:00 p.m."
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.timeSpinner.adapter = adapter
        binding.timeSpinner.setSelection(0)

        // Show date in TextView and make spinner visible on date selection
        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            binding.dateView.text = "Selected Date: $dayOfMonth/${month + 1}/$year"
            binding.timeSpinner.visibility = View.VISIBLE
        }

        binding.timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!firstLoad && position != 0) {
                    showBottomSheetDialog()
                }
                firstLoad = false // Update flag after first use
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return binding.root
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = MealplannerBottomSheetLayoutBinding.inflate(layoutInflater)

        bottomSheetBinding.shoppingListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mealPlannerAdapter = MealPlannerAdapter(emptyList())
        bottomSheetBinding.shoppingListRecyclerView.adapter = mealPlannerAdapter

        // Observe dishes and update adapter
        lifecycleScope.launch {
            mealPlannerViewModel.getAllDishes().collectLatest { dishes ->
                mealPlannerAdapter.updateDishes(dishes)
            }
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

