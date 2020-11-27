package edu.utah.cs4530.emergency.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.abstract.LiveModelFragment
import edu.utah.cs4530.emergency.extension.getLogger
import kotlinx.android.synthetic.main.fragment_history.view.*

class ReceivedHistoryFragment: LiveModelFragment<ReceivedHistoryViewModel>(ReceivedHistoryViewModel::class, R.layout.fragment_history)
{
    private val logger by getLogger()

    override fun onCreateViewModel(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        root.historyRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        viewModel.histories.observe(this, Observer {
            root.historyRecyclerView.adapter = ReceivedHistoryAdaptor(it)
        })
    }
}