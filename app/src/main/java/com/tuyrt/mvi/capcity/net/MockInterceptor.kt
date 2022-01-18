package com.tuyrt.mvi.capcity.net

import com.tuyrt.mvi.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol.HTTP_2
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.endsWith("mock") -> getMockApiResponse
                else -> ""
            }

            return Response.Builder()
                .request(chain.request())
                .code(200)
                .protocol(HTTP_2)
                .message(responseString)
                .body(responseString.toByteArray().toResponseBody("application/json".toMediaType()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError("""MockInterceptor is only meant for Testing Purposes and bound to be used only with DEBUG mode""")
        }
    }
}

const val getMockApiResponse = """
{
  "articles": [
    {
      "title": "Title",
      "description": "Description",
      "imageUrl": "imageUrl"
    },
    {
      "title": "Title",
      "description": "Description",
      "imageUrl": "imageUrl"
    }
  ]
}"""