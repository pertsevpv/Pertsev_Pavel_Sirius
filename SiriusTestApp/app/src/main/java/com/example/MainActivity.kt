package com.example

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fragments.BestFragment
import com.example.fragments.FreshFragment
import com.example.fragments.HotFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var hotFragment: Fragment = HotFragment()
    private var bestFragment: Fragment = BestFragment()
    private var freshFragment: Fragment = FreshFragment()
    private var activeFragment: Fragment = hotFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            initFN()
            AlertDialog.Builder(this).apply {
                setTitle("Внимание!")
                setMessage("При попытке запросить hot посты https://developerslife.ru/hot/0?json=true возвращает просто пустой массив для любой страницы. Тем не менее best и latest посты получаются нормально")
                setPositiveButton("Ok") { dialog, _ -> dialog.cancel() }
            }.show()
        } else {
            bestFragment = supportFragmentManager.findFragmentByTag("best") ?: BestFragment()
            hotFragment = supportFragmentManager.findFragmentByTag("hot") ?: HotFragment()
            freshFragment = supportFragmentManager.findFragmentByTag("fresh") ?: FreshFragment()
            showActive()
        }
        init()
        showActive()
    }

    private fun initFN() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.main_layout, bestFragment, "best")
            add(R.id.main_layout, freshFragment, "fresh")
            add(R.id.main_layout, hotFragment, "hot")
        }.commit()
    }

    private fun showActive() {
        supportFragmentManager.beginTransaction().apply {
            hide(hotFragment)
            hide(bestFragment)
            hide(freshFragment)
            show(activeFragment)
        }.commit()
    }

    private fun init() {
        nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_best -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(bestFragment)
                        .commit()
                    activeFragment = bestFragment
                    true
                }
                R.id.navigation_fresh -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(freshFragment)
                        .commit()
                    activeFragment = freshFragment
                    true
                }
                R.id.navigation_hot -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(hotFragment)
                        .commit()
                    activeFragment = hotFragment
                    true
                }
                else -> false
            }
        }
        nav_view.itemIconTintList = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ACTIVE_FRAGMENT_TAG", activeFragment.tag)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val tag = savedInstanceState.getString("ACTIVE_FRAGMENT_TAG")
        activeFragment = if (tag == null) hotFragment
        else supportFragmentManager.findFragmentByTag(tag) ?: hotFragment
    }

}