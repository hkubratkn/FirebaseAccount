package com.kapirti.a2tme.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kapirti.a2tme.R
import com.kapirti.a2tme.databinding.FragmentSigninBinding

class SigninFragment : Fragment() {

    private lateinit var binding:FragmentSigninBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth:FirebaseAuth

    private companion object{
        private const val RC_SIGN_IN=100
        private const val TAAG="GOOGLE_SIGN_IN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSigninBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient= GoogleSignIn.getClient(requireContext(),googleSignInOptions)

        auth= FirebaseAuth.getInstance()
        checkUser()

        binding.googleSigninBtn.setOnClickListener {
            Log.d(TAG,"begin sign in")
            val intent=googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
        binding.button.setOnClickListener {
            val email=binding.editTextTextEmailAddress.text.toString()
            val password=binding.editTextTextPassword.text.toString()
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if (task.isSuccessful){
                    findNavController().navigate(SigninFragmentDirections.actionSigninFragmentToProfileFragment())
                }
            }.addOnFailureListener { ext->
                Toast.makeText(requireContext(),ext.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }
        binding.textView.setOnClickListener {
            findNavController().navigate(SigninFragmentDirections.actionSigninFragmentToLoginFragment())
        }
        binding.textViewForget.setOnClickListener {
            findNavController().navigate(SigninFragmentDirections.actionSigninFragmentToForgetPasswordFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RC_SIGN_IN){
            Log.d(TAG,"google signin result")
            val accountTask=GoogleSignIn.getSignedInAccountFromIntent(data)
            val account=accountTask.getResult(ApiException::class.java)
            firebaseAuthSignInWithGoogleAccount(account)
        }
    }
    private fun firebaseAuthSignInWithGoogleAccount(account:GoogleSignInAccount){
        Log.d(TAG,"begin")
        val credential=GoogleAuthProvider.getCredential(account!!.idToken,null)
        auth.signInWithCredential(credential).addOnSuccessListener { authResult->
            Log.d(TAG,"LoggedIn")
            val firebaseUser=auth.currentUser
            val uid=firebaseUser!!.uid
            val email=firebaseUser.email
            Log.d(TAG,uid)
            Log.d(TAG,email!!)
            if (authResult.additionalUserInfo!!.isNewUser){
                Log.d(TAG,email)
            }else{
                Log.d(TAG,email)
            }
            findNavController().navigate(SigninFragmentDirections.actionSigninFragmentToProfileFragment())
        }
    }
    private fun checkUser(){
        val current=auth.currentUser
        if (current!=null){
            findNavController().navigate(SigninFragmentDirections.actionSigninFragmentToProfileFragment())
        }
    }








}