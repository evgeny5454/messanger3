package com.evgeny_m.messenger3.model

data class CommonModel (
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var userfullname: String = "",
    var state: String = "",
    var photoUrl: String = "empty",
    var phone: String = ""
)
