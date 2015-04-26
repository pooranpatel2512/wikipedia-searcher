package com.pooranpatel.search

import java.nio.file.Paths

import com.pooranpatel.controllers.Application.SearchTerms
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{ScoreDoc, TopDocs, IndexSearcher}
import org.apache.lucene.store.FSDirectory


object Searcher {

  lazy val ir = DirectoryReader.open(FSDirectory.open(Paths.get("../index")))

  lazy val is = new IndexSearcher(ir)

  lazy val analyzer = new StandardAnalyzer()

  lazy val cParser = new QueryParser("contributor", analyzer)

  def serach(st: SearchTerms): Option[Array[String]] = {
    st.contributor.map { contributor =>
      val query = cParser.parse(contributor)
      val results = is.search(query, null, 1000).scoreDocs
      results.map { res =>
        is.doc(res.doc).get("title")
      }
    }
  }

}
