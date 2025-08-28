package net.ticherhaz.karangancemerlangspm.model.student

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.DefaultValue
import com.huawei.agconnect.cloud.database.annotations.Indexes
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys

@PrimaryKeys("studentId")
@Indexes(value = ["name:name", "email:email"])
class Student : CloudDBZoneObject {
    var studentId: String? = null
    var name: String? = null
    var email: String? = null
    var tempX: String? = null
    var school: String? = null
    var bio: String? = null

    @DefaultValue(stringValue = "")
    var createdDate: String? = null

    // Primary constructor
    constructor() : super(Student::class.java) {
        // Initialize default values
        this.createdDate = ""
    }

    // Secondary constructor
    constructor(
        studentId: String?,
        name: String?,
        email: String?,
        tempX: String?,
        school: String?,
        bio: String?,
        createdDate: String? = ""
    ) : this() {  // Calls primary constructor
        this.studentId = studentId
        this.name = name
        this.email = email
        this.tempX = tempX
        this.school = school
        this.bio = bio
        this.createdDate = createdDate
    }
}