package com.olegmcnamara.braverats.model

import java.util.*

class User {
    var id: String = ""
    var userName: String = ""
    var email: String = ""

    constructor()

    constructor(id: String,
                userName: String,
                email: String) {

        this.id = id
        this.userName = userName
        this.email = email
    }

    override fun equals(other: Any?): Boolean {
        return if (other is User) {
            other.id == this.id
        } else {
            false
        }
    }

}