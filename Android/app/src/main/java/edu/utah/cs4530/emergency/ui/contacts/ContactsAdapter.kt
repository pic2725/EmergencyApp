package edu.utah.cs4530.emergency.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.component.picasso.RoundedTransformation
import edu.utah.cs4530.emergency.dao.ContactDAO
import edu.utah.cs4530.emergency.ui.contacts.ContactsAdapter.ItemViewHolder

class ContactsAdapter(private val listData: List<ContactDAO>, private val viewModel: ContactsViewModel) : RecyclerView.Adapter<ItemViewHolder>(), ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacts, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //holder.onBind(listData[position])
        holder.onBind(viewModel.getContactList(position))

    }

    override fun getItemCount(): Int {
        return listData.size
    }




    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val name: TextView = itemView.findViewById(R.id.txt_contacts_name)
        private val number: TextView = itemView.findViewById(R.id.txt_contacts_number)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun onBind(contactDAO: ContactDAO) {
            name.text = contactDAO.name
            number.text = contactDAO.phoneNumber
            Picasso.get().load(contactDAO.photoUri).transform(RoundedTransformation()).into(imageView)
        }

        private val deleteBtn: View = itemView.findViewById(R.id.btn_deleteItem)
        init {
            deleteBtn.setOnClickListener(remove())
        }

        private fun remove(): (View) -> Unit = {

            layoutPosition.also {
                adapterPosition -> viewModel.removeContactList(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun onItemSwipe(position: Int) {
        viewModel.removeContactList(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        //store item which will be moved
        val temp = viewModel.getContactList(from_position)
        //remove item which will be moved
        viewModel.removeContactList(from_position)
        viewModel.addContactList(temp, to_position)

        notifyItemMoved(from_position, to_position)
        return true
    }
}