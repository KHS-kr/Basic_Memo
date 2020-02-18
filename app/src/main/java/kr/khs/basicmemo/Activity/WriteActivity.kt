package kr.khs.basicmemo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_write.*
import kr.khs.basicmemo.R
import kr.khs.basicmemo.RV.RESULT_DELETE
import kr.khs.basicmemo.RV.RESULT_SAVE
import java.text.SimpleDateFormat
import java.util.*


class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val intentGetDate = intent.getStringExtra("date")
        val intentGetText = intent.getStringExtra("text")
        val intentGetSeq = intent.getIntExtra("seq", -1)
        val currentTime: String
        if(intentGetSeq == -1) {
            currentTime = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date())
        }
        else {
            currentTime = intentGetDate
            write_memo.text = Editable.Factory.getInstance().newEditable(intentGetText)
            write_remove.visibility = View.VISIBLE
        }

        write_date.text = currentTime

        write_save.setOnClickListener {
            if(write_memo.text.toString().isBlank()) {
                Toast.makeText(applicationContext, "메모를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val saveIntent = Intent().apply {
                putExtra("save_date", currentTime)
                putExtra("save_text", write_memo.text.toString())
                putExtra("save_seq", intentGetSeq)
            }
            setResult(RESULT_SAVE, saveIntent)
            finish()
        }

        //recyclerview에서 눌러서 가져왔을 경우에만 활성화
        write_remove.setOnClickListener {
            val deleteIntent = Intent().apply {
                putExtra("delete_seq", intentGetSeq)
            }
            setResult(RESULT_DELETE, deleteIntent)
            finish()
        }
    }
}
