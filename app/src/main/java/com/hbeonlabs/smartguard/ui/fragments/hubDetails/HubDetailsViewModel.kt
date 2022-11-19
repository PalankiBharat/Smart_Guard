package com.hbeonlabs.smartguard.ui.fragments.hubDetails
import android.icu.util.Calendar
import androidx.lifecycle.viewModelScope
import com.hbeonlabs.smartguard.base.BaseViewModel
import com.hbeonlabs.smartguard.data.local.models.ActivityHistory
import com.hbeonlabs.smartguard.data.local.models.Hub
import com.hbeonlabs.smartguard.data.local.repo.HubRepository
import com.hbeonlabs.smartguard.data.local.repo.HubRepositoryImp
import com.hbeonlabs.smartguard.ui.fragments.hubSettings.HubSettingEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HubDetailsViewModel constructor(
    private val repository: HubRepositoryImp
):BaseViewModel() {

    private val _progressIndicator = MutableStateFlow(0)
    val progressIndicatorLiveData: StateFlow<Int> = _progressIndicator

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _hubEvents = MutableSharedFlow<HubDetailsEvents>()
    val hubEvents: SharedFlow<HubDetailsEvents> = _hubEvents

    var hub_id = ""
    var hub:Hub? = null

    var job: Job = viewModelScope.launch { }

    fun startPress() {
        job = viewModelScope.launch {
            for (i in 0..100) {
                delay(10)
                _progressIndicator.emit(i)
            }
        }
    }

    fun resetPress() {
        job.cancel()
        viewModelScope.launch {
            _progressIndicator.emit(0)

        }
    }

    fun startLoading()
    {
        viewModelScope.launch {
            _loadingState.emit(true)
        }
    }

    fun stopLoading()
    {
        viewModelScope.launch {
            _loadingState.emit(false)
        }
    }

    fun armRegistered()
    {
        viewModelScope.launch {
            try {
                repository.setArmRegistered(hub_id)
            }  catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }

        }
    }

    fun disarmRegistered()
    {
        viewModelScope.launch {
            try {
                repository.setDisarmRegistered(hub_id)
            }  catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }

        }
    }

    fun sirenRegistered()
    {
        viewModelScope.launch {
            try {
                repository.setArmRegistered(hub_id)
            }  catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }

        }
    }

    fun ringRegistered()
    {
        viewModelScope.launch {
            try {
                repository.setArmRegistered(hub_id)
            }  catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }

        }
    }


    fun armDisarmHub(armState:Boolean){
        viewModelScope.launch {
            try {
                repository.armDisarmHub(armState,hub_id)
                val date = Date()
                val timeInMills = date.time
                if (armState)
                    {
                        repository.addActivityHistory(hub_id,"Hub Armed",timeInMills)
                        _hubEvents.emit(HubDetailsEvents.ArmDisarmEvent("Your hub is Armed"))

                    }
                else{
                    repository.addActivityHistory(hub_id,"Hub Disarmed",timeInMills)
                    _hubEvents.emit(HubDetailsEvents.ArmDisarmEvent("Your hub is Disarmed"))
                    }
            }
            catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }
        }
    }

    fun silenceRingHub(silenceRingState:Boolean){
        viewModelScope.launch {
            try {
                repository.silenceRingHub(silenceRingState,hub_id)
                val date = Date()
                val timeInMills = date.time
                if (silenceRingState)
                {
                    repository.addActivityHistory(hub_id,"Siren Enabled",timeInMills)
                    _hubEvents.emit(HubDetailsEvents.SilenceRingEvent("Hub siren is Enabled"))

                }
                else{
                    repository.addActivityHistory(hub_id,"Siren Disabled",timeInMills)
                    _hubEvents.emit(HubDetailsEvents.SilenceRingEvent("Hub siren is Disabled"))
                }
            }
            catch (e:Exception)
            {
                _hubEvents.emit(HubDetailsEvents.SQLErrorEvent(e.localizedMessage))
            }
        }
    }

    suspend fun getActivityHistory() :Flow<List<ActivityHistory>> = repository.getActivityHistory(hub_id)

    suspend fun getHubFromId(hubId:String):Flow<Hub?> {
        val data =  repository.getHubFromId(hubId)
        return data
    }



    sealed class HubDetailsEvents{
        class SilenceRingEvent(val message:String):HubDetailsEvents()
        class ArmDisarmEvent(val message: String):HubDetailsEvents()
        class SQLErrorEvent(val message: String):HubDetailsEvents()
    }

    override fun onCleared() {
        super.onCleared()
    }


}