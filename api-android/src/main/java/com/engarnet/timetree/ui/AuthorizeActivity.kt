package com.engarnet.timetree.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.engarnet.timetree.R
import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.client.impl.DefaultApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.authorization.AuthorizationApi
import com.engarnet.timetree.model.TAccessToken
import com.engarnet.timetree.model.toModel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class AuthorizeActivity : AppCompatActivity() {
    private val webView: WebView by lazy {
        findViewById<WebView>(R.id.web_view)
    }
    private val apiClient: ApiClient by lazy {
        DefaultApiClient("")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.authorize_activity)
        val param = intent?.extras?.getString(KEY_AUTHORIZE_PARAMS)?.let {
            Json.parse(AuthorizeParams.serializer(), it)
        } ?: throw IllegalStateException("query parameter not found")

        val url = "${Config.V1.authorizeUrl}?${param.toQueryParams()}"
        val extraHeaders = mapOf("Accept" to "application/vnd.timetree.v1+json")
        CookieManager.getInstance().removeAllCookies(null)
        webView.apply {
            clearCache(true)
            settings.apply {
                cacheMode = WebSettings.LOAD_NO_CACHE
                javaScriptEnabled = true
                setSupportZoom(true)
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val loadUrl = request?.url.toString()
                    if (loadUrl.startsWith(param.redirectUrl)) {
                        val code = Uri.parse(loadUrl)?.getQueryParameter("code") ?: return true
                        // 認証成功でリダイレクトされた場合
                        val api = AuthorizationApi(apiClient)
                        runBlocking {
                            val response = api.token(param.toTokenParam(code)).toModel()
                            setResult(response)
                        }
                        return true
                    }
                    view?.loadUrl(loadUrl)
                    return true
                }
            }
        }
        webView.loadUrl(url, extraHeaders)
    }

    private fun setResult(token: TAccessToken) {
        Intent().apply {
            putExtra(KEY_ACCESS_TOKEN, token.accessToken)
        }.let {
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    companion object {
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val REQUEST_CODE = 1
        private const val KEY_AUTHORIZE_PARAMS = "key_authorize_params"
        fun createIntent(context: Context, params: AuthorizeParams): Intent {
            return Intent(context, AuthorizeActivity::class.java).apply {
                putExtra(KEY_AUTHORIZE_PARAMS, Json.stringify(AuthorizeParams.serializer(), params))
            }
        }
    }
}

