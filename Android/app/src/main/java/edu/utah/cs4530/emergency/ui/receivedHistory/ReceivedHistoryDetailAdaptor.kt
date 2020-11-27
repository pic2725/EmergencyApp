package edu.utah.cs4530.emergency.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.component.picasso.RoundedTransformation
import edu.utah.cs4530.emergency.dao.AlertHistoryUserDAO


class ReceivedHistoryDetailAdaptor(private val data: List<AlertHistoryUserDAO>): RecyclerView.Adapter<ReceivedHistoryDetailAdaptor.ReceivedHistoryDetailHolder>()
{
    inner class ReceivedHistoryDetailHolder(private val view: View) : RecyclerView.ViewHolder(view)
    {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvEmail: TextView = view.findViewById(R.id.tv_email)
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)

        fun setDao(alertHistoryUserDAO: AlertHistoryUserDAO)
        {
            tvName.text = alertHistoryUserDAO.name
            tvEmail.text = alertHistoryUserDAO.phoneNumber
            Picasso.get().load(alertHistoryUserDAO.imageUrl).transform(RoundedTransformation()).into(ivProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedHistoryDetailHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacted, parent, false)

        return ReceivedHistoryDetailHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ReceivedHistoryDetailHolder, position: Int) {
        holder.setDao(data[position])
    }
}