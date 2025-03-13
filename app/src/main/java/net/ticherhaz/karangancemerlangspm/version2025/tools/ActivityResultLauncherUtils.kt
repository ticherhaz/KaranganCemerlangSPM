package net.ticherhaz.karangancemerlangspm.version2025.tools

import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

class ActivityResultLauncherUtils(private val componentActivity: ComponentActivity) {

    private var photoTakenPath: String? = null

    private val galleryLauncher = componentActivity.registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        galleryContinuation?.let { cont ->
            galleryContinuation = null
            uri?.let {
                try {
                    cont.resume(Tools.getPath(componentActivity, uri))
                } catch (e: Exception) {
                    cont.resume(null)
                }
            } ?: cont.resume(null)
        }
    }

    private val openDocumentLauncher = componentActivity.registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        openDocumentContinuation?.let { cont ->
            openDocumentContinuation = null
            uri?.let {
                try {
                    cont.resume(Tools.getPath(componentActivity, uri))
                } catch (e: Exception) {
                    cont.resume(null)
                }
            } ?: cont.resume(null)
        }
    }

    private val takePictureLauncher = componentActivity.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        takePictureContinuation?.let { cont ->
            takePictureContinuation = null
            if (success) {
                cont.resume(photoTakenPath)
            } else {
                cont.resume(null)
            }
        }
    }

    @Volatile
    private var galleryContinuation: CancellableContinuation<String?>? = null

    @Volatile
    private var openDocumentContinuation: CancellableContinuation<String?>? = null

    @Volatile
    private var takePictureContinuation: CancellableContinuation<String?>? = null

    suspend fun showGallery(): String? = suspendCancellableCoroutine { cont ->
        galleryContinuation?.cancel()
        galleryContinuation = cont
        cont.invokeOnCancellation { galleryContinuation = null }
        galleryLauncher.launch(PickVisualMediaRequest())
    }

    suspend fun showOpenDocument(): String? = suspendCancellableCoroutine { cont ->
        openDocumentContinuation?.cancel()
        openDocumentContinuation = cont
        cont.invokeOnCancellation { openDocumentContinuation = null }
        openDocumentLauncher.launch(arrayOf("image/*", "application/pdf", "application/msword"))
    }

    suspend fun showTakePicture(): String? = suspendCancellableCoroutine { cont ->
        takePictureContinuation?.cancel()
        takePictureContinuation = cont
        cont.invokeOnCancellation { takePictureContinuation = null }

        try {
            val file = createImageFile()
            val photoUri = FileProvider.getUriForFile(
                componentActivity,
                "net.ticherhaz.karangancemerlangspm.provider",
                file
            )
            photoTakenPath = file.absolutePath
            takePictureLauncher.launch(photoUri)
        } catch (e: Exception) {
            cont.resume(null)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir = File(componentActivity.filesDir, "Karangan Cemerlang SPM").apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Failed to create storage directory")
            }
        }
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}