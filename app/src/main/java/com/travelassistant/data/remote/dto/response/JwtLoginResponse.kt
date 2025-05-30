package com.travelassistant.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.travelassistant.data.remote.dto.response.UserResponse // Kullanıcı yanıtını temsil eden sınıf
// UserResponse'un import edildiğinden emin olun (eğer farklı paketteyse)
// import com.travelassistant.data.model.response.UserResponse 

data class JwtLoginResponse(
    @SerializedName("user")
    val user: UserResponse, // Bir önceki adımda oluşturduğumuz UserResponse sınıfı
    @SerializedName("token")
    val token: String
)
