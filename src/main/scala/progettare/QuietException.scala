package progettare

/**
 * Created by Hayden Blauzvern on 12/3/14.
 */
case class QuietException(str: String) extends Exception(str: String) {
  override def fillInStackTrace() = this
}