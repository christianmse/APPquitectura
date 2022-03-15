package com.etsisi.appquitectura.presentation.ui.login.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityLoginBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.GoogleSignInListener
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.ui.main.view.MainActivity
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.startActivity
import com.etsisi.appquitectura.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login, LoginViewModel::class
), GoogleSignInListener {

    override val isSplash: Boolean
        get() = true

    private val isFromMain:Boolean
        get() = runCatching { args.isFromMain }.getOrDefault(false)
    private val args: LoginActivityArgs by navArgs()

    private val contentView: View
        get() = findViewById(android.R.id.content)

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = completedTask.getResult(ApiException::class.java)
                mViewModel.initFirebaseLoginWithCredentials(account, true,this)
            } catch (e: ApiException) {
                mViewModel.initGoogleLoginFailed(e.statusCode)
            }
        }

    private val onPreDrawListener = object: ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            return if (mViewModel.login(this@LoginActivity)) {
                contentView.viewTreeObserver.removeOnPreDrawListener(this)
                true
            } else {
                false
            }
        }
    }

    override fun getActivityArgs(bundle: Bundle) {
        if (intent.data?.host == Constants.DYNAMIC_LINK_PREFIX) {
            Firebase
                .dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    pendingDynamicLinkData?.link?.let { deeplink ->
                        mViewModel.initVerificationCode(pendingDynamicLinkData)
                    }
                }
                .addOnFailureListener(this) { e ->
                    Log.e(TAG, "getDynamicLink:onFailure", e)
                }
        }
    }

    override fun setUpDataBinding(mBinding: ActivityLoginBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            viewModel = mViewModel
            lifecycleOwner = this@LoginActivity
            lifecycle.addObserver(mViewModel)
            if (!isFromMain) {
                contentView.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
            }
        }
    }

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            // String default_web_client_id is auto-generated
            setGoogleClient(this@LoginActivity, getString(R.string.default_web_client_id))
            onError.observe(this@LoginActivity, LiveEventObserver { dialogConfig ->
                navigator.openDialog(dialogConfig)
            })
            onCodeVerified.observe(this@LoginActivity, LiveEventObserver {
                navigator.navigateFromLoginToMain()
            })
            onSuccessLogin.observe(this@LoginActivity, LiveEventObserver {
                navigator.navigateFromLoginToMain()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFromMain) {
            contentView.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
        }
    }

    override fun getFragmentContainer(): Int = mBinding.navHostLogin.id

    override fun initSignInGoogle() {
        googleSignInLauncher.launch(mViewModel.googleClient.signInIntent)
    }

}