package edu.utah.cs4530.emergency.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.gun0912.tedonactivityresult.TedOnActivityResult
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.const.OAuthConst
import edu.utah.cs4530.emergency.dao.UserDAO
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.repository.DeviceRepository
import edu.utah.cs4530.emergency.repository.UserRepository
import edu.utah.cs4530.emergency.util.DeviceInfo
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity()
{
    private val logger by getLogger()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(OAuthConst.GoogleApiClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        btn_googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent

            TedOnActivityResult.with(this)
                .setIntent(signInIntent)
                .setListener { resultCode, data ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try
                    {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account!!)
                    }
                    catch (e: ApiException)
                    {
                        logger.warn("Google sign in failed", e)
                    }
                }
                .startActivityForResult()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        logger.debug("firebaseAuthWithGoogle:" + acct.id!!)

        showProgressBar()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    loadUserData(firebaseAuth.currentUser!!)
                }
                else
                {
                    logger.warn("signInWithCredential:failure", task.exception)
                    Snackbar.make(layout_main, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                    hideProgressBar()
                }
            }
    }

    private fun loadUserData(firebaseUser: FirebaseUser)
    {
        UserRepository.getUserOnce(firebaseUser.uid) { result: Boolean, userDAO: UserDAO?, exception: Exception? ->
            if(result)
            {
                UserRepository.setUser(firebaseUser.uid, (userDAO ?: UserDAO()).apply {
                    name = firebaseUser.displayName
                    phoneNumber = DeviceInfo.phoneNumber
                    imageUrl = firebaseUser.photoUrl.toString()
                    fcmToken = DeviceRepository.fcmToken
                })

                startActivity(Intent(this, TutorialActivity::class.java))
                finish()
            }
            else
            {
                logger.warn("loadUserData:failure", exception)
                Snackbar.make(layout_main, "Communication With Database Server Failed.", Snackbar.LENGTH_SHORT).show()
            }

            hideProgressBar()
        }
    }

    private fun showProgressBar()
    {
        progressBar.apply {
            visibility = View.VISIBLE
            isIndeterminate = true
        }
    }

    private fun hideProgressBar()
    {
        progressBar.apply {
            visibility = View.INVISIBLE
            isIndeterminate = false
        }
    }
}