package com.example.operationclient.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.operationclient.databinding.ActivityRegistrationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding

    private val CODE_PARTHER: Int = 450321

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAuthActivity()

        registrationUser()

        initEmail()
    }

    private fun startAuthActivity() {
        binding.authTextView.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.putExtra("email", binding.emailTextView.text.toString())

            startActivity(intent)

            finish()
        }
    }

    private fun registrationUser() {
        binding.registrationButton.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    binding.lastFirstNameTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Введите Фамилию и Имя",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.emailTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Введите email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.passwordTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Введите пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(
                    binding.passwordConfirmTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Подтвердите пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.equals(
                    binding.passwordTextView.text.toString(),
                    binding.passwordConfirmTextView.text.toString()
                ).not() -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Пароли не совпадают",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(
                    binding.partherCodeTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Введите код партнера",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.equals(
                    binding.partherCodeTextView.text.toString(),
                    CODE_PARTHER.toString()
                ).not() -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Код партнера введен не верно, уточните его у руководителя",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = binding.emailTextView.text.toString().trim() { it <= ' ' }

                    val password: String =
                        binding.passwordTextView.text.toString().trim() { it <= ' ' }


                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    addUserDb(firebaseUser)

                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Вы зарегистрировались",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(
                                        this@RegistrationActivity,
                                        AuthenticationActivity::class.java
                                    )

                                    intent.putExtra("email", email)
                                    intent.putExtra("password", password)

                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }
    }

    private fun initEmail() {
        val email = intent.getStringExtra("email")

        binding.emailTextView.setText(email)
    }

    private fun addUserDb(firebaseUser: FirebaseUser) {
        val reference= FirebaseDatabase.getInstance().getReference("users")

        val user = Users(firebaseUser.uid,
            binding.lastFirstNameTextView.text.toString(),
            binding.emailTextView.text.toString(),
            false
            )

        reference.child(firebaseUser.uid).setValue(user)
            .addOnCompleteListener {task ->
                if(task.isSuccessful) {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Успешно",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}