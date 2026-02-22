package com.example.isgreporterpro.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.isgreporterpro.data.AccidentEntity
import com.example.isgreporterpro.data.NearMissEntity
import com.example.isgreporterpro.data.ObservationEntity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PdfGenerator {

    // FOTOĞRAFI PDF'E ÇİZEN ÖZEL FONKSİYON
    private fun drawImageOnCanvas(context: Context, canvas: Canvas, imageUriString: String?, startY: Float): Float {
        if (imageUriString == null) return startY
        try {
            val uri = Uri.parse(imageUriString)
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (originalBitmap != null) {
                // Fotoğrafı A4 sayfasına sığacak şekilde küçült
                val maxWidth = 350f
                val scale = maxWidth / originalBitmap.width
                val newWidth = (originalBitmap.width * scale).toInt()
                val newHeight = (originalBitmap.height * scale).toInt()

                val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

                // Sayfayı ortalayarak çiz
                val left = (595f - newWidth) / 2f
                canvas.drawBitmap(scaledBitmap, left, startY, null)
                return startY + newHeight + 20f // Yeni Y pozisyonunu döndür
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return startY
    }

    fun generateNearMissPdf(context: Context, report: NearMissEntity) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.BLACK
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("RAMAK KALA RAPORU", 180f, 80f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        var yPos = 140f
        val lineSpacing = 35f

        canvas.drawText("Rapor ID: ${report.id}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Olay Başlığı: ${report.title}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Tarih ve Saat: ${report.auditTime}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Olay Yeri: ${report.location}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Tanık: ${report.witness}", 50f, yPos, paint); yPos += lineSpacing
        val status = if (report.isReported) "Evet (Bildirildi)" else "Hayır (Bildirilmedi)"
        canvas.drawText("Yönetime Bildirildi mi: $status", 50f, yPos, paint); yPos += lineSpacing

        // Fotoğrafı çiz ve yPos'u güncelle
        drawImageOnCanvas(context, canvas, report.imageUri, yPos + 20f)

        document.finishPage(page)
        saveDocument(context, document, "Ramak_Kala_Raporu_${report.id}.pdf")
    }

    fun generateAccidentPdf(context: Context, report: AccidentEntity) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.RED
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("İŞ KAZASI RAPORU", 180f, 80f, paint)

        paint.color = Color.BLACK
        paint.textSize = 14f
        paint.isFakeBoldText = false
        var yPos = 140f
        val lineSpacing = 35f

        canvas.drawText("Kaza Başlığı: ${report.reportTitle}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Tarih ve Saat: ${report.dateAndTime}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Olay Yeri: ${report.location}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Yaralanan Personel: ${report.injuredStaffName}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Personel Statüsü: ${report.staffStatus}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Kaza Tipi: ${report.accidentType}", 50f, yPos, paint); yPos += lineSpacing

        drawImageOnCanvas(context, canvas, report.imageUri, yPos + 20f)

        document.finishPage(page)
        saveDocument(context, document, "Is_Kazasi_Raporu_${report.id}.pdf")
    }

    fun generateObservationPdf(context: Context, report: ObservationEntity) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.BLUE
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("SAHA GÖZLEM RAPORU", 170f, 80f, paint)

        paint.color = Color.BLACK
        paint.textSize = 14f
        paint.isFakeBoldText = false
        var yPos = 140f
        val lineSpacing = 35f

        canvas.drawText("Gözlem Başlığı: ${report.title}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Tarih ve Saat: ${report.dateAndTime}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Konum: ${report.location}", 50f, yPos, paint); yPos += lineSpacing
        canvas.drawText("Risk Seviyesi: ${report.riskLevel}", 50f, yPos, paint); yPos += lineSpacing

        canvas.drawText("Gözlem Detayı:", 50f, yPos, paint); yPos += 25f
        canvas.drawText(report.detail.take(80), 50f, yPos, paint); yPos += lineSpacing

        drawImageOnCanvas(context, canvas, report.imageUri, yPos + 20f)

        document.finishPage(page)
        saveDocument(context, document, "Saha_Gozlem_Raporu_${report.id}.pdf")
    }

    private fun saveDocument(context: Context, document: PdfDocument, fileName: String) {
        try {
            var outputStream: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/ISG_Raporlari")
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    outputStream = resolver.openOutputStream(uri)
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val isgDir = File(downloadsDir, "ISG_Raporlari")
                if (!isgDir.exists()) isgDir.mkdirs()
                val file = File(isgDir, fileName)
                outputStream = FileOutputStream(file)
            }

            if (outputStream != null) {
                document.writeTo(outputStream)
                outputStream.close()
                Toast.makeText(context, "PDF İndirildi: İndirilenler / ISG_Raporlari", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Hata: Dosya oluşturulamadı.", Toast.LENGTH_SHORT).show()
            }

            document.close()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "PDF Hatası: ${e.message}", Toast.LENGTH_LONG).show()
            document.close()
        }
    }
}