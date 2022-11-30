package com.example.projectlfg

class UserInformation {

    // class instead of data for firebase operation

    var name: String? = ""
    var email: String? = ""
    var imageuri: String? = ""
    var friendID: String? = ""
    var friendList: ArrayList<String>? = null

    constructor()

    constructor(name: String, email: String, uri: String) {
        this.name = name
        this.email = email
        this.imageuri = uri
        this.friendID = ""
    }

}
