package com.trm.daysaway.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import co.touchlab.kermit.Logger
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object CountdownWidgetStateDefinition : GlanceStateDefinition<CountdownWidgetState> {
  private const val DATA_STORE_FILENAME = "CountdownWidgetState"

  private val Context.datastore by dataStore(DATA_STORE_FILENAME, StateSerializer)

  override suspend fun getDataStore(
    context: Context,
    fileKey: String,
  ): DataStore<CountdownWidgetState> = context.datastore

  override fun getLocation(context: Context, fileKey: String): File =
    context.dataStoreFile(DATA_STORE_FILENAME)

  private object StateSerializer : Serializer<CountdownWidgetState> {
    override val defaultValue = CountdownWidgetState.Empty

    override suspend fun readFrom(input: InputStream): CountdownWidgetState =
      try {
        Json.decodeFromString(CountdownWidgetState.serializer(), input.readBytes().decodeToString())
      } catch (ex: SerializationException) {
        Logger.e(
          throwable = ex,
          messageString = "Error reading WidgetState - using defaultValue as fallback.",
        )
        defaultValue
      }

    override suspend fun writeTo(t: CountdownWidgetState, output: OutputStream) {
      output.use {
        it.write(Json.encodeToString(CountdownWidgetState.serializer(), t).encodeToByteArray())
      }
    }
  }
}
