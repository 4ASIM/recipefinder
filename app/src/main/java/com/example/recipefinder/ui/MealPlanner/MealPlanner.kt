package com.example.recipefinder.ui.MealPlanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.databinding.FragmentMealPlannerBinding
import com.example.recipefinder.databinding.MealplannerBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class MealPlanner : Fragment() {

    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealPlannerViewModel: MealPlannerViewModel
    private lateinit var mealPlanAdapter: MealPlanAdapter
//    private val viewmodel by viewModels<MealPlannerViewModel>()
    private var firstLoad = true
    private var selectedDate: String? = null

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
        val mealPlanDao = DishDatabase.getDatabase(requireContext()).mealPlanDao()
        val repository = DishRepository(
            dishDao,
            ingredientDao,
            cookingStepDao,
            savedDishDao,
            shoppingListDao,
            mealPlanDao
        )

        mealPlannerViewModel = ViewModelProvider(this, MealPlannerViewModelFactory(repository))
            .get(MealPlannerViewModel::class.java)

        mealPlanAdapter = MealPlanAdapter(emptyList())
        binding.rvMealplanner.layoutManager = LinearLayoutManager(context)
        binding.rvMealplanner.adapter = mealPlanAdapter

        setupCalendar()
        setupSpinner()

        return binding.root
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        binding.calendar.minDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 6)
        binding.calendar.maxDate = calendar.timeInMillis

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            binding.dateView.text = "Selected Date: $dayOfMonth/${month + 1}/$year"
            binding.timeSpinner.visibility = View.VISIBLE
            selectedDate?.let { date ->
                loadMealPlansForSelectedDate(date)
            }
        }
    }

    private fun setupSpinner() {
        val mealTimes = listOf(
            "Select a time",
            "Breakfast: 6:00 a.m. - 9:00 a.m.",
            "Mid-Morning Snack: 10:00 a.m. - 11:00 a.m.",
            "Lunch: 12:00 p.m. - 2:00 p.m.",
            "Afternoon Snack: 3:00 p.m. - 5:00 p.m.",
            "Dinner: 6:00 p.m. - 8:00 p.m.",
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.timeSpinner.adapter = adapter
        binding.timeSpinner.setSelection(0)

        binding.timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!firstLoad && position != 0) {
                    val mealTime = mealTimes[position]
                    selectedDate?.let { date ->
                        showBottomSheetDialog(date, mealTime)
                    }
                }
                firstLoad = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadMealPlansForSelectedDate(date: String) {
        mealPlannerViewModel.getMealPlansForDate(date)
        mealPlannerViewModel.mealPlans.observe(viewLifecycleOwner) { mealPlans ->
            mealPlanAdapter.updateMealPlans(mealPlans)
        }
    }


    private fun showBottomSheetDialog(selectedDate: String, mealTime: String) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = MealplannerBottomSheetLayoutBinding.inflate(layoutInflater)

        bottomSheetBinding.shoppingListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val mealPlannerAdapter = MealPlannerAdapter(emptyList()) { selectedDish ->
            mealPlannerViewModel.saveMealPlan(selectedDate, mealTime, selectedDish.id) {
                Snackbar.make(
                    binding.root,
                    "Dish already added for $mealTime on $selectedDate",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.shoppingListRecyclerView.adapter = mealPlannerAdapter

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
