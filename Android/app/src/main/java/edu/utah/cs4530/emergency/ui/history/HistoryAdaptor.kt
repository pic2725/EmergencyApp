package edu.utah.cs4530.emergency.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.telcuon.appcard.restful.extension.Date
import com.telcuon.appcard.restful.extension.toString
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.dao.AlertHistoryDAO
import edu.utah.cs4530.emergency.extension.default


class HistoryAdaptor(private val dataList: List<AlertHistoryDAO>): RecyclerView.Adapter<HistoryAdaptor.HistoryHolder>()
{
    inner class HistoryHolder(private val view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback
    {
        val tvDetail: TextView = view.findViewById(R.id.tv_detail)
        val btnShowDetail: Button = view.findViewById(R.id.btn_showDetail)
        val mapView: MapView = view.findViewById(R.id.mapView)

        lateinit var alertHistoryDAO: AlertHistoryDAO

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

        fun setDao(alertHistoryDAO: AlertHistoryDAO)
        {
            this.alertHistoryDAO = alertHistoryDAO

            //Initialize map view
            mapView.apply {
                onCreate(null)
                onResume()
                MapsInitializer.initialize(view.context)
                getMapAsync(this@HistoryHolder)
            }

            tvDetail.text = Date(alertHistoryDAO.time, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX").toString("yyyy/MM/dd HH:mm:ss")
            btnShowDetail.setOnClickListener {
                val navController = Navigation.findNavController(view)
                val navOptions = NavOptions.Builder().default().build()
                navController.navigate(R.id.nav_history_detail, HistoryDetailFragment.makeBundle(alertHistoryDAO), navOptions)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)

        return HistoryHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.setDao(dataList[position])
    }
}