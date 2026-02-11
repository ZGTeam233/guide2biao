package com.github.zgteam233.demo

import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil.isValidUrl
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var errorCount = 0
    private val maxErrorRetries = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        val config = readConfigFromAssets()

        with(webSettings) {
            javaScriptEnabled = config.enableJavaScript // 是否允许执行 JS
            domStorageEnabled = true // 启用 DOM 存储，适用于需要本地存储的网页
            useWideViewPort = true // 将图片调整到适合 WebView 的大小
            loadWithOverviewMode = true // 缩放至屏幕的大小

            // 根据配置设置缩放功能
            setSupportZoom(config.enableZoom)
            builtInZoomControls = config.enableZoom
            displayZoomControls = false // 隐藏默认的缩放控件

            // 其他推荐设置
            allowFileAccess = true // 允许访问文件
            cacheMode = WebSettings.LOAD_DEFAULT // 使用默认缓存策略
        }

        // 设置 WebViewClient，控制页面加载行为
        webView.webViewClient = object : WebViewClient() {
            // 确保链接在应用内打开，不调用系统浏览器
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true // 表示链接已在当前WebView内处理
            }
            // 页面加载完成后的处理（例如隐藏加载动画）
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("MainActivity", "页面加载完成: $url")
            }

            // 页面加载失败时的处理
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)

                // 关键修复：只有当主框架（整个页面）加载失败时才处理
                if (request != null && request.isForMainFrame) {
                    errorCount++
                    Log.e("MainActivity", "主页面加载出错 (${errorCount}/$maxErrorRetries): ${error.description}")

                    // 防止无限循环：达到最大重试次数后停止
                    if (errorCount <= maxErrorRetries) {
                        // 检查备用URL是否有效
                        if (isValidUrl(config.fallbackUrl)) {
                            view?.loadUrl(config.fallbackUrl)
                        } else {
                            // 如果备用URL也无效，加载硬编码的默认URL
                            view?.loadUrl("https://www.github.com")
                            Log.w("MainActivity", "备用URL无效，使用硬编码默认URL")
                        }
                    } else {
                        Log.w("MainActivity", "达到最大重试次数，停止加载")
                        // 这里可以显示一个错误页面或提示信息
                    }
                } else {
                    // 非主框架的资源加载失败，只记录日志，不跳转
                    Log.w("MainActivity", "资源加载失败（忽略）: ${error.description}")
                }
            }
        }

        if (config.url.isNotBlank()) {
            webView.loadUrl(config.url)
            Log.d("MainActivity", "正在加载: ${config.url}")
        } else {
            // 如果配置中的URL为空，则使用备用URL
            webView.loadUrl(config.fallbackUrl)
            Log.w("MainActivity", "配置URL为空，使用备用URL")
        }
    }

    private fun readConfigFromAssets(): WebViewConfig {
        return try {
            // 打开assets中的config.json文件
            val inputStream = assets.open("config.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // 解析JSON数据
            val jsonObject = JSONObject(jsonString)

            WebViewConfig(
                url = jsonObject.optString("url", "https://baidu.com"),
                enableJavaScript = jsonObject.optBoolean("enableJavaScript", true),
                enableZoom = jsonObject.optBoolean("enableZoom", false),
                fallbackUrl = jsonObject.optString("fallbackUrl", "https://github.com"),
                // 新增调试字段，默认false
                debugForceFallback = jsonObject.optBoolean("debugForceFallback", false)
            )

        } catch (e: IOException) {
            // 文件读取失败
            Log.e("MainActivity","读取配置文件失败: ${e.message}")
            WebViewConfig() // 返回默认配置
        } catch (e: Exception) {
            // JSON解析失败
            Log.e("MainActivity","解析JSON配置失败: ${e.message}")
            WebViewConfig() // 返回默认配置
        }
    }

}

data class WebViewConfig(
    val url: String = "https://baidu.com", // 默认URL
    val enableJavaScript: Boolean = true,
    val enableZoom: Boolean = false,
    val fallbackUrl: String = "https://github.com",
    val debugForceFallback: Boolean = false // 新增调试字段
)