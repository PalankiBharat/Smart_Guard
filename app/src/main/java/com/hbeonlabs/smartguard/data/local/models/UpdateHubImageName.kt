package com.hbeonlabs.smartguard.data.local.models

import androidx.room.Entity

@Entity
data class UpdateHubImageName(
    val hub_serial_no:String,
    val hub_name :String,
    val hub_image:String,
)
