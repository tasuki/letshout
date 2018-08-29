import org.scalatest.FlatSpec

class TweetTest extends FlatSpec {
  it should "uppercase the tweet and add exclamation mark" in {
    val shout = Tweet("I'm not shouting").shout.text
    assert(shout == "I'M NOT SHOUTING!")
  }
}
