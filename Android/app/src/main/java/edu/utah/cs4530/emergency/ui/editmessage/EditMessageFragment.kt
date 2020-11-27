package edu.utah.cs4530.emergency.ui.editmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.abstract.LiveModelFragment
import kotlinx.android.synthetic.main.fragment_edit_message.*
import kotlinx.android.synthetic.main.fragment_edit_message.view.*
import kotlinx.android.synthetic.main.fragment_edit_message.view.btn_save


class EditMessageFragment : LiveModelFragment<EditMessageViewModel>(EditMessageViewModel::class, R.layout.fragment_edit_message) {

    override fun onCreateViewModel(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    {
        viewModel.emergencyMessage.observe(this, Observer {
            root.txt_message.setText(it)
        })

        root.btn_save.setOnClickListener {
            viewModel.saveEmergencyMessage(root.txt_message.text.toString())
        }
    }
}


