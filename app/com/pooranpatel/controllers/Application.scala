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

  implicit val searchTermsWrites: Writes[SearchTerms] = (
    (JsPath \ "contributor").writeNullable[String] and
    (JsPath \ "text").writeNullable[String]
  )(unlift(SearchTerms.unapply))

  def index = Action {
    Ok(views.html.index())
  }

  def search = Action(parse.json) { request =>
    val vr: JsResult[SearchTerms] = request.body.validate[SearchTerms]
    vr.fold(
      errors => { BadRequest("") },
      st => {
        Searcher.serach(st) match {
          case Some(results) => Ok(Json.toJson(results))
          case None => Ok(Json.toJson(JsArray(Seq.empty)))
        }
      }
    )
  }
}