package com.pooranpatel.controllers

import com.pooranpatel.search.Searcher
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes, _}
import play.api.mvc._

object Application extends Controller {

  case class SearchTerms(contributor: Option[String], text: Option[String])

  implicit val searchTermsReads: Reads[SearchTerms] = (
    (JsPath \ "contributor").readNullable[String] and
    (JsPath \ "text").readNullable[String]
  )(SearchTerms.apply _)

  def index = Action {
    Ok(views.html.index())
  }

  def search = Action(parse.json) { request =>
    val vr: JsResult[SearchTerms] = request.body.validate[SearchTerms]
    vr.fold(
      errors => { BadRequest("") },
      st => {
        handleSearchRequest(st)
      }
    )
  }

  private def handleSearchRequest(searchTerms: SearchTerms) = {
    searchTerms match {
      case SearchTerms(None, None) => Ok(Json.toJson(JsArray(Seq.empty)))
      case _ =>  Ok(Json.toJson(Searcher.search(searchTerms)))
    }
  }
}