package by.slavintodron.mytestappl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter (
    private val persons: List<Person>,
    private val listner: OnItemClickListener
    ):
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>(){

   inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        var ageTextView: TextView? = null
        var genderTextView: TextView? = null
        var nameTextView: TextView? = null
        init {
            ageTextView = itemView.findViewById(R.id.textViewAge)
            genderTextView = itemView.findViewById(R.id.textViewGender)
            nameTextView = itemView.findViewById(R.id.textViewName)

            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position !=RecyclerView.NO_POSITION)
            {
            listner.onItemClick(persons[adapterPosition]._id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rc_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.nameTextView?.text = persons[position].name
            holder.genderTextView?.text = persons[position].gender
            holder.ageTextView?.text = persons[position].age.toString()
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    interface OnItemClickListener {
        fun onItemClick(personId: String)
    }
}