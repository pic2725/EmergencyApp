package edu.utah.cs4530.emergency.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.telcuon.appcard.restful.extension.Date
import com.telcuon.appcard.restful.extension.toString
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.abstract.LiveModelFragment
import edu.utah.cs4530.emergency.dao.AlertHistoryDAO
import edu.utah.cs4530.emergency.extension.getLogger
import kotlinx.android.synthetic.main.fragment_history_detail.view.*

class HistoryDetailFragment: LiveModelFragment<HistoryDetailViewModel>(HistoryDetailViewModel::class, R.layout.fragment_history_detail), OnMapReadyCallback
{
    private val logger by getLogger()
    private lateinit var alertHistoryDAO: AlertHistoryDAO

    override fun onCreateViewModel(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        alertHistoryDAO = arguments!!.getSerializable(BUNDLE_ALERT_HISTORY_DAO) as AlertHistoryDAO

        root.contactedRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        root.mapView.apply {
            onCreate(savedInstanceState)
            onResume()
            MapsInitializer.initialize(activity!!.applicationContext)
            getMapAsync(this@HistoryDetailFragment)
        }

        root.tv_time.text = Date(alertHistoryDAO.time, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX").toString("yyyy/MM/dd HH:mm:ss")
        root.contactedRecyclerView.adapter = ReceivedHistoryDetailAdaptor(ArrayList(alertHistoryDAO.contactedUserPhoneNumber.values))
    }

    override fun onMapReady(map: GoogleMap) {
        map.apply {
            uiSettings.apply {
                isMyLocationButtonEnabled = false
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = false
                isCompassEnabled = false
            }
        }

        alertHistoryDAO.let {
            val latlng = LatLng(it.latitude, it.longitude)
            map.addMarker(MarkerOptions().position(latlng))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15F))
        }
    }

    companion object
    {
        const val BUNDLE_ALERT_HISTORY_DAO = "ALERT_HISTORY_DAO"

        fun makeBundle(alertHistoryDAO: AlertHistoryDAO): Bundle
        {
            //Set arguments
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_ALERT_HISTORY_DAO, alertHistoryDAO)

            return bundle
        }
    }


}