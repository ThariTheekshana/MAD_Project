package com.example.mymad1.models

data class PaymentModel(
    var PaymentId:String? =null,
    var CardNumber:String? = null,
    var CardHolderName:String? = null,
    var ExpireMonth:String? = null,
    var ExpireYear:String? = null,
    var Cvv:String? = null,
    var ClassFee:String? = null
)