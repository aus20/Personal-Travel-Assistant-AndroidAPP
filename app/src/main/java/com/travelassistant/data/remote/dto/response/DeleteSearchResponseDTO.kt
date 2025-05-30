package com.travelassistant.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class DeleteSearchResponse(
    @SerializedName("message")
    val message: String,

)