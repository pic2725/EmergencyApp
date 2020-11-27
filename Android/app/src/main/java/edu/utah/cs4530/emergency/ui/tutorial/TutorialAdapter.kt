package edu.utah.cs4530.emergency.ui.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.dao.TutorialDAO

class TutorialAdapter(private val tutorial: List<TutorialDAO>) : RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        return TutorialViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_tutorial, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tutorial.size
    }

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        holder.bind(tutorial[position])
    }


    inner class TutorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle = view.findViewById<TextView>(R.id.tutorialTitle)
        private val textDescription = view.findViewById<TextView>(R.id.tutorialDescription)
        private val image = view.findViewById<ImageView>(R.id.tutorialImage)

        fun bind(tutorial: TutorialDAO) {
            textTitle.text = tutorial.title
            textDescription.text = tutorial.description
            image.setImageResource(tutorial.screen)
        }
    }


}