package com.example.emoji.viewState.elm.laod

import com.example.emoji.model.StreamModel
import com.example.emoji.repository.StreamRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class LoadStream @Inject constructor(
    private val repo: StreamRepository,
) {

    fun getStreams(): Observable<StreamModel> {
        return repo.getStreams()
    }
}
