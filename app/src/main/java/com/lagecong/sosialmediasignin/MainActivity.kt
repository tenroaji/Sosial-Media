package com.lagecong.sosialmediasignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import org.json.JSONException
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private var  SIGN_GOOGLE = 0
    var TAG = "lapar"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callbackManager = CallbackManager.Factory.create()
        checkLoginStatus()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                }
                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })

        //google
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("1060952712888-3q2nqbm16j1jouepjcs1ahv26g4tfvl4.apps.googleusercontent.com")
            .requestEmail()
            .build()

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLoginGoogle.setOnClickListener {
            signInGoogle()
//            firebaseAuthWithGoogle()
        }

    }


    var tokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            if (currentAccessToken == null) {
                Toast.makeText(this@MainActivity, "User Logged out", Toast.LENGTH_LONG).show()
            } else
                loadUserProfile(currentAccessToken)
        }
    }

    private fun loadUserProfile(newAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            newAccessToken
        ) { jsonObject, response ->
            try {
                Log.e("fb","$response")
                val firstName = jsonObject.getString("first_name")
                val lastName = jsonObject.getString("last_name")
                val email = jsonObject.getString("email")
                val id = jsonObject.getString("id")
                val imageUrl = "https://graph.facebook.com/$id/picture?type=normal"
                nextActivity("$firstName $lastName",email,imageUrl)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()

    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_GOOGLE)
    }


    private fun checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken())
        }

//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    var intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                        putExtra("uid",user?.uid)
                        putExtra("name",user?.displayName)
                        putExtra("email",user?.email)
                        putExtra("photo",user?.photoUrl.toString())
                    }
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun handleSignInResult(completedTask : Task<GoogleSignInAccount>) {
    try {
        var account = completedTask.getResult(ApiException::class.java)
        nextActivity(account?.displayName,account?.email,account?.photoUrl.toString())

    } catch (e : ApiException) {
        // The ApiException status code indicates the detailed failure reason.
        // Please refer to the GoogleSignInStatusCodes class reference for more information.
        Log.w(TAG, "signInResult:failed code=" + e.statusCode)
    }
}

    fun nextActivity(nama : String?,email : String?,foto : String?){
        var data = ModelData(nama,email,foto)
        var intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("data",data)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

          if (requestCode == SIGN_GOOGLE) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
       var task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
    }


//         if (requestCode == SIGN_GOOGLE) {
//             Log.e(TAG,"$data")
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    firebaseAuthWithGoogle(account)
//                }
//            } catch (e: ApiException) {
//                Log.e(TAG, "Google sign in failed", e)
//            }
//        }

    }


}
