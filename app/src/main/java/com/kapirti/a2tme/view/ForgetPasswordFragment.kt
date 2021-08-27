package com.kapirti.a2tme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.kapirti.a2tme.R
import com.kapirti.a2tme.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding:FragmentForgetPasswordBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentForgetPasswordBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()

        binding.textViewRememberPassword.setOnClickListener {
            findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToSigninFragment())
        }
        binding.floatingActionButton.setOnClickListener {
            val mail=binding.editTextTextEmailAddress3.text.toString()
            auth.sendPasswordResetEmail(mail).addOnCompleteListener { task->
                if (task.isSuccessful){
                    findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToSigninFragment())
                }
            }.addOnFailureListener { ex->
                Toast.makeText(requireContext(),ex.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }


}