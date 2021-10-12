package com.evgeny_m.messenger3.model

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var userfullname: String = "",
    var status: String = "",
    var photoUrl: String = "empty",
    var phone: String = "",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var time_stamp: Any = ""
)
