package com.pooranpatel.search

import java.nio.file.Paths

import com.pooranpatel.controllers.Application.SearchTerms
import org.apache.lucene.index.{Term, DirectoryReader}
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser
import org.apache.lucene.search._
import org.apache.lucene.store.FSDirectory
import play.api.Play
import play.api.Play.current

/**
 * Singleton object which provides the searching capabilities in indexed wikipedia articles
 */
object Searcher {

  val indexesDir = Play.application.configuration.getString("application.indexesdir").getOrElse("")

  lazy val indexReader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexesDir)))

  lazy val indexSearcher = new IndexSearcher(indexReader)

  lazy val analyzer = new WikipediaTextAnalyzer()

  lazy val queryParserHelper = new StandardQueryParser(analyzer)

  /**
   * Handles search request
   * @param searchTerms search terms entered by the user
   * @return list of wikipedia article's titles
   */
  def search(searchTerms: SearchTerms): Array[String] = {
    val results = indexSearcher.search(buildQuery(searchTerms), 100).scoreDocs
    results.map { res =>
      indexSearcher.doc(res.doc).get("title")
    }
  }

  /**
   * Builds lucene query using search terms entered by user
   * @param searchTerms search terms entered by the user
   * @return lucene query
   */
  def buildQuery(searchTerms: SearchTerms): Query = {
    val query = new BooleanQuery()
    searchTerms.contributor match {
      case Some(contributor) => query.add(new TermQuery(new Term("contributor", contributor)), BooleanClause.Occur.MUST)
      case None =>
    }
    searchTerms.text match {
      case Some(text) => query.add(new TermQuery(new Term("text", text)), BooleanClause.Occur.MUST)
      case None =>
    }
    query
  }
}