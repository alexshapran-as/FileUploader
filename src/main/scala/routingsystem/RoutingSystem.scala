package routingsystem

import java.io.File

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.stream.scaladsl.FileIO
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.Future

object RoutingSystem extends Directives {

  def getRoute: Route = extractRequestContext { ctx =>

    implicit val materializer = ctx.materializer
    implicit val ec = ctx.executionContext

    path("new") {
      post {
        entity(as[Multipart.FormData]) { formData =>

          // collect all parts of the multipart as it arrives into a map
          val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

            case bodyPart: BodyPart =>
              // stream into a file as the chunks of it arrives and return a future
              // file to where it got stored
              val file: File = new File("/home/alex/work/test_file_upload/test_file_upload.tmp")
              bodyPart.entity.dataBytes.runWith(FileIO.toFile(file)).map(_ => bodyPart.name -> file)

          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

          // when processing have finished create a response for the user
          onSuccess(allPartsF) { _ => complete { "ok!" } }

        }
      }
    }

  }

}
