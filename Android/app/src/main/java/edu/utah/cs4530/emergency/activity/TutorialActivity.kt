package edu.utah.cs4530.emergency.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import edu.utah.cs4530.emergency.EmergencyApplication.Companion.context
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.dao.TutorialDAO
import edu.utah.cs4530.emergency.ui.tutorial.TutorialAdapter
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity: AppCompatActivity()
{
    private val tutorialAdapter = TutorialAdapter(
        listOf(
            TutorialDAO(
                title = "In the simplest way",
                description = " You can send an Emergency message in the simplest way by swiping the SOS button",
                screen = R.drawable.home_screen
            ),
            TutorialDAO(
                title = "Create your Contact List",
                description = "You can create and edit your contact list",
                screen = R.drawable.contact_screen
            ),
            TutorialDAO(
                title = "Edit your Emergency Message",
                description = "You can Edit your own Emergency Message",
                screen = R.drawable.edit_message_screen
            ),
            TutorialDAO(
                title = "Check Your History",
                description = "You can check your history of Emergency message that were sent to you",
                screen = R.drawable.history_screen
            ),
            TutorialDAO(
                title = "More details",
                description = "You can check more details, where your Emergency happened and the people you contacted",
                screen = R.drawable.history_detail_screen
            )

        )


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        tutorialViewPager.adapter = tutorialAdapter
        setupIndicators()
        setCurrentIndicator(0)
        tutorialViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })


        btn_tutorialNext.setOnClickListener {
            if(tutorialViewPager.currentItem + 1 < tutorialAdapter.itemCount) {
                tutorialViewPager.currentItem += 1
            }
            else {
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

        textSkipIntro.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }

    private fun setupIndicators() {

        val indicators = arrayOfNulls<ImageView>(tutorialAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        layoutParams.setMargins(8,0,8,0)

        for(i in indicators.indices) {

//            indicators[i] = ImageView(applicationContext)
            indicators[i] = ImageView( context.applicationContext)

            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext, R.drawable.indicator_inactive
                    )
                )

                this?.layoutParams = layoutParams
            }

            tutorialIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {

        val childCount = tutorialIndicators.childCount

        for(i in 0 until childCount) {

            val imageView = tutorialIndicators[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext, R.drawable.indicator_active
                    )
                )
            }
            else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }




}