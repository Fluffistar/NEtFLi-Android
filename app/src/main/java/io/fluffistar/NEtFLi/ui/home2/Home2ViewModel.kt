package io.fluffistar.NEtFLi.ui.home2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class Home2ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val apiProfile: String

) : ViewModel() {
}