package net.ticherhaz.karangancemerlangspm.version2025.ui.forum

import android.os.Bundle
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ticherhaz.karangancemerlangspm.R
import net.ticherhaz.karangancemerlangspm.databinding.ActivitySignUp2Binding
import net.ticherhaz.karangancemerlangspm.huawei.model.CloudDBZoneWrapper
import net.ticherhaz.karangancemerlangspm.model.student.Student
import java.time.Instant


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var cloudDB: AGConnectCloudDB
    private var cloudDBZone: CloudDBZone? = null
    private val TAG = "CloudDBZoneWrapper"

    private lateinit var binding: ActivitySignUp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ThreadPolicy.Builder().permitAll().build()

        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Cloud Database
        initializeCloudDB()


        val btnSignUp = findViewById<MaterialButton>(R.id.btn_sign_up)
        btnSignUp.setOnClickListener {
            val auth = AGConnectAuth.getInstance()
            auth.signInAnonymously().addOnSuccessListener {
                Log.i("???", "Success")
            }.addOnFailureListener {
                Log.i("???", "Failed: " + it.message)
            }
        }
    }

    private val mCloudDBZoneWrapper: CloudDBZoneWrapper = CloudDBZoneWrapper()

    private fun initializeCloudDB() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                mCloudDBZoneWrapper.createObjectType()
                mCloudDBZoneWrapper.openCloudDBZone()
                mCloudDBZoneWrapper.openCloudDBZoneV2()

                Log.d(TAG, "Cloud DB initialized mCloudDBZoneWrapper")

                // Add a student
                val student = net.ticherhaz.karangancemerlangspm.huawei.model.Student()
                student.studentId = "STU124"
                student.name = "John Doe"
                student.email = "john.doe@example.com"
                student.tempX = "SomeValue"
                student.school = "Example University"
                student.bio = "A passionate student"
                student.createdDate = Instant.now().toString()

                mCloudDBZoneWrapper.upsertBookInfos(student)
                // addStudent(student)

            } catch (e: AGConnectCloudDBException) {
                Log.e(TAG, "Failed to initialize Cloud DB: ${e.message}", e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during initialization: ${e.message}", e)
            }
        }
    }


    private fun addStudent(student: Student) {
        cloudDBZone?.let { zone ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val upsertTask = zone.executeUpsert(student)
                    withContext(Dispatchers.Main) {
                        upsertTask.addOnSuccessListener { result ->
                            Log.d(TAG, "Student added successfully, affected rows: $result")
                        }.addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to add student: ${exception.message}", exception)
                        }
                    }
                } catch (e: AGConnectCloudDBException) {
                    Log.e(TAG, "Error adding student: ${e.message}", e)
                }
            }
        } ?: Log.e(TAG, "Cloud DB Zone is not initialized")
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                mCloudDBZoneWrapper.closeCloudDBZone()
            } catch (e: AGConnectCloudDBException) {
                Log.e(TAG, "Failed to close Cloud DB Zone: ${e.message}", e)
            }
        }
    }
}