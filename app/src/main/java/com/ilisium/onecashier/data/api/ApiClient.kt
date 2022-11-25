package com.ilisium.onecashier.data.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ilisium.onecashier.helper.GlobalVar
import com.google.gson.GsonBuilder
import com.ilisium.onecashier.MyApplication
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

/**
 * Created by eko.purnomo on 28/07/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */
class ApiClient{

    companion object {

        val client: Retrofit
            get() {

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf<X509Certificate>()
                        }

                    }
                )

                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

                // add to fix java.net.SocketTimeoutException: timeout
                //https://stackoverflow.com/questions/39219094/sockettimeoutexception-in-retrofit
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(SupportInterceptor())
//                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(ChuckerInterceptor(MyApplication.getContext()!!))

                // to ignore any ssl issue
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { hostname, session -> true }
                    .build()

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                return Retrofit.Builder()
                    .baseUrl(GlobalVar.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
    }

}