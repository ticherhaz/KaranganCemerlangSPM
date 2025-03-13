package net.ticherhaz.karangancemerlangspm.version2025.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.ticherhaz.karangancemerlangspm.databinding.ItemMenuBinding
import net.ticherhaz.karangancemerlangspm.version2025.model.Menu

class MenuAdapter(menuAdapterCallback: MenuAdapterCallback) :
    ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MENU_COMPARATOR) {

    class MenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    private var callback: MenuAdapterCallback = menuAdapterCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position)

        with(holder.binding) {
            tvTitle.text = menu.title
            tvDescription.text = menu.description
            ivIcon.setImageResource(menu.iconId)

            main.setOnClickListener {
                when (menu.menuId) {
                    "1" -> callback.onKCSPMLiteClicked()
                    "2" -> callback.onEssayListClicked()
                    "3" -> callback.onEssayTipsClicked()
                    "4" -> callback.onIdiomClicked()
                    "5" -> callback.onForumClicked()
                    "6" -> callback.onDonationClicked()
                    "7" -> callback.onSettingsClicked()
                }
            }
        }
    }

    companion object {
        val MENU_COMPARATOR = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean =
                oldItem.menuId == newItem.menuId

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean =
                oldItem == newItem
        }
    }

    interface MenuAdapterCallback {
        fun onKCSPMLiteClicked()
        fun onEssayListClicked()
        fun onEssayTipsClicked()
        fun onIdiomClicked()
        fun onForumClicked()
        fun onDonationClicked()
        fun onSettingsClicked()
    }
}