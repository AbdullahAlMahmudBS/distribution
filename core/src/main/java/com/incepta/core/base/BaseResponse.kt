package com.incepta.core.base


/**
 * Created by Abdullah on 18/5/25.
 */
/**
 * A base class for all response classes.
 * This class can be used to create a common response class for all API responses.
 * This class can be extended to create a specific response class for each API.
 */

open class BaseResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): BaseResponse<T> {
            return BaseResponse("Success", "Success", data)
        }

        fun <T> error(message: String): BaseResponse<T> {
            return BaseResponse("Error", message)
        }
    }
}
