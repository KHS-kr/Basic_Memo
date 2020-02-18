package kr.khs.basicmemo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kr.khs.basicmemo.Database.DBHelper
import kr.khs.basicmemo.R
import kr.khs.basicmemo.RV.MemoData
import kr.khs.basicmemo.RV.MemoRVAdapter
import kr.khs.basicmemo.RV.RESULT_DELETE
import kr.khs.basicmemo.RV.RESULT_SAVE



class MainActivity : AppCompatActivity() {
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
    private lateinit var rvManager: RecyclerView.LayoutManager
    private var memoData = mutableListOf<MemoData>()
    private var dbHelper : DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        memoData = dbHelper!!.getMemo()

        main_fab.setOnClickListener {
            val i1 = Intent(this, WriteActivity::class.java)
            startActivityForResult(i1, RESULT_SAVE)
        }

        rvManager = LinearLayoutManager(this)
        rvAdapter = MemoRVAdapter(memoData, this)
        main_recycler.apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = rvAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_SAVE) {
            val savedate = data!!.getStringExtra("save_date")!!
            val savetext = data!!.getStringExtra("save_text")!!
            val saveseq = data!!.getIntExtra("save_seq", 0)
            val saveMemo = MemoData(saveseq, savedate, savetext)
            val ret = if(saveseq == -1)
                dbHelper!!.addMemo(saveMemo)
            else
                dbHelper!!.updateMemo(saveMemo)

            if(ret) {
                runOnUiThread {
                    memoData = dbHelper!!.getMemo()
                    rvAdapter = MemoRVAdapter(memoData, this)
                    main_recycler.adapter = rvAdapter
                    rvAdapter.notifyDataSetChanged()
                }
                Toast.makeText(applicationContext, "메모 저장 완료", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(applicationContext, "메모 저장 실패", Toast.LENGTH_SHORT).show()
        }
        else if(resultCode == RESULT_DELETE) {
            val deleteseq = data!!.getIntExtra("delete_seq", 0)
            val ret = dbHelper!!.deleteMemo(MemoData(deleteseq, "", ""))

            if(ret) {
                runOnUiThread {
                    memoData = dbHelper!!.getMemo()
                    rvAdapter = MemoRVAdapter(memoData, this)
                    main_recycler.adapter = rvAdapter
                    rvAdapter.notifyDataSetChanged()
                }
                Toast.makeText(applicationContext, "메모 삭제 성공", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(applicationContext, "메모 삭제 성공", Toast.LENGTH_SHORT).show()
        }
    }
}