package org.smop.sgd.reaper

import org.smop.oai.reaper.Reaper
import java.io.{File, PrintWriter}
import org.smop.oai.{RecordType, ResumptionTokenType, HeaderType}
import xml.{Node, NodeSeq}

class SgdReaper extends Reaper("http://services.kb.nl/mdo/oai") {
  def listIdentifiers(metadataPrefix: String, resumptionToken: String): Product2[Iterator[HeaderType], Option[String]] = ListIdentifiersReq.resume(metadataPrefix)(Some(resumptionToken))
}

object SgdReaper {
  val baseDir = new File("/Volumes/Sammich 1/SGD/records")
  baseDir.mkdirs()

  def main(args: Array[String]) {
    fetchMetadata()
  }

  private def fetchMetadata() {
    import scalaxb._
    import Scalaxb._
    import org.smop.oai._

    import DefaultXMLProtocol._
    val reaper = new SgdReaper
    reaper.listRecords("didl", Some("SGD")).foreach {
      rec: RecordType =>
        val id = rec.header.identifier.toString.replace("SGD:sgd:mpeg21:", "").replace(":", "-")
        val metadata = rec.metadata.map(_.any.value).head.asInstanceOf[NodeSeq]

        if ((metadata \\ "Descriptor" \ "Statement").exists(_.text.matches("^Kamerstuk.*"))) {
          val ref = ((metadata \\ "Component" \\ "Resource").head \ "@ref").text
          println(ref)
          saveMetadata(id, ref)
          println(id)
          val file = new File(baseDir, id + ".xml")
          file.createNewFile
          val out = new PrintWriter(file, "UTF-8")
          out.write(toXML[RecordType](rec, "record", defaultScope).toString)
          out.close()
        } else {
          println("Not a Kamerstuk: "+id)
        }
    }
  }

  private def saveMetadata(id: String, url: String) {
    import dispatch._

    val http = new Http
    val req = new Request(url)
    http(req <> {
      x =>
        val record = x \\ "record"
        val file = new File(baseDir, id + "-meta.xml")
        file.createNewFile
        val out = new PrintWriter(file, "UTF-8")
        out.write(record.toString)
        out.close()
    })
  }

  private def listKeys {
    val file = new File("SGD.txt")
    file.createNewFile
    val out = new PrintWriter(file, "UTF-8")
    val reaper = new SgdReaper
    reaper.listIdentifiers("didl", Some("SGD")).foreach {
      id: HeaderType =>
        out.println(id.identifier)
    }
    out.close
  }
}
