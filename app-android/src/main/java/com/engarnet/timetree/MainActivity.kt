package com.engarnet.timetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.databinding.ActivityMainBinding
import com.engarnet.timetree.ui.AuthorizeActivity
import com.engarnet.timetree.ui.AuthorizeParams
import com.engarnet.timetree.util.Container
import com.engarnet.timetree.view.CalendarRecyclerViewAdapter
import kotlinx.coroutines.runBlocking
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

    // TODO: OAuthアプリケーションの情報を設定
    private val setting = OAuthSetting(
        clientId = "",
        clientSecret = "",
        redirectUrl = ""
    )

    // TODO: パーソナルアクセストークンを設定
    private val personalAccessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = CalendarRecyclerViewAdapter("カレンダー一覧", listOf()) { position ->
            startActivity(
                CalendarActivity.createIntent(
                    this,
                    binding.adapter!!.items[position]
                )
            )
        }
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

            loadCalendars()
        }
    }

    private fun handleAccessTokenButtonClicked() {
        // 未設定の場合は処理を実施しない
        if (personalAccessToken.isEmpty()) return
        Container.timeTreeClient = TimeTreeClient(accessToken = personalAccessToken)

        loadCalendars()
    }

    private fun loadCalendars() {
        runBlocking {
            runCatching {
                Container.timeTreeClient.calendars()
            }.onSuccess { calendars ->
                binding.adapter!!.items = calendars
                binding.adapter!!.notifyDataSetChanged()
            }.onFailure {
                print(it)
            }
        }
    }
}
