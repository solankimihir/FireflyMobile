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

package xyz.hisname.fireflyiii.workers

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import xyz.hisname.fireflyiii.data.local.account.OldAuthenticatorManager
import xyz.hisname.fireflyiii.data.local.pref.AppPref
import xyz.hisname.fireflyiii.data.remote.firefly.FireflyClient
import xyz.hisname.fireflyiii.util.getUserEmail
import xyz.hisname.fireflyiii.util.network.CustomCa
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

abstract class BaseWorker(private val context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams){

    private val baseUrl by lazy { AppPref(sharedPref).baseUrl }
    private val accessToken by lazy { OldAuthenticatorManager(AccountManager.get(context)).accessToken }
    val genericService by lazy { FireflyClient.getClient(baseUrl,accessToken, AppPref(sharedPref).certValue, getTrust(), getSslSocket()) }
    protected val sharedPref by lazy { context.getSharedPreferences(context.getUserEmail() + "-user-preferences", Context.MODE_PRIVATE)}
    private val customCa by lazy { CustomCa(File(context.filesDir.path + "/user_custom.pem")) }

    protected fun getCurrentUserEmail(): String{
        val bufferedReader = BufferedReader(FileReader(context.applicationInfo.dataDir + "/current_active_user.txt"))
        return bufferedReader.readLine()
    }

    private fun getTrust(): X509TrustManager?{
        return if(AppPref(sharedPref).isCustomCa){
            customCa.getCustomTrust()
        } else {
            null
        }
    }

    private fun getSslSocket(): SSLSocketFactory?{
        return if(AppPref(sharedPref).isCustomCa){
            customCa.getCustomSSL()
        } else {
            null
        }
    }
}