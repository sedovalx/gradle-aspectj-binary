class App {

    @MyAnnotation("category")
    fun hello(name: String): String {
        println("Got " + name)
        return "Hello " + name
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val app = App()
            println(app.hello("Bob"))
        }
    }
}
