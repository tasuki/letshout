import scala.concurrent.{ ExecutionContext, Future }

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.exceptions.TwitterException

object Twitter {
  val twitterClient = TwitterRestClient()

  def getTweets(
    username: String,
    count: Int
  )(implicit ec: ExecutionContext): Future[Seq[Tweet]] =
    twitterClient
      .userTimelineForUser(screen_name = username, count = count)
      .map(_.data)
      .map(_.map(t => Tweet(t.text)))
      .recoverWith {
        case e: TwitterException if e.code.intValue == 404 =>
          val msg = s"Twitter handle $username not found, most likely a typo!"
          Future.failed(TwitterNotFoundException(msg))
      }
}