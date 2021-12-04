package com.example.emoji.repository

import com.example.emoji.model.StreamModel
import com.example.emoji.viewState.StreamViewState
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author y.gladkikh
 */
interface StreamRepository {
    fun getStreams(): Observable<StreamModel>
    fun getLocalStreams(): Single<StreamViewState>
}
