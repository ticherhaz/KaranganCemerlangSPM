package net.ticherhaz.karangancemerlangspm.huawei.model

/**
 * Constants to mark book info field
 */
interface BookEditFields {
    /**
     * To mark it's in add mode or in modify mode
     */
    enum class EditMode {
        ADD, MODIFY
    }

    companion object {
        const val BOOK_ID = "id"
        const val BOOK_NAME = "bookName"
        const val AUTHOR = "author"
        const val PRICE = "price"
        const val SHADOW_FLAG = "shadowFlag"
        const val LOWEST_PRICE = "lowest_price"
        const val HIGHEST_PRICE = "highest_price"
        const val PUBLISHER = "publisher"
        const val PUBLISH_TIME = "publishTime"
        const val SHOW_COUNT = "showCount"
        const val EDIT_MODE = "EDIT_MODE"

        const val BOOK_NAME_OR = "bookNameOr"
        const val AUTHOR_OR = "authorOr"
    }
}
