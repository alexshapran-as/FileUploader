import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor
import routingsystem.RoutingSystem

object FileUploader {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("file-uploader-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    Http().bindAndHandle(RoutingSystem.getRoute, "127.0.0.1", 1234)
    println(s"Server online at http://127.0.0.1:1234/")

  }

}
