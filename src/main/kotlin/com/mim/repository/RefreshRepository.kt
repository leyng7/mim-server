package com.mim.repository

import org.springframework.stereotype.Repository

@Repository
class RefreshRepository {

    fun existsByRefreshToken(refreshToken: String): Boolean {
        return true
    }

    fun deleteByRefreshToken(refreshToken: String) {

    }

}
