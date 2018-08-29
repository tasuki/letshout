import scala.io.StdIn
import scala.util.{ Failure, Success }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer

object WebServer extends App with JsonSupport {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val exceptionHandler = ExceptionHandler {
    case e: TwitterNotFoundException =>
      complete(StatusCodes.NotFound, new Exception(e.getMessage))
    case e =>
      println("Internal server error: " + e.getMessage)
      complete(StatusCodes.InternalServerError, new Exception("Internal Server error"))
  }

  def shout(tweet: Tweet): Tweet =
    tweet.copy(text = tweet.text.toUpperCase + "!")

  def uppercaseTweets(username: String, numOfTweets: Int): Route = {
    val futureTweets = Twitter.getTweets(username, numOfTweets)
      .map(_.map(shout))

    onSuccess(futureTweets) { tweets =>
      complete(tweets.map(TweetJsonWriter.write))
    }
  }

  val route =
    handleExceptions(exceptionHandler) {
      path("shout") {
        get {
          parameters('username.as[String], 'n.as[Int])(uppercaseTweets)
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}