package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        binding.BtnLogin.setOnClickListener {
            val loginEmail = binding.LoginEmail.text.toString().trim()
            val loginPassword = binding.LoginPassword.text.toString().trim()
            if(loginEmail.isNotEmpty() && loginPassword.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener{
                    if(it.isSuccessful){
                        saveEmailToPreferences(loginEmail)
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }

        binding.ForgotPass.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener{
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        binding.SignupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveEmailToPreferences(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userEmail", email)
        editor.apply()
    }

    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
