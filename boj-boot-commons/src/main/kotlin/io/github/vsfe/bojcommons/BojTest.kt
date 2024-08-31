package io.github.vsfe.bojcommons

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BojTest(val problemNumber: Int)