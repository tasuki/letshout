case class Tweet(text: String)

case class TwitterNotFoundException(message: String) extends Exception(message)
