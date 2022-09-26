package com.example.fetchchallenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

private const val URL_CONNECTION_ADDRESS = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
private const val RESPONSE_LIST_ID_PARAMETER = "listId"
private const val RESPONSE_ID_PARAMETER = "id"
private const val RESPONSE_NAME_PARAMETER = "name"
private const val RESPONSE_NAME_NULL = "null"


class FetchActivity : AppCompatActivity() {

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mRecyclerViewAdapter : RecyclerArrayAdapter

    private var mAllUrlConnectionResponses : MutableList<JsonContentInfo> = mutableListOf()
    private var mListId1Responses : TreeMap<Int, JsonContentInfo> = TreeMap()
    private var mListId2Responses : TreeMap<Int, JsonContentInfo> = TreeMap()
    private var mListId3Responses : TreeMap<Int, JsonContentInfo> = TreeMap()
    private var mListId4Responses : TreeMap<Int, JsonContentInfo> = TreeMap()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.main_recycler_view)
        mRecyclerViewAdapter = RecyclerArrayAdapter(mAllUrlConnectionResponses)
        mRecyclerView.layoutManager = LinearLayoutManager(this@FetchActivity)
        mRecyclerView.adapter = mRecyclerViewAdapter

        openUrlConnection()
    }

    private fun openUrlConnection() {
        val backgroundThread = Thread {
            try {
                val url = URL(URL_CONNECTION_ADDRESS)
                val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
                val bufferedInputStream = BufferedInputStream(urlConnection.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(bufferedInputStream))

                for (lineToParse in bufferedReader.lines()){
                    if (lineToParse == "[" || lineToParse == "]")
                        continue
                    parseJson(lineToParse)
                }
                mAllUrlConnectionResponses.apply {
                    mAllUrlConnectionResponses.addAll(mListId1Responses.values)
                    mAllUrlConnectionResponses.addAll(mListId2Responses.values)
                    mAllUrlConnectionResponses.addAll(mListId3Responses.values)
                    mAllUrlConnectionResponses.addAll(mListId4Responses.values)
                }
                mRecyclerView.post {
                    mRecyclerViewAdapter.notifyDataSetChanged()
                }
                urlConnection.disconnect()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
        backgroundThread.start()
    }

    private fun parseJson(jsonString : String) {
        try {
            val jsonResponseObject = JSONObject(jsonString)

            val listIdObject = jsonResponseObject.getInt(RESPONSE_LIST_ID_PARAMETER)
            val idObject = jsonResponseObject.getInt(RESPONSE_ID_PARAMETER)
            val nameObject = jsonResponseObject.getString(RESPONSE_NAME_PARAMETER)

            if (nameObject.isBlank().or(nameObject.equals(RESPONSE_NAME_NULL)))
                return

            val jsonContentInfo = JsonContentInfo(
                "List ID : $listIdObject",
                idObject,
                "Name : $nameObject"
            )

            when (listIdObject){
                1 -> mListId1Responses[jsonContentInfo.responseId] = jsonContentInfo
                2 -> mListId2Responses[jsonContentInfo.responseId] = jsonContentInfo
                3 -> mListId3Responses[jsonContentInfo.responseId] = jsonContentInfo
                4 -> mListId4Responses[jsonContentInfo.responseId] = jsonContentInfo
            }
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }
}