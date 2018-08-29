import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonWriter }

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object TweetJsonWriter extends RootJsonWriter[Tweet] {
    def write(t: Tweet): JsValue =
      JsString(t.text)
  }

  implicit object ExceptionJsonWriter extends RootJsonWriter[Exception] {
    def write(e: Exception): JsValue =
      JsObject("error" -> JsString(e.getMessage))
  }
}
