package petrovich.rules

sealed trait Tag {
  def s: String
}

/**
 * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
 */
object Tag {

  type Tags = List[Tag]
  
  case object FirstWord extends Tag {
    val s = "first_word"
  }
  
  def apply(value: String): Tag = new Tag {
    val s = value
  }
}

