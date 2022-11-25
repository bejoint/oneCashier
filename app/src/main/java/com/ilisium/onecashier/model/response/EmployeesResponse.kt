package com.ilisium.onecashier.model.response

data class EmployeesResponse(
    val status: Boolean,
    val `data`: List<Karyawan>
) {
    data class Karyawan(
        val id: String,
        val owner_id: String,
        val outlet_id: String,
        val name: String,
        val nik: String,
        val phone: String,
        val email: String,
        val password: String,
        val position: String? = "",
        val pin: String? = "000000",
        val type_access_id: String,
        val profile_picture: String? = "",
        val status_employee: String,
        val created_at: String,
        val updated_at: String,
        val deleted_at: String? = "",
        val created_by: String? = "",
        val updated_by: String? = "",
        val deleted_by: String? = ""
    )
}