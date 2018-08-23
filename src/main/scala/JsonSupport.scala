import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.danielasfregola.twitter4s.entities.Tweet
import spray.json.{ DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonWriter }

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object TweetJsonWriter extends RootJsonWriter[Tweet] {
    def write(t: Tweet): JsValue = {
      JsString(t.text)
    }
  }
}
