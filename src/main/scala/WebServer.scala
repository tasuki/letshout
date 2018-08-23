import scala.concurrent.Future
import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.Tweet

object WebServer extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val twitterClient = TwitterRestClient()

  def getTweets(username: String, count: Int): Future[Seq[Tweet]] =
    twitterClient
      .userTimelineForUser(screen_name = username, count = count)
      .map(_.data)

  val route =
    path("shout") {
      get {
        parameters('username.as[String], 'n.as[Int]) { (username, numOfTweets) =>
          complete(s"List $numOfTweets tweets of $username</h1>")
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