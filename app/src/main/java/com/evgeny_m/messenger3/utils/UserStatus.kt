package com.evgeny_m.messenger3.utils

enum class UserStatus (val status: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("...is typing");

    companion object {

        fun updateUserStatus(userStatus: UserStatus) {
            if (auth.currentUser != null) {
                database
                    .child(NODE_USERS)
                    .child(currentUserId)
                    .child(CHILD_STATUS)
                    .setValue(userStatus.status)
            }
        }
    }
}