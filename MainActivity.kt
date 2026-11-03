package com.vrsdiario.app

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.os.Build
import android.content.Intent
import android.provider.Settings
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationManagerCompat

class MainActivity : Activity() {

    private lateinit var texto: TextView
    private lateinit var referencia: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        montarTela()
        solicitarPermissoes()
        atualizarVersiculo()
    }

    override fun onResume() {
        super.onResume()

        if (::texto.isInitialized) {
            solicitarAlarmes()
            AlarmScheduler.programar(this)
        }
    }

    private fun montarTela() {

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.WHITE)
        }

        val titulo = TextView(this).apply {
            text = "Versículo do Dia"
            textSize = 28f
            setTextColor(Color.rgb(40, 80, 60))
            gravity = Gravity.CENTER
        }

        texto = TextView(this).apply {
            textSize = 27f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(10, 50, 10, 30)
        }

        referencia = TextView(this).apply {
            textSize = 20f
            setTextColor(Color.rgb(40, 100, 70))
            gravity = Gravity.CENTER
        }

        val subtitulo = TextView(this).apply {
            text = "\nVersículos disponíveis"
            textSize = 20f
            setTextColor(Color.DKGRAY)
        }

        val lista = ListView(this)

        lista.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Versiculos.lista.map {
                it.referencia
            }
        )

        lista.setOnItemClickListener { _, _, position, _ ->

            val item = Versiculos.lista[position]

            texto.text = item.texto
            referencia.text = item.referencia
        }

        layout.addView(titulo)
        layout.addView(texto)
        layout.addView(referencia)
        layout.addView(subtitulo)

        layout.addView(
            lista,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(layout)
    }

    private fun atualizarVersiculo() {

        val prefs = getSharedPreferences(
            "versiculos",
            MODE_PRIVATE
        )

        val textoIntent = intent.getStringExtra("texto")
        val referenciaIntent = intent.getStringExtra("referencia")

        texto.text = textoIntent
            ?: prefs.getString("ultimo_texto", null)
            ?: Versiculos.lista[0].texto

        referencia.text = referenciaIntent
            ?: prefs.getString("ultima_referencia", null)
            ?: Versiculos.lista[0].referencia
    }

    private fun solicitarPermissoes() {

        if (Build.VERSION.SDK_INT >= 33) {

            if (checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        solicitarAlarmes()
    }

    private fun solicitarAlarmes() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val alarmManager =
                getSystemService(ALARM_SERVICE) as android.app.AlarmManager

            if (!alarmManager.canScheduleExactAlarms()) {

                startActivity(
                    Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    )
                )
            }
        }
    }
}
