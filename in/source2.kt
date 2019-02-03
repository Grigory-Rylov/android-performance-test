val words = "Hello, i am test string"
val charsSet = HashSet<Char>()
if (charsSet.isEmpty()) {
    RU.forEach {
        charsSet.add(it)
    }
}

val result = words.asSequence()
        .filter { it.isLetter() }
        .map { it.toLowerCase() }
        .all { charsSet.contains(it) }