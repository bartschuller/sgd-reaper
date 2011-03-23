package org.smop.sgd.reaper.tests

import org.specs._
import log.ConsoleLog
import org.smop.sgd.reaper.SgdReaper
import org.smop.oai._
import java.net.URI

/**
 * Created by IntelliJ IDEA.
 * User: schuller
 * Date: 1/16/11
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */

class SgdReaperSpecification extends Specification with ConsoleLog {
  "The Staten-Generaal Digitaal Reaper" should {
    val reaper = new SgdReaper()
    "handle Identify" in {
      reaper.identify.adminEmail must containMatchOnlyOnce("theo.vanveen@kb.nl")
    }
    "handle ListMetadataFormats" in {
      reaper.listMetadataFormats must contain(MetadataFormatType("didl", new URI("http://www.iso.org/mpeg12-didl/schema/unavailable"), new URI("urn:mpeg:mpeg21:2002:02-DIDL-NS")))
    }
    "handle ListIdentifiers" in {
      val idents = reaper.listIdentifiers("didl", Some("SGD")).take(2000).toSeq.map(_.identifier.toString)
      idents must contain("SGD:sgd:mpeg21:19931994:0001561")
    }
    "handle bogus ResumptionToken" in {
      val (iter, newToken) = reaper.listIdentifiers("didl", "SGD!2010-11-08T12:41:35.702Z!null!didl!455200")
      val idents = iter.toSeq.map(_.identifier.toString)
      idents must contain("SGD:sgd:mpeg21:19781979:0007057")
    }
  }
}
