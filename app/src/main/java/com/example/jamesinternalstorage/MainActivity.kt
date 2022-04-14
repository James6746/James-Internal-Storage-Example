package com.example.jamesinternalstorage

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jamesinternalstorage.databinding.ActivityMainBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            btnSave.setOnClickListener {
                save(etText.text.toString())
                etText.text.clear()
            }

            btnRead.setOnClickListener {
                tvText.text = read()
            }

            btnUpdate.setOnClickListener {
                update(etText.text.toString())
            }

            btnClear.setOnClickListener {
                clear()
            }

            btnDeleteFile.setOnClickListener {
                deleteFile()
            }

            btnSaveImage.setOnClickListener {
                val myBitmapImg = BitmapFactory.decodeResource(
                    this@MainActivity.resources,
                    R.drawable.my_img
                )
                storeImage(myBitmapImg)

                toast(saveToInternalStorage(myBitmapImg)!!)

//                val files: Array<String> = this@MainActivity.fileList()
//                for(a in files){
//                    toast(a)
//                }


            }
        }
    }

    private fun save(text: String) {
        val fOut = this@MainActivity.openFileOutput("myFile.txt", Context.MODE_PRIVATE)
        fOut.write(text.toByteArray())
        fOut.close()
//        val path = this@MainActivity.filesDir.toString()
//                toast(path)
    }

    private fun read(): String {
        val text: String
        this.openFileInput("myFile.txt").use { stream ->
            text = stream.bufferedReader().use {
                it.readText()
            }
        }
        return text
    }

    private fun update(text: String) {
        var previousText = read()
        previousText += " $text"
        save(previousText)
    }

    private fun clear() {
        save("")
    }

    private fun deleteFile(): Boolean {
        val dir: File = filesDir
        val file = File(dir, "myFile.txt")
        return file.delete()
    }


    /*
    * This method creates a folder in internal storage, and then saves the image*/
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {

        val cw = ContextWrapper(baseContext)
        val directory = cw.getDir("images", MODE_PRIVATE)

//
//        directory.list().forEach { a ->
//            toast(a.toString())
//        }

        val myPath = File(directory, "profile.png")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
//            fos.write("James".toByteArray())
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 10, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return directory.absolutePath
    }

    /*THis method saves the image into a "files" folder*/
    private fun storeImage(image: Bitmap) {
        try {
            val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
            val mImageName = "MI_$timeStamp.png"
            val fos = openFileOutput(mImageName, MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.PNG, 10, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
//            val img: ImageView = findViewById<View>(R.id.imgPicker) as ImageView
//            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    fun Activity.toast(msg: String) {
        Toast.makeText(application, msg, Toast.LENGTH_LONG).show()
    }
}