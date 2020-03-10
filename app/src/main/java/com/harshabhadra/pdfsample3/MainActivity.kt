package com.harshabhadra.pdfsample3

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.PageSize.A4
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestReadPermissions()
        val editText:EditText = findViewById(R.id.editText)
        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            savePdf()
        }
    }

    fun savePdf(){
        val mDoc = Document(A4,8f,8f,8f,8f)
        val folder = File(Environment.getExternalStorageDirectory(),"PdfSample1")
        Log.e("MainActivity",folder.absolutePath)
        var success = true
        if(!folder.exists()){
            success = folder.mkdirs()
            Toast.makeText(this, "Folder created",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Folder exists",Toast.LENGTH_SHORT).show()
        }
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault())
                .format(System.currentTimeMillis())
        val filePath =folder.absolutePath + "/" + mFileName + ".pdf"
        try {
            PdfWriter.getInstance(mDoc,FileOutputStream(filePath))
            mDoc.open()
            val lineSeparator = LineSeparator()
            lineSeparator.lineColor = BaseColor.BLACK

            val mChunk = Chunk("Harsha Bhadra", Font(Font.FontFamily.TIMES_ROMAN,32.0f))
            val title = Paragraph(mChunk)
            title.alignment = Element.ALIGN_CENTER

            val mText = editText.text.toString()
            mDoc.add(title)
            mDoc.addAuthor("Harsha Bhadra")
            mDoc.addTitle("Harsha Bhadra")
            mDoc.add(Chunk(lineSeparator))
            mDoc.add(Paragraph(mText))
            mDoc.add(Paragraph(mText))

            mDoc.close()
            Toast.makeText(this, "Pdf Created at $mFileName.pdf at $filePath",Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            Log.e("MainActivity",e.message)
        }
    }
    private fun requestReadPermissions() {

        Dexter.withActivity(this)
                .withPermissions( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(applicationContext, "All permissions are granted by user!", Toast.LENGTH_SHORT)
                                    .show()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener(object : PermissionRequestErrorListener {
                    override fun onError(error: DexterError) {
                        Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
                    }
                })
                .onSameThread()
                .check()
    }
}
