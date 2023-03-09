package com.shiva.bot
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class FullscreenActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var messageRvAdapter: MessageRvAdapter
    private lateinit var messageList: ArrayList<MessageRvModel>
    var url = "https://api.openai.com/v1/completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        recyclerView = findViewById(R.id.recycler_view)
        messageEditText = findViewById(R.id.message_edit_text)
        messageList = ArrayList()
        messageRvAdapter = MessageRvAdapter(messageList)

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = messageRvAdapter

        messageEditText.setOnEditorActionListener(TextView.OnEditorActionListener{ textView, i ,   KeyEvent->

            if (i==EditorInfo.IME_ACTION_SEND) {
                if(messageEditText.text.toString().length>0) {
                    messageList.add(MessageRvModel(messageEditText.text.toString() , "user"))
                    messageRvAdapter.notifyDataSetChanged()
                    getResponse(messageEditText.text.toString())
                }else {
                    Toast.makeText(this , "Please enter some text." , Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(Edtext: String) {
        messageEditText.setText("")
        val text : RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject : JSONObject = JSONObject()
        jsonObject.put("model" , "text-davinci-003" )
        jsonObject.put( "prompt", Edtext )
        jsonObject.put("temperature" , "0" )
        jsonObject.put("max_tokens" , "100" )
        jsonObject.put("top_p" , "1" )
        jsonObject.put("frequency_penalty" , "0.0" )
        jsonObject.put("presence_penalty" , "0.0" )
        val postRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener {response ->

        val responseMsg : String = response.getJSONArray("choices").getJSONObject(0).getString("text")
            messageList.add(MessageRvModel(responseMsg, "bot"))
            messageRvAdapter.notifyDataSetChanged()
        } , Response.ErrorListener {
            Toast.makeText(applicationContext , "Failed to get response", Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String , String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }

        }

        postRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {
            }
        })
        text.add(postRequest)
    }
}
