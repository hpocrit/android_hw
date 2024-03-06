package ru.kpfu.itis.android_hw.data.handlers

import retrofit2.HttpException
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.exceptions.Exception400
import ru.kpfu.itis.android_hw.data.exceptions.Exception401
import ru.kpfu.itis.android_hw.data.exceptions.Exception404
import ru.kpfu.itis.android_hw.data.exceptions.Exception429
import ru.kpfu.itis.android_hw.data.exceptions.Exception5xx
import ru.kpfu.itis.android_hw.utils.ResManager

class ExceptionHandlerDelegate(
    private val resManager: ResManager,
) {
    fun handleException(ex: Throwable): Throwable {
        return when (ex) {
            is HttpException -> {
                when (ex.code() % 100) {
                    4 -> {
                        when(ex.code()){
                            400 -> {
                                Exception400(message = resManager.getString(R.string.exception_400))
                            }

                            401 -> {
                                Exception401(message = resManager.getString(R.string.exception_401))
                            }

                            404 -> {
                                Exception404(message = resManager.getString(R.string.exception_404))
                            }

                            429 -> {
                                Exception429(message = resManager.getString(R.string.exception_429))
                            }
                            else -> {
                                ex
                            }
                        }
                    }

                    5 -> {
                        Exception5xx(message = resManager.getString(R.string.exception_5xx))
                    }

                    else -> {
                        ex
                    }
                }
            }

            else -> {
                ex
            }
        }
    }
}
