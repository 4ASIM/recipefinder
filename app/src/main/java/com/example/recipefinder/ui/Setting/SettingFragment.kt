package com.example.recipefinder.ui.Setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipefinder.MainActivity
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentSettingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileImageView = binding.profileImageView
        usernameTextView = binding.usernameTextView
        emailTextView = binding.emailTextView

        auth = FirebaseAuth.getInstance()

              googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)

        binding.signupButton.setOnClickListener {
            signOutUser()
        }

        binding.deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

        loadUserInfo()

        return root
    }

    private fun loadUserInfo() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            usernameTextView.text = user.displayName
            emailTextView.text = user.email

            Glide.with(this)
                .load(user.photoUrl)
                .into(profileImageView)
        } else {
            usernameTextView.text = "User not logged in"
            emailTextView.text = ""
            profileImageView.setImageResource(R.drawable.logorecipe)
        }
    }

    private fun signOutUser() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {

            }
        }
    }

    private fun deleteAccount() {
        val user = auth.currentUser

        user?.let {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
