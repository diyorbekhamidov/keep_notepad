package com.bounce.keep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform