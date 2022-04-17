package cr.ac.menufragment


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DrawerLayout.DrawerListener {

    lateinit var  drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         var toolbar   = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override  fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val title: Int
        when (menuItem.getItemId()) {
            R.id.nav_camera -> title = R.string.menu_camera
            R.id.nav_gallery -> title = R.string.menu_gallery
            R.id.nav_manage -> title = R.string.menu_tools
            R.id.nav_share -> title = R.string.menu_share
            R.id.nav_send -> title = R.string.menu_send
            else -> throw IllegalArgumentException("menu option not implemented!!")
        }
        val fragment: Fragment = HomeFragment.newInstance(getString(title))
        supportFragmentManager
            .beginTransaction()
            //.setCustomAnimations(R.anim.bottom_nav_enter, R.anim.bottom_nav_exit)
            .replace(R.id.home_content, fragment)
            .commit()
        setTitle(getString(title))
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        TODO("Not yet implemented")
    }

    override fun onDrawerOpened(drawerView: View) {
        TODO("Not yet implemented")
    }

    override fun onDrawerClosed(drawerView: View) {
        TODO("Not yet implemented")
    }

    override fun onDrawerStateChanged(newState: Int) {
        TODO("Not yet implemented")
    }
}