package com.example.async

import android.os.AsyncTask
import android.view.View
import com.example.api.Post
import com.example.api.PostListResult
import com.example.application.DevApplication
import com.example.fragments.MODE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_post.view.*
import retrofit2.Call
import java.lang.Exception
import kotlin.random.Random

class LoadPostAsyncTask(private val root: View) :
    AsyncTask<Pair<MODE, Int>, Unit, Post?>() {

    override fun onPostExecute(result: Post?) {
        with(root) {
            if (result != null) {
                root.postImage.visibility = View.VISIBLE
                postTitle.text = result.title
                postTitle.visibility = View.VISIBLE
                LoadGifAsync(root).execute(result.gifUrl)
            } else
                progressBar.visibility = View.INVISIBLE
        }
    }

    override fun doInBackground(vararg params: Pair<MODE, Int>?): Post? {
        runCatching {
            if (params.isNullOrEmpty()) throw Exception("Invalid arguments")
            val pair = params[0] ?: throw Exception("Invalid arguments")
            val query: Call<PostListResult> = when (pair.first) {
                MODE.BEST -> DevApplication.instance.devAPIService.getBestPostList(pair.second)
                MODE.HOT -> DevApplication.instance.devAPIService.getHotPostList(pair.second)
                MODE.FRESH -> DevApplication.instance.devAPIService.getFreshPostList(pair.second)
            }

            val postList = query.execute().body()
            return if (postList == null || postList.list.isNullOrEmpty()) throw Exception("Loaded data is null or empty")
            else postList.list[Random.nextInt(0, postList.list.size)]
        }.onFailure {
            try {
                Snackbar.make(root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }catch (ignore : Exception){
            }
        }
        return null
    }

    override fun onPreExecute() {
        with(root) {
            progressBar.visibility = View.VISIBLE
            postImage.visibility = View.INVISIBLE
            postTitle.visibility = View.INVISIBLE
        }
    }
}