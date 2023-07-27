package addinn.dev.team.viewModel.group

import addinn.dev.domain.entity.group.Group
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.group.GetGroupsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetGroupsViewModel @Inject constructor(
    private val getGroups: GetGroupsUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dataState = MutableStateFlow<Response<List<Group>>>(Response.Loading)
    val groupsList: StateFlow<Response<List<Group>>> = _dataState

    internal fun getGroups(username: String) = viewModelScope.launch {
        _loadingState.value = true

        getGroups.invoke(username).collectLatest {
            when (it) {
                is Response.Error -> {
                    _dataState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _dataState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }
}