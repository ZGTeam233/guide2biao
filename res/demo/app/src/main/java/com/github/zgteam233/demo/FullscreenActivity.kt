package com.github.zgteam233.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.InputStream
import org.json.JSONObject
import java.io.IOException
import org.json.JSONException
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView


class FullscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        val webView: WebView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // 启用JavaScript
        webSettings.domStorageEnabled =true // 启用DOM存储，适用于需要本地存储的网页
        val targetUrl =readConfigFromAssets()
        if (!targetUrl.isNullOrBlank()) {
            webView.loadUrl(targetUrl)
        } else {
            // 处理URL读取失败的情况，加载一个默认的线上URL
            webView.loadUrl("https://github.com")
            Log.e("FullscreenActivity", "Failed to load URL from config. Using fallback.")
        }
    }

    private fun readConfigFromAssets(): String? {
        return try {
            // 1. 打开assets中的文件
            val inputStream: InputStream = assets.open("config.json")
            // 2. 将文件流转换为字符串
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            // 3. 解析JSON，获取"url"字段的值
            val jsonObject = JSONObject(jsonString)
            jsonObject.optString("url").takeIf { it.isNotBlank() }
        } catch (e: IOException) {
            Log.e("FullscreenActivity", "读取config.json文件时发生IO错误", e)
            null
        } catch (e: JSONException) {
            Log.e("FullscreenActivity", "解析config.json时发生JSON格式错误", e)
            null
        } catch (e: Exception) {
            Log.e("FullscreenActivity", "读取配置时发生未知错误", e)
            null
        }
    }
}
