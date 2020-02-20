package ittepic.edu.mx.ladm_u1_p3_brayanenrique

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var vector = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //PERMISO NO CONCEDIDO, ENTONCES SE UTILIZA
            //Los permisos se deben solicitar en el manifest
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            mensaje("Los permisos ya fueron otorgados!!")
        }//todo :======================PERMISOS================>


        btnInsertar.setOnClickListener {
            if (txtValor.text.isEmpty() || txtPosicion.text.isEmpty()) {

                Toast.makeText(this, "los campos estan vacios", Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }//todo:de vacio
            var valor = txtValor.text.toString()
            val posicion: Int = txtPosicion.text.toString().toInt() - 1
            vector.add(posicion, valor);
            Toast.makeText(this, "se capturo el valor ", Toast.LENGTH_LONG).show()


        }//todo:-----> insertar arreglo de listas
        btnMostrar.setOnClickListener {
            mostrarLista()

        }//todo:-----> mostrar arreglo de listas

        btnGuardar.setOnClickListener {
            guardarArchivoSD()
        }//todo:guardar la lista ================================>


        btnLeer.setOnClickListener {
            Toast.makeText(this, "se visualizo el archivo ", Toast.LENGTH_LONG).show()
            leerArchivoSD()
        }//todo:guardar la lista ================================>
    }


    fun mostrarLista() {
        var total = vector.size - 1
        var datosVector: Array<String> = Array(vector.size, { "" })
        (0..total).forEach {
            datosVector[it] = vector[it]
            datosVector[it] = datosVector[it].replace("&", "\n");
        }//todo:=======> foreach
        var adaptadorListView =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datosVector)
        lista.adapter = adaptadorListView


    }


    private fun guardarArchivoInterno() {
        try {
            var flujoSalida =
                OutputStreamWriter(openFileOutput(txtGuardar.text.toString(), Context.MODE_PRIVATE))
            var data = vector.size - 1
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }//todo : -------> guardar archivo interno POR SI ACASO


    fun guardarArchivoSD() {
        if (noSD()) {
            mensaje("No hay memoria")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtGuardar.text.toString())
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = ""
            (0..9).forEach {
                data += vector[it]


            }
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }// todo :------> aguardarArchivo en la SD


    fun leerArchivoSD() {
        if (noSD()) {
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtLeer.text.toString())
            var flujoEnntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEnntrada.readLine()

            var vector2 = ArrayList<String>()
            var total = vector2.size - 1
            vector2.add(data)
            var datosVector: Array<String> = Array(vector2.size, { "" })
            (0..total).forEach {
                datosVector[it] = vector2[it]
                datosVector[it] = datosVector[it].replace("2", "\n");


            }

            mensaje(vector.toString() + "vector normal")

            mensaje(vector2.toString() + "vector2")


        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }

    //TODO==================================METODOS DE AYUDA======================================>
    fun noSD(): Boolean {
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED) {
            return true
        } else {
            return false
        }
    }//TODO: verificiar la memoria-------------------------->


    fun mensaje(m: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(m)
            .setPositiveButton("ACEPTAR") { d, i ->
            }.show()

    }//todo:mensaje===>

    fun asignarText(t1: String) {
        txtValor.setText(t1)
        Toast.makeText(this, "el arreglo es : " + t1, Toast.LENGTH_LONG).show()

    }//todo:metodo para asignar mensajes---->


    fun asignarLista(t1: ArrayList<String>) {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, t1)
        lista.adapter
    }


}
