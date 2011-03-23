package org.smop.sgd.reaper

import org.smop.oai.reaper.Reaper
import java.io.{File, PrintWriter}
import org.smop.oai.{ResumptionTokenType, HeaderType}

/**
 * Created by IntelliJ IDEA.
 * User: schuller
 * Date: 1/16/11
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */

class SgdReaper extends Reaper("http://services.kb.nl/mdo/oai") {
  def listIdentifiers(metadataPrefix: String, resumptionToken: String): Product2[Iterator[HeaderType], Option[String]] = ListIdentifiersReq.resume(metadataPrefix)(Some(resumptionToken))
}

object SgdReaper {
  def main(args: Array[String]) {
    val file = new File("SGD.txt")
    file.createNewFile
    val out = new PrintWriter(file, "UTF-8")
    val reaper = new SgdReaper
    reaper.listIdentifiers("didl", Some("SGD")).foreach { id: HeaderType =>
      out.println(id.identifier)
    }
    out.close
  }
}
