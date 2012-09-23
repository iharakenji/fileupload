package controllers

import models._
import play.api.mvc._
import play.api.Logger
import play.api.libs.json.Json._

object FileUpload extends Controller {
  
  def index = Action {
    Logger.info("index")
    Ok(views.html.fileupload.index())
  }

  def upload = Action(parse.multipartFormData) {
    Logger.info("upload")
    request =>
      request.body.file("files[]").map {
        Logger.info("uploadstart")
        file =>
          import java.io.File
          val filename = file.filename
          val contentType = file.contentType
          file.ref.moveTo(new File("/tmp/"+ filename))
          Logger.info("uploadend")
          Ok(toJson(List(Map(
            "delete_type" -> "DELETE",
            "delete_url" -> "http://yahoo.com",
            "name" -> filename,
            "size" -> "10",
            "thumbnail_url" -> "http://google.com",
            "type" -> contentType.get,
            "url" -> "http://twitter.com"
          ))))
      }.getOrElse {
        Logger.info("uploaderror")
        Redirect(routes.FileUpload.index).flashing(
          "error" -> "Missing file"
        )
      }
  }

  def delete = TODO

}