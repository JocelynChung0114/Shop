package com.toplogis.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parking.*
import kotlinx.android.synthetic.main.row_parking.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class ParkingActivity : AppCompatActivity(), AnkoLogger {
    var parking : Parking? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("http://data.tycg.gov.tw/opendata/datalist/datasetMeta/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)

        val parkingService = retrofit.create(ParkingService::class.java)

        recycle_parking.layoutManager = LinearLayoutManager(this)
        recycle_parking.setHasFixedSize(true)
        recycle_parking.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        val parking = "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/"

        doAsync {
            parking = parkingService.listParking()
                .execute()
                .body()

            uiThread {
                recycle_parking.adapter = parkingAdapter()
            }
//            val url = URL(parking)
//            val json = url.readText()
//            info(json)
//            uiThread {
//                tv_info.text = json
//                parseGson(json)
//            }
        }

    }

    inner class parkingAdapter() : RecyclerView.Adapter<ParkingHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_parking, parent, false)
            return  ParkingHolder(view)
        }

        override fun getItemCount(): Int {
            return  parking?.parkingLots?.size?:0
        }

        override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
            holder.tvName.text = parking!!.parkingLots.get(position).parkName
            holder.tvTotal.text = parking!!.parkingLots.get(position).totalSpace.toString()
            holder.tvSurplus.text = parking!!.parkingLots.get(position).surplusSpace
            holder.tvAddress.text = parking!!.parkingLots.get(position).address
        }

    }

    inner class ParkingHolder(view:View) : RecyclerView.ViewHolder(view) {
        val tvName = view.tv_name
        val tvTotal = view.tv_total
        val tvSurplus = view.tv_surplus
        val tvAddress = view.tv_address
    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        info(parking.parkingLots.size)
        parking.parkingLots.forEach {
            info("${it.areaId}, ${it.areaName}, ${it.totalSpace}")
        }
    }

    data class Parking(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)
    interface ParkingService {
        @GET("download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f")
        fun listParking(): Call<Parking>
    }
}
