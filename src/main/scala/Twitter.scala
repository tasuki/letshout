import scala.concurrent.{ ExecutionContext, Future }

import com.danielasfregola.twitter4s.TwitterRestClient

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
}