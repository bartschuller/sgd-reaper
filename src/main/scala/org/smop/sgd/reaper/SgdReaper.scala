package org.smop.sgd.reaper

import org.smop.oai.reaper.Reaper
import java.io.{File, PrintWriter}
import org.smop.oai.{RecordType, ResumptionTokenType, HeaderType}
import xml.{Node, NodeSeq}
import java.nio.charset.Charset

class SgdReaper extends Reaper("http://services.kb.nl/mdo/oai") {
  def listIdentifiers(metadataPrefix: String, resumptionToken: String): Product2[Iterator[HeaderType], Option[String]] = ListIdentifiersReq.resume(metadataPrefix)(Some(resumptionToken))
}

object SgdReaper {
  import com.mongodb.casbah.Imports._
  import com.mongodb.casbah.gridfs.Imports._

  val mongoConn = MongoConnection()
  val mongoDB = mongoConn("kamerstukken")
  val gridfs = GridFS(mongoDB)

  //val baseDir = new File("/Volumes/Sammich 1/SGD/records")
  //baseDir.mkdirs()

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

        val ref = ((metadata \\ "Component" \\ "Resource").head \ "@ref").text
        println(ref)
        if (saveMetadata(id, ref)) {
          println(id)
/*          val file = new File(baseDir, id + ".xml")
          file.createNewFile
          val out = new PrintWriter(file, "UTF-8")
          out.write(toXML[RecordType](rec, "record", defaultScope).toString)
          out.close() */
        } else {
          println("Not a Kamerstuk: " + id)
        }
    }
  }

  private def saveMetadata(id: String, url: String): Boolean = {
    import dispatch._

    val http = new Http
    val req = new Request(url)
    var done = false
    http(req <> {
      x =>
        val record = x \\ "record"
        val isKamerstuk = (record \\ "type").text == "Kamerstuk"
        if (isKamerstuk) {
          saveMongo(id, record.toString)
/*          val file = new File(baseDir, id + "-meta.xml")
          file.createNewFile
          val out = new PrintWriter(file, "UTF-8")
          out.write(record.toString)
          out.close() */
          done = true
        }
    })
    done
  }

  private def saveMongo(id: String, content: String) {
    val data = content.getBytes("UTF-8")
    gridfs(data) { fh =>
      fh.filename = id + "-meta.xml"
      fh.contentType = "application/xml"
    }
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
