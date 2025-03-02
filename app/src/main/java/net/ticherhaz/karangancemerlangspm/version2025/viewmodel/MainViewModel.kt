package net.ticherhaz.karangancemerlangspm.version2025.viewmodel

import androidx.lifecycle.ViewModel
import net.ticherhaz.karangancemerlangspm.R
import net.ticherhaz.karangancemerlangspm.version2025.model.Menu

class MainViewModel : ViewModel() {


    fun generateMenu() {
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
            iconId = R.drawable.kcspm_lite
        )
        val menuIdiom = Menu(
            menuId = "3",
            title = "Peribahasa",
            description = "Senarai peribahasa yang sering kita guna.",
            iconId = R.drawable.kcspm_lite
        )
        val menuForum = Menu(
            menuId = "3",
            title = "Forum",
            description = "Dimana semua orang dapat berbincang antara satu sama lain.",
            iconId = R.drawable.kcspm_lite
        )
        val menuDonation = Menu(
            menuId = "3",
            title = "Sumbangan",
            description = "Menyumbang untuk memberi sokong kepada aplikasi ini secara ikhlas.",
            iconId = R.drawable.kcspm_lite
        )

        menuItems.add(menuDownloadKCSPMLite)
        menuItems.add(menuEssayList)
        menuItems.add(menuEssayTips)
        menuItems.add(menuIdiom)
        menuItems.add(menuForum)
        menuItems.add(menuDonation)


    }
}