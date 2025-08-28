package net.ticherhaz.karangancemerlangspm.huawei
/*

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agc.clouddb.quickstart.HomePageFragment
import net.ticherhaz.karangancemerlangspm.huawei.model.LoginHelper
import net.ticherhaz.karangancemerlangspm.huawei.model.StorageLocationHelper

class MainActivity : AppCompatActivity() {
    private var mViewPager: ViewPager? = null
    private var mNavigationBar: BottomNavigationView? = null
    var loginHelper: LoginHelper? = null
    var storageLocationHelper: StorageLocationHelper? = null
        private set
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        updateTitle()
        if (item.itemId == R.id.navigation_home) {
            if (mViewPager!!.currentItem == Page.HOME.pos) {
                return@OnNavigationItemSelectedListener true
            }
            mViewPager!!.setCurrentItem(Page.HOME.pos, true)
        } else if (item.itemId == R.id.navigation_about_me) {
            if (mViewPager!!.currentItem == Page.ABOUT_ME.pos) {
                return@OnNavigationItemSelectedListener true
            }
            mViewPager!!.setCurrentItem(Page.ABOUT_ME.pos, true)
        }
        true
    }
    private val mPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
        override fun onPageSelected(i: Int) {
            updateTitle()
            if (i == Page.HOME.pos) {
                if (mNavigationBar!!.selectedItemId == R.id.navigation_home) {
                    return
                }
                mNavigationBar!!.selectedItemId = R.id.navigation_home
            } else {
                if (mNavigationBar!!.selectedItemId == R.id.navigation_about_me) {
                    return
                }
                mNavigationBar!!.selectedItemId = R.id.navigation_about_me
            }
        }

        override fun onPageScrollStateChanged(i: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.container)
        mViewPager?.run {
            val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
            adapter = sectionsPagerAdapter
            addOnPageChangeListener(mPageChangeListener)
        }

        mNavigationBar = findViewById(R.id.nav_view)
        mNavigationBar?.run {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
        loginHelper = LoginHelper(this)
        storageLocationHelper = StorageLocationHelper();
    }

    override fun onResume() {
        updateTitle()
        super.onResume()
    }

    fun hideNavigationBar() {
        mNavigationBar!!.visibility = View.GONE
    }

    fun showNavigationBar() {
        mNavigationBar!!.visibility = View.VISIBLE
    }

    private fun updateTitle() {
        title = if (mViewPager!!.currentItem == Page.HOME.pos) {
            getString(R.string.book_manager_title)
        } else {
            getString(R.string.user_info_str)
        }
    }

    private enum class Page(val pos: Int) {
        HOME(0), ABOUT_ME(1);

    }

    private inner class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return if (position == Page.HOME.pos) {
                HomePageFragment.newInstance()
            } else {
                AboutMeFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return Page.values().size
        }
    }
}
*/
