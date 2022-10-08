package com.hbeonlabs.smartguard.ui.fragments.secondoryUser.edit

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.hbeonlabs.smartguard.base.BaseViewModel
import com.hbeonlabs.smartguard.data.local.models.SecondaryUser
import com.hbeonlabs.smartguard.data.local.repo.SecondaryUserRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSecondaryUserViewModel @Inject constructor(
    private val repo: SecondaryUserRepositoryImp
):BaseViewModel()
{
    private val _addSecondaryUserEvents = MutableSharedFlow<EditSecondaryUserEvents>()
    val addSecondaryUserEvents: SharedFlow<EditSecondaryUserEvents> = _addSecondaryUserEvents

    var hub_id  =""
    var slot = 0

    fun addSecondaryUser(name:String,imageUri:Uri,number:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            val secondaryUser = SecondaryUser(
                null,
                name,
                slot,
                imageUri.toString(),
                number,
                hub_id)
            if (secondaryUser.user_name.isBlank() || secondaryUser.user_pic.isBlank() || secondaryUser.user_phone_number.isBlank())
            {
                _addSecondaryUserEvents.emit(EditSecondaryUserEvents.SQLErrorEvent("Please Fill All the Fields"))
            }
            try
            {
                repo.addSecondaryUser(secondaryUser )
                _addSecondaryUserEvents.emit(EditSecondaryUserEvents.AddUserSuccessEvent)
            }
            catch (e: Exception) {
                _addSecondaryUserEvents.emit(EditSecondaryUserEvents.SQLErrorEvent(e.localizedMessage))
            }
        }
    }

}

sealed class EditSecondaryUserEvents{
    class SQLErrorEvent(val message: String): EditSecondaryUserEvents()
    object AddUserSuccessEvent: EditSecondaryUserEvents()
}