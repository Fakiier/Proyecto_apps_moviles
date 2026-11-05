package com.example.appcloro_grupojrcc.data.sync

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasSync(private val context: Context) {

    private val claveUltimaMedicion = longPreferencesKey("ultima_medicion_remota")
    private val claveMomento = longPreferencesKey("momento_ultima_sync")

    val momentoUltimaSync: Flow<Long> =
        context.dataStore.data.map { it[claveMomento] ?: 0L }

    suspend fun ultimaMedicionRemota(): Long =
        context.dataStore.data.first()[claveUltimaMedicion] ?: 0L

    suspend fun guardar(ultimaMedicion: Long?, momento: Long) {
        context.dataStore.edit { p ->
            if (ultimaMedicion != null) p[claveUltimaMedicion] = ultimaMedicion
            p[claveMomento] = momento
        }
    }
}