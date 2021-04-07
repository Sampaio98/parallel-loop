package com.example.study.parallelstream

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureTimeMillis

@SpringBootTest
class Scenarios {

    @Test
    fun `Teste com coroutines chamadas assincronas`() {
        val time = measureTimeMillis {
            runBlocking {
                val userAsync = async { findUser() }
                val accountAsync = async { findAccount() }

                val user = userAsync.await()
                val account = accountAsync.await()

                println("Name: ${user[1]} with account: $account")
            }
        }
        println("Async levou: $time ms")
    }

    private suspend fun findAccount(): Int {
        delay(2000)
        return 1
    }

    private suspend fun findUser(): Map<Int, String> {
        delay(2000)
        return mapOf(1 to "Jack")
    }
}