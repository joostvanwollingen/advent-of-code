package nl.vanwollingen.aoc.util

class AocUtil {
    companion object {
        fun load(input: String): String = this::class.java.classLoader.getResource(input)?.readText()
            ?: throw Exception("$input is not a valid path.")

        fun <T> parse(input: String, clazz: Class<T>): Collection<T> =
            this::class.java.classLoader.getResource(input)?.let { file ->
                file.readText().lines().map {
                    clazz.constructors.first { c -> c.parameterCount == 1 }.newInstance(it)
                } as List<T>
            } ?: throw Exception("$input is not a valid path.")

        fun <T> parse(input: String, function: (it: String) -> T): Collection<T> =
            this::class.java.classLoader.getResource(input)?.let {
                it.readText().lines().map { line -> function(line) }
            } ?: throw Exception("$input is not a valid path.")
    }
}

