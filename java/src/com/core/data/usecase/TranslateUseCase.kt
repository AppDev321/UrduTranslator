package com.core.data.usecase

import com.core.data.model.translate.TranslateReq
import com.core.data.model.translate.TranslateResponse
import com.core.data.repo.translation.TranslateRepo
import com.core.domain.usecase.NetworkUseCaseHelper
import org.json.JSONArray
import retrofit2.Response
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val translateRepo: TranslateRepo
) : NetworkUseCaseHelper<TranslateResponse, TranslateUseCase.Params>() {

    override suspend fun buildUseCase(params: Params?): Response<TranslateResponse> {
        val req = params!!.params
        val response =  translateRepo.getTranslation(req.from, req.to, req.text)
        val data = TranslateResponse(parseResult(response.body().toString()))
       return Response.success(data)
    }

    data class Params constructor(val params: TranslateReq) {
        companion object {
            fun create(params: TranslateReq) = Params(params)
        }
    }

    private fun parseResult(inputJson: String): String {
        var result = ""
        val jsonArray = JSONArray(inputJson).getJSONArray(0)
        for (pos in 0 until jsonArray.length()) {
            val innerJsonArray = jsonArray.getJSONArray(pos)
            //for (int pos1 = 0; pos1 < innerJsonArray.length(); pos1++) {
            val value = innerJsonArray.getString(0)
            if (value != null && value != "null") {
                result = "$result $value"
            }
            //}
        }
        return result.trim { it <= ' ' }
        //return ((JSONArray) ((JSONArray) new JSONArray(inputJson).get(0)).get(0)).get(0).toString();
    }
}