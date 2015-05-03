package com.pooranpatel.search

import java.nio.file.Paths

import com.pooranpatel.controllers.Application.SearchTerms
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser
import org.apache.lucene.search.{Query, ScoreDoc, TopDocs, IndexSearcher}
import org.apache.lucene.store.FSDirectory

/**
 * Singleton object which provides the searching capabilities in indexed wikipedia articles
 */
object Searcher {

  lazy val indexReader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get("../index")))

  lazy val indexSearcher = new IndexSearcher(indexReader)

  lazy val analyzer = new WikipediaTextAnalyzer()

  lazy val queryParserHelper = new StandardQueryParser(analyzer)

  /**
   * Handles search request
   * @param searchTerms search terms entered by the user
   * @return list of wikipedia article's titles
   */
  def search(searchTerms: SearchTerms): Array[String] = {

    val stringBuilder = new StringBuilder
    stringBuilder.append(queryForContributor(searchTerms))
    stringBuilder.append(queryForText(searchTerms))

    println(stringBuilder.toString)
    val query = queryParserHelper.parse(stringBuilder.toString(), "")
    val results = indexSearcher.search(query, 100).scoreDocs
    results.map { res =>
      indexSearcher.doc(res.doc).get("title")
    }
  }

  /**
   * Builds lucene query term for contributor using search terms entered by user
   * @param searchTerms search terms entered by the user
   * @return lucene query term for contributor
   */
  private def queryForContributor(searchTerms: SearchTerms): String = {
    val stringBuilder = new StringBuilder
    searchTerms.contributor match {
      case Some(contributor) => stringBuilder.append("contributor").append(":").append(contributor).append(" ").toString()
      case None => ""
    }
  }

  /**
   * Builds lucene query term for wikipedia article text using search terms entered by user
   * @param searchTerms search terms entered by the user
   * @return lucene query for wikipedia article text
   */
  private def queryForText(searchTerms: SearchTerms): String = {
    val stringBuilder = new StringBuilder
    searchTerms.text match {
      case Some(text) => stringBuilder.append("text").append(":").append(text).append(" ").toString()
      case None => ""
    }
  }
}