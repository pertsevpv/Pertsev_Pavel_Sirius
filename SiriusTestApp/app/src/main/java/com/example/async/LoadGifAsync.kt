package com.example.async

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import kotlinx.android.synthetic.main.fragment_post.view.*

class LoadGifAsync(private val root: View) : AsyncTask<String, Unit, RequestBuilder<Drawable>>() {

    override fun onPostExecute(result: RequestBuilder<Drawable>?) {
        with(root) {
            if (result != null) {
                result.into(postImage)
                postImage.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun doInBackground(vararg params: String?): RequestBuilder<Drawable>? {
        return if (params.isNullOrEmpty()) null
        else
            Glide.with(root)
                .load(params[0])
    }

    override fun onPreExecute() {
        with(root) {
            postImage.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

}