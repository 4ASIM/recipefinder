//package com.example.recipefinder.ui.dashboard
//
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.recipefinder.model.Recipe
//import com.example.recipefinder.model.RetrofitClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//
//class FavouriteViewModel : ViewModel() {
//
//    private val _recipes = MutableLiveData<List<Recipe>>()
//    val recipes: LiveData<List<Recipe>> get() = _recipes
//
//    fun searchRecipes(query: String, ingredients: String) {
//        RetrofitClient.instance.searchRecipes("d5e31bd7064846d4bc71299db2f9570e", query, ingredients, 5)
//            .enqueue(object : Callback<com.example.recipefinder.model.RecipeResponse> {
//                override fun onResponse(
//                    call: Call<com.example.recipefinder.model.RecipeResponse>,
//                    response: Response<com.example.recipefinder.model.RecipeResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        _recipes.postValue(response.body()?.results ?: emptyList())
//                    }
//                }
//
//                override fun onFailure(call: Call<com.example.recipefinder.model.RecipeResponse>, t: Throwable) {
//                    _recipes.postValue(emptyList())
//                }
//            })
//    }
//}
