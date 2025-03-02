package com.mim.controller

import com.mim.dto.LoginUser
import com.mim.dto.ProfileRequest
import com.mim.dto.User
import com.mim.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "프로필 관리")
@RestController
class ProfileController(
    private val userService: UserService
) {

    @Operation(
        summary = "프로필 업데이트",
        description = "사용자의 프로필 정보를 업데이트합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponse(
        responseCode = "200",
        description = "프로필 업데이트 성공",
    )
    @PatchMapping("/profile")
    fun updateProfile(
        @LoginUser user: User,
        @RequestBody request: ProfileRequest
    ) {
        userService.updateProfile(user.username, request)
    }

    @Operation(
        summary = "프로필 변경 여부 확인",
        description = "현재 사용자의 프로필이 변경되었는지 여부를 반환합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = "boolean", example = "true", description = "프로필 변경 여부 (true: 변경됨, false: 변경되지 않음)")
                )]
            ),
        ]
    )
    @GetMapping("/profile/changed")
    fun isProfileChanged(
        @LoginUser user: User,
    ): Boolean {
        return userService.isProfileChanged(user.username)
    }

}
