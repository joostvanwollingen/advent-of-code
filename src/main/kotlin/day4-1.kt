import java.io.File

fun main() {
    val lines = File("day4.test.input").readLines()
    val scratchCards = lines.map{
        println(it)
        Scratchcard(it)}
    val points = scratchCards.sumOf { it.points }
    println(points)
}

data class Scratchcard(val input:String){
    val id = Regex("Card\\W*(\\d*):").find(input)!!.groupValues[1]
    val winningNumbers:List<String> = Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[0].strip().split(" ").filter { it.isNotEmpty() }
    val numbers :List<String> = Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[1].strip().split(" ").filter { it.isNotEmpty() }
    val hasMatch: Boolean = winningNumbers.intersect(numbers).isNotEmpty()
    val numberOfMatches = winningNumbers.intersect(numbers).size
    val points = getPoints(numberOfMatches)

    private fun getPoints(numberOfMatches: Int): Int = when(numberOfMatches){
        0->0
        1->1
        else->Math.pow(2.0, numberOfMatches-1.toDouble()).toInt()
    }



    override fun toString(): String {
        return "Scratchcard(id='$id', winningNumbers=$winningNumbers, numbers=$numbers)"
    }

}