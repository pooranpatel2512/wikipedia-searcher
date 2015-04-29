package com.pooranpatel.search

import java.nio.file.Paths

import com.pooranpatel.controllers.Application.SearchTerms
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser
import org.apache.lucene.search.{Query, ScoreDoc, TopDocs, IndexSearcher}
import org.apache.lucene.store.FSDirectory


object Searcher {

  lazy val indexReader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get("../index")))

  lazy val indexSearcher = new IndexSearcher(indexReader)

  lazy val analyzer = new WikipediaTextAnalyzer()

  lazy val queryParserHelper = new StandardQueryParser(analyzer)

  def search(searchTerms: SearchTerms): Array[String] = {
    println(s"Searching... for = ${searchTerms}")

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

  private def queryForContributor(searchTerms: SearchTerms): String = {
    val stringBuilder = new StringBuilder
    searchTerms.contributor match {
      case Some(contributor) => stringBuilder.append("contributor").append(":").append(contributor).append(" ").toString()
      case None => ""
    }
  }

  private def queryForText(searchTerms: SearchTerms): String = {
    val stringBuilder = new StringBuilder
    searchTerms.text match {
      case Some(text) => stringBuilder.append("text").append(":").append(text).append(" ").toString()
      case None => ""
    }
  }
}
