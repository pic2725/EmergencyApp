package edu.utah.cs4530.emergency.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.component.picasso.RoundedTransformation
import edu.utah.cs4530.emergency.dao.AlertHistoryDAO
import edu.utah.cs4530.emergency.dao.AlertReceivedHistoryDAO
import edu.utah.cs4530.emergency.extension.default
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.repository.DeviceRepository
import edu.utah.cs4530.emergency.repository.UserRepository
import edu.utah.cs4530.emergency.ui.history.ReceivedHistoryDetailFragment
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    private val logger by getLogger()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_contacts,
                R.id.nav_history,
                R.id.nav_received_history,
                R.id.nav_editmessage
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Observe user data
        viewModel.userDao.observe(this, Observer {
            it?.let {
                val headerView = navView.getHeaderView(0)
                headerView.tv_name.text = it.name
                headerView.tv_email.text = it.phoneNumber
                Picasso.get().load(it.imageUrl).transform(RoundedTransformation()).into(headerView.iv_profile)
            }
        })

        //Update FCM token
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val fcmToken = instanceIdResult.token
            logger.debug("Successfully get updated FCM token [$fcmToken}]")

            DeviceRepository.fcmToken = fcmToken
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logger.debug("onNewIntent()")

        //Check activity is opened from notification
        intent?.extras?.getSerializable(BUNDLE_ALERT_RECEIVED_HISTORY_DAO)?.let {
            (it as? AlertReceivedHistoryDAO)?.let {
                val navController = findNavController(R.id.nav_host_fragment)
                val navOptions = NavOptions.Builder().default().build()
                navController.navigate(R.id.nav_received_history_detail, ReceivedHistoryDetailFragment.makeBundle(it), navOptions)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object
    {
        const val BUNDLE_ALERT_RECEIVED_HISTORY_DAO = "ALERT_RECEIVED_HISTORY_DAO"

        fun makeBundle(alertReceivedHistoryDAO: AlertReceivedHistoryDAO): Bundle
        {
            //Set arguments
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_ALERT_RECEIVED_HISTORY_DAO, alertReceivedHistoryDAO)

            return bundle
        }
    }
}
