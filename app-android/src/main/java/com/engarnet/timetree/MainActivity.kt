package com.engarnet.timetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.calendar.CalendarListActivity
import com.engarnet.timetree.common.Container
import com.engarnet.timetree.databinding.ActivityMainBinding
import com.engarnet.timetree.ui.AuthorizeActivity
import com.engarnet.timetree.ui.AuthorizeParams
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    data class OAuthSetting(
        val clientId: String,
        val clientSecret: String,
        val redirectUrl: String
    ) {
        val isEmpty: Boolean get() = (clientId.isEmpty() || clientSecret.isEmpty() || redirectUrl.isEmpty())
    }

    // TODO: oAuth app settings
    // https://timetreeapp.com/oauth/applications
    private val setting = OAuthSetting(
        clientId = "",
        clientSecret = "",
        redirectUrl = ""
    )

    // TODO: personal access token setting
    // https://timetreeapp.com/personal_access_tokens
    private val personalAccessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.loginButton.setOnClickListener {
            handleLoginButtonClicked()
        }

        binding.accessTokenButton.setOnClickListener {
            handleAccessTokenButtonClicked()
        }
    }

    private fun handleLoginButtonClicked() {
        // 未設定の場合は処理を実施しない
        if (setting.isEmpty) return

        AuthorizeParams(
            clientId = setting.clientId,
            clientSecret = setting.clientSecret,
            redirectUrl = setting.redirectUrl,
            state = UUID.randomUUID().toString(),
            codeVerifier = UUID.randomUUID().toString()
        ).let { AuthorizeActivity.createIntent(this, it) }.let {
            startActivityForResult(it, AuthorizeActivity.REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AuthorizeActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val accessToken = data?.extras?.getString(AuthorizeActivity.KEY_ACCESS_TOKEN) ?: return
            Container.timeTreeClient = TimeTreeClient(accessToken = accessToken)

            moveCalendarList()
        }
    }

    private fun handleAccessTokenButtonClicked() {
        // 未設定の場合は処理を実施しない
        if (personalAccessToken.isEmpty()) return
        Container.timeTreeClient = TimeTreeClient(accessToken = personalAccessToken)

        moveCalendarList()
    }

    private fun moveCalendarList() {
        CalendarListActivity.createIntent(this).let {
            startActivity(it)
        }
    }
}
