package com.example.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.*
import com.example.api.Post
import com.example.async.LoadGifAsync
import com.example.async.LoadPostAsyncTask
import kotlinx.android.synthetic.main.fragment_post.view.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

abstract class BaseFragment : Fragment() {

    protected var loadPostAsyncTask: LoadPostAsyncTask? = null
    private var loadGifAsyncTask: LoadGifAsync? = null
    private lateinit var postList: MutableList<Post>
    protected lateinit var root: View
    private var pageCounter = 0
    private var postCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.root = inflater.inflate(R.layout.fragment_post, container, false)
        with(root) {
            postImage.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            postTitle.visibility = View.INVISIBLE
            if (savedInstanceState != null) {
                postList = savedInstanceState.getParcelableArrayList("LIST_KEY_${mode()}")
                    ?: mutableListOf()
                pageCounter = savedInstanceState.getInt("PAGE_COUNTER_KEY_${mode()}")
                postCounter = savedInstanceState.getInt("POST_COUNTER_KEY_${mode()}")
                if (postCounter > 1) {
                    loadGif(postList[postCounter - 1].gifUrl)
                    postTitle.text = postList[postCounter - 1].title
                    postTitle.visibility = View.VISIBLE
                } else {
                    val post = loadPost(pageCounter++)
                    if (post != null) {
                        postList.add(post)
                        postCounter++
                    } else {
                        postImage.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            } else {
                postList = mutableListOf()
                val post = loadPost(pageCounter++)
                if (post != null) {
                    postList.add(post)
                    postCounter++
                } else {
                    postImage.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }

            postTitle.movementMethod = ScrollingMovementMethod()
            nextButton.setOnClickListener {
                if (postCounter >= postList.size) {
                    val post = loadPost(pageCounter++)
                    if (post != null) {
                        postList.add(post)
                        postCounter++
                    }
                } else {
                    loadGif(postList[postCounter].gifUrl)
                    postTitle.text = postList[postCounter].title
                    postCounter++
                }
            }
            prevButton.setOnClickListener {
                if (postCounter > 1) {
                    postCounter--
                    loadGif(postList[postCounter - 1].gifUrl)
                    postTitle.text = postList[postCounter - 1].title
                } else {
                    AlertDialog.Builder(context).apply {
                        setMessage(R.string.no_posts)
                        setPositiveButton("Ok") { dialog: DialogInterface, _: Int -> dialog.cancel() }
                    }.show()
                }
            }
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("LIST_KEY_${mode()}", ArrayList(postList))
        outState.putInt("PAGE_COUNTER_KEY_${mode()}", pageCounter)
        outState.putInt("POST_COUNTER_KEY_${mode()}", postCounter)
        super.onSaveInstanceState(outState)
    }

    private fun loadGif(url: String) {
        loadGifAsyncTask?.cancel(true)
        loadGifAsyncTask = LoadGifAsync(root)
        loadGifAsyncTask?.execute(url)?.get(15, TimeUnit.SECONDS)
    }

    abstract fun loadPost(id: Int): Post?

    abstract fun mode(): String

}

class BestFragment : BaseFragment() {

    override fun loadPost(id: Int): Post? {
        loadPostAsyncTask?.cancel(true)
        loadPostAsyncTask = LoadPostAsyncTask(root)
        return loadPostAsyncTask?.execute(Pair(MODE.BEST, id))?.get(5, TimeUnit.SECONDS)
    }

    override fun mode(): String = "BEST"
}

class FreshFragment : BaseFragment() {

    override fun loadPost(id: Int): Post? {
        loadPostAsyncTask?.cancel(true)
        loadPostAsyncTask = LoadPostAsyncTask(root)
        return loadPostAsyncTask?.execute(Pair(MODE.FRESH, id))?.get(5, TimeUnit.SECONDS)
    }

    override fun mode(): String = "FRESH"
}

class HotFragment : BaseFragment() {

    override fun loadPost(id: Int): Post? {
        loadPostAsyncTask?.cancel(true)
        loadPostAsyncTask = LoadPostAsyncTask(root)
        return loadPostAsyncTask?.execute(Pair(MODE.HOT, id))?.get(5, TimeUnit.SECONDS)
    }

    override fun mode(): String = "HOT"
}
