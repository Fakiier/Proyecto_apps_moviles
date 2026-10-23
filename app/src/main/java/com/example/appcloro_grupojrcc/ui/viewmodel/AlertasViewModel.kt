package com.example.appcloro_grupojrcc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva

class AlertasViewModel : ViewModel() {
    //la logica esta implementada en alertas, ya declarada
    //para que AccionScreen pueda llamarla desde ahora
    fun registrarAccion(accion: AccionCorrectiva) { }
}