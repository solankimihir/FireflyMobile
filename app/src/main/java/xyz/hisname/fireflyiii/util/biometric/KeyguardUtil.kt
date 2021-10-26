/*
 * Copyright (c)  2018 - 2021 Daniel Quah
 * Copyright (c)  2021 ASDF Dev Pte. Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package xyz.hisname.fireflyiii.util.biometric

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import xyz.hisname.fireflyiii.data.local.pref.AppPref
import xyz.hisname.fireflyiii.util.getUniqueHash

class KeyguardUtil(private val activity: Activity) {

    fun isDeviceKeyguardEnabled(): Boolean{
        val keyguardManager  = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardSecure
    }

    fun isAppKeyguardEnabled(): Boolean{
        return AppPref(activity.getSharedPreferences(
            activity.getUniqueHash().toString() + "-user-preferences", Context.MODE_PRIVATE)).isKeyguardEnabled
    }

}