package com.example

import android.Manifest
import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.components.hasAudioRecordingPermission
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Pesalam AI", appName)
  }

  @Test
  fun `verify audio recording permission check`() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val shadowApp = Shadows.shadowOf(application)

    shadowApp.denyPermissions(Manifest.permission.RECORD_AUDIO)
    assertFalse(hasAudioRecordingPermission(application))

    shadowApp.grantPermissions(Manifest.permission.RECORD_AUDIO)
    assertTrue(hasAudioRecordingPermission(application))
  }
}
