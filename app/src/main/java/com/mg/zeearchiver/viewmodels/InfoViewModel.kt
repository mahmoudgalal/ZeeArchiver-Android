package com.mg.zeearchiver.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.zeearchiver.Archive
import com.mg.zeearchiver.Archive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoViewModel : ViewModel() {
    private val _codecs: MutableLiveData<List<Codec>> = MutableLiveData()
    private val _formats: MutableLiveData<List<ArchiveFormat>> = MutableLiveData()
    val codecs: LiveData<List<Codec>> by lazy {
        _codecs
    }
    val formats: LiveData<List<ArchiveFormat>> by lazy {
        _formats
    }

    fun load() {
        viewModelScope.launch {
            val info = getSupportedCodecsAndInfo()
            _formats.value = info.first
            _codecs.value = info.second
        }
    }

    private suspend fun getSupportedCodecsAndInfo(): Pair<List<ArchiveFormat>, List<Codec>> {
        return withContext(Dispatchers.IO) {
            val arc = Archive()
            Pair(arc.supportedFormats, arc.supportedCodecs)
        }
    }
}