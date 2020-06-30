package com.sam.sabinomeals.models

class TokenModel {
    var phone: String?=null
    var token: String?=null

    constructor(phone: String?, token: String?) {
        this.phone = phone
        this.token = token
    }
}
