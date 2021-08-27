package com.kapirti.a2tme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kapirti.a2tme.R
import com.kapirti.a2tme.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()

        binding.textView2.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSigninFragment())
        }
        binding.button2.setOnClickListener {
            val mail=binding.editTextTextEmailAddress2.text.toString()
            val password=binding.editTextTextPassword2.text.toString()
            auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener { task->
                if (task.isSuccessful){
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToProfileFragment())
                }
            }.addOnFailureListener { ext->
                Toast.makeText(requireContext(),ext.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }


}