package com.example.recipefinder

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.recipefinder.adapter.AdapterViewPager
import com.example.recipefinder.databinding.ActivityMain2Binding
import com.example.recipefinder.ui.favorite.FavoriteFragment
import com.example.recipefinder.ui.home.HomeFragment
import com.example.recipefinder.ui.slideshow.SlideshowFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private val fragments = listOf(
        HomeFragment(),
        FavoriteFragment(),
        SlideshowFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val auth = Firebase.auth
        val user = auth.currentUser

        viewPager = findViewById(R.id.vp_dashbaord)
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        val adapter = AdapterViewPager(this, fragments)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.Home
                    1 -> R.id.Favorite
                    2 -> R.id.Setting
                    else -> R.id.Home
                }
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.Favorite -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.Setting -> {
                    viewPager.currentItem = 2
                    true
                }
                else -> false
            }
        }
    }
}
