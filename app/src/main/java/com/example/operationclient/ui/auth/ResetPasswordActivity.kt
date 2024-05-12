package com.example.operationclient.ui.auth

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.operationclient.R
import com.example.operationclient.databinding.ActivityRegistrationBinding
import com.example.operationclient.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEmail()

        enterAuthActivity()

        pushSmsEmail()
    }

    private fun initEmail() {
        val email = intent.getStringExtra("email")

        binding.emailTextView.setText(email)
    }

    private fun enterAuthActivity() {
        binding.AuthSignTextView.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity, AuthenticationActivity::class.java)
            intent.putExtra("email", binding.emailTextView.text.toString())

            startActivity(intent)
            finish()
        }
    }

    private fun pushSmsEmail() {
        binding.PushSmsEmailButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.emailTextView.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Введите email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val email = binding.emailTextView.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    "Сообщение отправлено",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val dialogBinding =
                                    layoutInflater.inflate(R.layout.fragment_reset_password, null)
                                val myDialog = Dialog(this)
                                myDialog.setContentView(dialogBinding)
                                myDialog.setCancelable(false)
                                myDialog.show()

                                val buttonAuthAct =
                                    dialogBinding.findViewById<Button>(R.id.authActBut)

                                buttonAuthAct.setOnClickListener {
                                    myDialog.dismiss()

                                    val intent =
                                        Intent(
                                            this@ResetPasswordActivity,
                                            AuthenticationActivity::class.java
                                        )

                                    startActivity(intent)
                                }


                            } else {
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                }
            }
        }
    }
}