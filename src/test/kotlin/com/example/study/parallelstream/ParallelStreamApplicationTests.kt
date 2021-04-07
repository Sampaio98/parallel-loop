package com.example.study.parallelstream

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureTimeMillis

@SpringBootTest
class ParallelStreamApplicationTests {

    private val numbers = listOf<Number>(1, 2, 3, 4, 5)

    @Test
    fun `Teste utilizando loop sequencial`() {
        println("Iniciando sequencial...")

        val time = measureTimeMillis {
            for (i in numbers) {
                doSomething()
            }
        }

        println("Sequencial levou $time ms")
    }

    @Test
    fun `Teste utilizando loop Stream Parallel`() {
        println("Iniciando paralelismo...")

        val time = measureTimeMillis {
            numbers.parallelStream().forEach { doSomething() }
        }

        println("Paralelo levou $time ms")
    }

    @Test
    fun `Teste utilizando loop assincrono`() {
        println("Iniciando assíncrono...")

        val time = measureTimeMillis {

            runBlocking {
                val numbersAsync = numbers.map { async { doSomething() } }
                numbersAsync.forEach { it.await() }
            }
        }

        numbers.forEachParallel { doSomething() }

        println("Assíncrono levou $time ms")
    }

    @Test
    fun `Teste utilizando loop assincrono com method extension foreachParallel`() {
        println("Iniciando assíncrono...")

        val time = measureTimeMillis {
            numbers.forEachParallel { doSomething() }
        }

        println("ForeachParallel levou $time ms")
    }


    @Test
    fun `Teste utilizando loop assincrono com method extension foreachParallelMeasureTimeMillis`() {
        println("Iniciando assíncrono...")

        val time = numbers.forEachParallelMeasureTimeMillis { doSomething() }

        println("ForeachParallelMeasureTimeMillis levou $time ms")
    }


    fun doSomething() {
        runBlocking { delay(1000) }
    }

}

fun <A> Collection<A>.forEachParallel(f: suspend (A) -> Unit): Unit = runBlocking {
    map { async { f(it) } }.forEach { it.await() }
}

fun <A> Collection<A>.forEachParallelMeasureTimeMillis(f: suspend (A) -> Unit): Long = measureTimeMillis {
    runBlocking {
        map { async { f(it) } }.forEach { it.await() }
    }
}

