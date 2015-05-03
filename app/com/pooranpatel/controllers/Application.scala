package com.pooranpatel.controllers

import com.pooranpatel.search.Searcher
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes, _}
import play.api.mvc._

/**
 * Singleton controller for wikipedia search application
 */
object Application extends Controller {

  /**
   * Search terms provided by the user
   * @param contributor search term for contributor of wikipedia article
   * @param text search term for text of wikipedia article
   */
  case class SearchTerms(contributor: Option[String], text: Option[String])

  /**
   * Reads for search terms in json to case class [[SearchTerms]]
   */
  implicit val searchTermsReads: Reads[SearchTerms] = (
    (JsPath \ "contributor").readNullable[String] and
    (JsPath \ "text").readNullable[String]
  )(SearchTerms.apply _)

  /**
   * Provides a search page of an application
   * @return rendered html search page
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Handler for search request
   */
  def search = Action(parse.json) { request =>
    val vr: JsResult[SearchTerms] = request.body.validate[SearchTerms]
    vr.fold(
      errors => { BadRequest("") },
      st => {
        handleSearchRequest(st)
      }
    )
  }

  /**
   * Handler for search request if valid search request is received
   * @param searchTerms search terms entered by the user
   * @return
   */
  private def handleSearchRequest(searchTerms: SearchTerms) = {
    searchTerms match {
      case SearchTerms(None, None) => Ok(Json.toJson(JsArray(Seq.empty)))
      case _ =>  Ok(Json.toJson(Searcher.search(searchTerms)))
    }
  }
}