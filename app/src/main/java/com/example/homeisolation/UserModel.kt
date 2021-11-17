package com.example.homeisolation

class UserModel {
    var uid =""
    var email=""
    var name=""
    var address=""
    var phone =""

    constructor(uid: String?, email: String?, name: String, address: String, phone: String) {
        if (uid != null) {
            this.uid = uid
        }
        if (email != null) {
            this.email = email
        }
        this.name = name
        this.address = address
        this.phone = phone
    }
}