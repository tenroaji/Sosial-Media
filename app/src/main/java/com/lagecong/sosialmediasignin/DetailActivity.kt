package com.lagecong.sosialmediasignin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_activity.*


class DetailActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        var data = intent.getSerializableExtra("data") as ModelData
        Picasso.get().load(data.image).into(imageView)
        tvEmail.text = data.email
        tvNama.text = data.nama


        btnLogOut.setOnClickListener {
        }

        btnWhatsapp.setOnClickListener {
//            var toNumber = "+6285342710769" // contains spaces.
//            toNumber = toNumber.replace("+", "").replace(" ", "")
//            val message = "lapar"
//            val sendIntent = Intent("android.intent.action.MAIN")
//            sendIntent.putExtra("jid", "$toNumber@s.whatsapp.net")
//            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
//            sendIntent.action = Intent.ACTION_SEND
//            sendIntent.setPackage("com.whatsapp")
//            sendIntent.type = "text/plain"
//            startActivity(sendIntent)


            try {
                val whatsAppRoot = "http://api.whatsapp.com/"
                val number =
                    "send?phone=+6285342710769" //here the mobile number with its international prefix
                val text = "&text=HERE YOUR TEXT"
                val uri = whatsAppRoot + number + text

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(uri)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "WhatsApp cannot be opened", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
