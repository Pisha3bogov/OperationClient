package com.example.operationclient.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.operationclient.databinding.ActivityAuthenticationBinding
import com.example.operationclient.ui.main.admin.MainAdminActivity
import com.example.operationclient.ui.main.user.MainUserActivity
import com.example.operationclient.ui.main.user.OperationActivityUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class AuthenticationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startRegActivity()

        initNewUser()

        authUser()

        resetPassEnter()
    }

    private fun startRegActivity() {
        binding.regTextView.setOnClickListener {

            val intent = Intent(this,RegistrationActivity::class.java)
            intent.putExtra("email", binding.EmailTextView.text.toString())

            startActivity(intent)
            finish()
        }
    }

    private fun initNewUser() {
        val emailReg = intent.getStringExtra("email")
        val passwordReg = intent.getStringExtra("password")

        binding.EmailTextView.setText(emailReg)
        binding.passwordTextView.setText(passwordReg)
    }

    private fun authUser() {
        binding.logoutButton.setOnClickListener {


            when {
                TextUtils.isEmpty(binding.EmailTextView.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Введите email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.passwordTextView.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Введите пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email = binding.EmailTextView.text.toString().trim { it <= ' '}
                    val password = binding.passwordTextView.text.toString().trim { it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {task ->

                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@AuthenticationActivity,
                                    "Авторизация прошла успешно",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                openActivity(firebaseUser)

                            } else {
                                binding.passwordTextView.text.clear()
                                Toast.makeText(
                                    this@AuthenticationActivity,
                                    "Неправильный логин или пароль",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun resetPassEnter() {
        binding.ResetPassSignTextView.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, ResetPasswordActivity::class.java)
            intent.putExtra("email", binding.EmailTextView.text.toString())

            startActivity(intent)
            finish()
        }
    }

    private fun openActivity(firebaseUser: FirebaseUser) {

        val reference = FirebaseDatabase.getInstance()
            .getReference("users")
        reference.child(firebaseUser.uid).get().addOnSuccessListener {
            if (it.exists()) {
                val key = it.child("key").value.toString().toBoolean()

                if (key) {
                    val intent = Intent(this@AuthenticationActivity,
                        MainAdminActivity::class.java)

                    startActivity(intent)
                    finish()
                } else {

                    val intent = Intent(
                        this@AuthenticationActivity,
                        MainUserActivity::class.java
                    )

                    startActivity(intent)
                    finish()
                }

            } else {
                Toast.makeText(
                    this@AuthenticationActivity,
                    "Dont exit",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener() {
            Toast.makeText(
                this@AuthenticationActivity,
                "Dont exit",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}