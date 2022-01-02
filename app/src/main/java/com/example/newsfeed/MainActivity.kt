package com.example.newsfeed

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchdata()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

    }

    private fun fetchdata() {

        //val queue = Volley.newRequestQueue(this)
        val url =
            "https://newsapi.org/v2/everything?q=tesla&from=2021-12-02&sortBy=publishedAt&apiKey=<your api key>"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonarray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonarray.length()) {
                    val newsJsonObject = newsJsonarray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },

            Response.ErrorListener {})
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

// Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


        }

    /*private fun fetch_data():ArrayList<String>{
        val list= ArrayList<String>()
        for(i in 0 until 25){
            list.add("Item $i")
        }
    return list
    }*/
override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
}
}
