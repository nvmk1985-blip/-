package com.example.data.api

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

enum class GeminiModelOption(
    val id: String,
    val displayName: String,
    val badgeTamil: String,
    val descriptionTamil: String
) {
    GEMINI_3_8_FLASH(
        id = "gemini-3.8-flash",
        displayName = "Gemini 3.8 Flash",
        badgeTamil = "⚡ Gemini 3.8 Flash (வேகமான சர்வ அறிவு AI)",
        descriptionTamil = "எதை கேட்டாலும் (பொது அறிவு, அறிவியல், கணிதம், ஆங்கிலம், கதைகள், மொழிபெயர்ப்பு) உடனடியாக தமிழ் + ஆங்கிலத்தில் பதிலளிக்கும்!"
    ),
    GEMINI_FLASH_LATEST(
        id = "gemini-flash-latest",
        displayName = "Gemini Flash Latest",
        badgeTamil = "🚀 Gemini Flash Latest",
        descriptionTamil = "உடனடி உரையாடல் மற்றும் தினசரி ஸ்போக்கன் இங்கிலீஷ் பயிற்சிக்கு ஏற்றது."
    ),
    GEMINI_3_1_PRO(
        id = "gemini-3.1-pro-preview",
        displayName = "Gemini 3.1 Pro",
        badgeTamil = "🧠 Gemini 3.1 Pro (ஆழ்ந்த விளக்கம்)",
        descriptionTamil = "கடினமான கேள்விகள், கட்டுரைகள் மற்றும் விரிவான இலக்கண விளக்கங்களுக்கு ஏற்றது."
    )
}

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<ContentPayload>,
    val systemInstruction: ContentPayload? = null,
    val generationConfig: GenerationConfigPayload? = null
)

@JsonClass(generateAdapter = true)
data class ContentPayload(
    val parts: List<PartPayload>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class InlineDataPayload(
    val mimeType: String,
    val data: String
)

@JsonClass(generateAdapter = true)
data class PartPayload(
    val text: String? = null,
    val inlineData: InlineDataPayload? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfigPayload(
    val temperature: Float? = null,
    val maxOutputTokens: Int? = null,
    val responseMimeType: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<CandidatePayload>? = null
)

@JsonClass(generateAdapter = true)
data class CandidatePayload(
    val content: ContentPayload? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContentByModel(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    /**
     * Calls the Gemini REST API using the requested model (defaulting to gemini-3.8-flash),
     * and automatically cascades through supported fallback models if a model alias returns 404/error.
     */
    suspend fun generateWithFallback(
        apiKey: String,
        request: GeminiRequest,
        preferredModel: String = GeminiModelOption.GEMINI_3_8_FLASH.id
    ): GeminiResponse? {
        val candidateModels = listOf(
            preferredModel,
            "gemini-flash-latest",
            "gemini-3.5-flash",
            "gemini-3.1-flash-lite-preview",
            "gemini-3.1-pro-preview"
        ).distinct()

        for (modelId in candidateModels) {
            try {
                val response = service.generateContentByModel(
                    model = modelId,
                    apiKey = apiKey,
                    request = request
                )
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!text.isNullOrBlank()) {
                    return response
                }
            } catch (_: Exception) {
                // Continue to next supported fallback model in the chain
            }
        }
        return null
    }
}
