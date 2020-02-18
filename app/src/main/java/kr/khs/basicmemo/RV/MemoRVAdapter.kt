package kr.khs.basicmemo.RV

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.khs.basicmemo.Activity.WriteActivity
import kr.khs.basicmemo.R

const val RESULT_SAVE = 1
const val RESULT_DELETE = 2

class MemoRVAdapter(private val data : MutableList<MemoData>, private val a : Activity) :
    RecyclerView.Adapter<MemoRVAdapter.MyViewHolder>() {
    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val dateTv = view.findViewById<TextView>(R.id.rv_date)
        val mainTv = view.findViewById<TextView>(R.id.rv_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRVAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MemoRVAdapter.MyViewHolder, position: Int) {
        val myviewholder = holder as MyViewHolder
        myviewholder.dateTv.text = data[position].date
        myviewholder.mainTv.text = data[position].text

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val tempSeq = data[position].seq
            val tempDate = data[position].date
            val tempText = data[position].text
            val intent = Intent(context, WriteActivity::class.java).apply {
                putExtra("seq", tempSeq)
                putExtra("date", tempDate)
                putExtra("text", tempText)
            }
            a.startActivityForResult(intent, RESULT_SAVE)
        }
    }
}