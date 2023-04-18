package cr.ac.menufragment


import NameFragment
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var drawerLayout: DrawerLayout
    var fragment: Fragment? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        var currentFragment= supportFragmentManager.findFragmentById(R.id.home_content)
        if (currentFragment != null) {
            fragment = currentFragment
        } else {
            // Si el fragmento actual es nulo, crear un nuevo fragmento
            fragment = HomeFragment()
        }

        var toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                // Cerrar el teclado cuando el cajón de navegación cambia de estado
                if (newState == DrawerLayout.STATE_SETTLING || newState == DrawerLayout.STATE_DRAGGING) {
                    closeKeyboard()
                }
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerOpened(drawerView: View) {}
        })

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

    // Función para cerrar el teclado
    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val title: Int

        closeKeyboard()
        when (menuItem.getItemId()) {
            R.id.nav_camera -> {
                title = R.string.menu_camera
                fragment = MapsFragment()
            }
            R.id.nav_gallery -> {
                title = R.string.menu_gallery
                fragment = this.fragment

                val myDialogFragment = MyDialogFragment()
                myDialogFragment.show(supportFragmentManager, "MyDialogFragmentTag")
            }
            R.id.nav_manage ->{
                title = R.string.menu_tools
                fragment= NameFragment()

            }

            else -> throw IllegalArgumentException("menu option not implemented!!")
        }

        supportFragmentManager
            .beginTransaction()
            //.setCustomAnimations(R.anim.bottom_nav_enter, R.anim.bottom_nav_exit)
            .replace(R.id.home_content, fragment!!)
            .commit()
        setTitle(getString(title))
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}