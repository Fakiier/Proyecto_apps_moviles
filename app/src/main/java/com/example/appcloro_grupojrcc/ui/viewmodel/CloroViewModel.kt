package com.example.appcloro_grupojrcc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appcloro_grupojrcc.data.model.Rol
import com.example.appcloro_grupojrcc.data.repository.CloroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CloroViewModel : ViewModel(){
    private val repo = CloroRepository()
    private val _puntos = MutableStateFlow(repo.obtenerPuntos())
    val puntos = _puntos.asStateFlow()

    private val _rol = MutableStateFlow<Rol?>(null)
    val rol = _rol.asStateFlow()

    fun elegirRol(r: Rol) { _rol.value = r }

    fun puntoPorId(id: Int ) = _puntos.value.find{ it.id == id }
}