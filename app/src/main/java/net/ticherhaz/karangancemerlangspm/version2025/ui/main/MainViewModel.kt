package net.ticherhaz.karangancemerlangspm.version2025.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import net.ticherhaz.karangancemerlangspm.R
import net.ticherhaz.karangancemerlangspm.version2025.enumerator.PermissionNotificationCondition
import net.ticherhaz.karangancemerlangspm.version2025.model.Menu
import net.ticherhaz.karangancemerlangspm.version2025.model.System
import net.ticherhaz.karangancemerlangspm.version2025.repository.IoDispatcher
import net.ticherhaz.karangancemerlangspm.version2025.retrofit.Resource
import net.ticherhaz.karangancemerlangspm.version2025.tools.Constant
import net.ticherhaz.karangancemerlangspm.version2025.tools.QuickSave
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val quickSave: QuickSave,
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _menuState = MutableStateFlow<Resource<MutableList<Menu>>>(Resource.Initialize())
    val menuState = _menuState.asStateFlow()

    private val _announcementState = MutableStateFlow<Resource<String>>(Resource.Initialize())
    val announcementState = _announcementState.asStateFlow()

    private val _appVersionState = MutableSharedFlow<Resource<System>>()
    val appVersionState = _appVersionState.asSharedFlow()

    private val _isPermissionNotificationGranted =
        MutableSharedFlow<Resource<PermissionNotificationCondition>>()
    val isPermissionNotificationGranted = _isPermissionNotificationGranted.asSharedFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Log.e(TAG, throwable.message + "")
    }

    init {
        initMenuState()
        initAnnouncementState()
        initAppVersionState()
    }

    private fun initAnnouncementState() {
        viewModelScope.launch {
            _announcementState.emit(Resource.Loading())
            try {
                val data = withContext(ioDispatcher + coroutineExceptionHandler) {
                    val snapshot = firebaseDatabase.reference.child("announcement").get().await()
                    val announcement = snapshot.getValue(String::class.java)
                    announcement.toString()
                }

                _announcementState.emit(Resource.Success(data))
            } catch (e: Exception) {
                _announcementState.emit(Resource.Error("Announcement Error: ${e.message}"))
            }
        }
    }

    private fun initAppVersionState() {
        viewModelScope.launch {
            _appVersionState.emit(Resource.Loading())
            try {
                val data = withContext(ioDispatcher + coroutineExceptionHandler) {
                    val snapshot = firebaseDatabase.reference.child("system").get().await()
                    val systemApp = snapshot.getValue(System::class.java)
                    systemApp
                }
                _appVersionState.emit(Resource.Success(data))

            } catch (e: Exception) {
                _appVersionState.emit(Resource.Error("App Version Error: ${e.message}"))
            }
        }
    }

    fun initIsPermissionNotificationGranted(permissionNotificationCondition: PermissionNotificationCondition) {
        viewModelScope.launch {
            _isPermissionNotificationGranted.emit(Resource.Loading())
            try {

                withContext(ioDispatcher + coroutineExceptionHandler) {
                    quickSave.save(
                        Constant.IS_PERMISSION_NOTIFICATION_GRANTED,
                        permissionNotificationCondition.name
                    )
                }

                _isPermissionNotificationGranted.emit(Resource.Success(data = permissionNotificationCondition))
            } catch (e: Exception) {
                _isPermissionNotificationGranted.emit(Resource.Error("${e.message}"))
            }
        }
    }

    private fun initMenuState() {
        viewModelScope.launch {
            _menuState.emit(Resource.Loading())
            try {
                val data = withContext(ioDispatcher + coroutineExceptionHandler) {
                    generateMenuList()
                }

                _menuState.emit(Resource.Success(data))
            } catch (e: Exception) {
                _menuState.emit(Resource.Error("Menu Error: ${e.message}"))
            }
        }
    }

    private fun generateMenuList(): MutableList<Menu> {
        val menuItems: MutableList<Menu> = mutableListOf()

        val menuDownloadKCSPMLite = Menu(
            menuId = "1",
            title = "Muat Turun KCSPM Lite!",
            description = "Sebuah aplikasi yang ringan berbanding aplikasi ini.",
            iconId = R.drawable.ic_baseline_download_24
        )
        val menuEssayList = Menu(
            menuId = "2",
            title = "Senarai Karangan",
            description = "Menyenaraikan semua jenis karangan.",
            iconId = R.drawable.ic_essay_24dp
        )
        val menuEssayTips = Menu(
            menuId = "3",
            title = "Tips Mengarang",
            description = "Saya menyediakan tips bagaimana untuk mengarang dengan cemerlang.",
            iconId = R.drawable.ic_tips_24dp
        )
        val menuIdiom = Menu(
            menuId = "4",
            title = "Peribahasa",
            description = "Senarai peribahasa yang sering kita guna.",
            iconId = R.drawable.ic_idiom_24dp
        )
        val menuForum = Menu(
            menuId = "5",
            title = "Forum",
            description = "Dimana semua orang dapat berbincang antara satu sama lain.",
            iconId = R.drawable.ic_forum_24dp
        )
        val menuDonation = Menu(
            menuId = "6",
            title = "Sumbangan",
            description = "Menyumbang untuk memberi sokong kepada aplikasi ini secara ikhlas.",
            iconId = R.drawable.ic_donation_24dp
        )
        val menuSettings = Menu(
            menuId = "7",
            title = "Tetapan",
            description = "Tetapan untuk aplikasi ini.",
            iconId = R.drawable.ic_setting
        )

        menuItems.add(menuDownloadKCSPMLite)
        menuItems.add(menuEssayList)
        menuItems.add(menuEssayTips)
        menuItems.add(menuIdiom)
        menuItems.add(menuForum)
        menuItems.add(menuDonation)
        menuItems.add(menuSettings)

        return menuItems
    }
}