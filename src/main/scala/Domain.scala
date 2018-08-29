case class Tweet(text: String) {
  def shout: Tweet =
    Tweet(text.toUpperCase + "!")
}

case class TwitterNotFoundException(message: String) extends Exception(message)
