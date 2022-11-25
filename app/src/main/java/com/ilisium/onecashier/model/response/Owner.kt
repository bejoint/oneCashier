package com.ilisium.onecashier.model.response

data class Owner(
    val email: String,
    val employee_id: String,
    val is_active: String,
    val is_wizard: String,
    val name: String,
    val outlet_id: String,
    val owner_id: String,
    val password: String,
    val status_employee: String,
    val type_access_id: String,
    val type_access_name: String
)