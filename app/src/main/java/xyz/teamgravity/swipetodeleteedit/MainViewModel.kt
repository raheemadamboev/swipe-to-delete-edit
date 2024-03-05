package xyz.teamgravity.swipetodeleteedit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _names = MutableStateFlow(NameProvider.VALUE)
    val names: StateFlow<List<NameModel>> = _names.asStateFlow()

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onDelete(value: NameModel) {
        _names.update { data ->
            val processedData = data.toMutableList()
            processedData.remove(value)
            return@update processedData
        }
    }
}