import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{ Failure, Success }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.exceptions.TwitterException

object WebServer extends App with JsonSupport {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val twitterClient = TwitterRestClient()

  def shout(tweet: Tweet): Tweet =
    tweet.copy(text = tweet.text.toUpperCase + "!")

  def getTweets(username: String, count: Int): Future[Seq[Tweet]] =
    twitterClient
      .userTimelineForUser(screen_name = username, count = count)
      .map(_.data)

  def uppercaseTweets(username: String, numOfTweets: Int): Route = {
    val futureTweets = getTweets(username, numOfTweets)
      .map(_.map(shout))

    onComplete(futureTweets) {
      case Success(tweets) => complete(tweets.map(TweetJsonWriter.write))
      case Failure(f) => f match {
        case e: TwitterException if e.code.intValue == 404 =>
          val msg = "Not found, most likely typo in the Twitter handle!"
          complete(StatusCodes.NotFound, new Exception(msg))
        case _ =>
          complete(StatusCodes.InternalServerError, new Exception("Internal Server error"))
      }
    }
  }

  val route =
    path("shout") {
      get {
        parameters('username.as[String], 'n.as[Int])(uppercaseTweets)
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}