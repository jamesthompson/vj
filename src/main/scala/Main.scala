package vj

import com.github.nscala_time.time.Imports._
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import scala.collection.JavaConverters._

case class History(filed: Option[DateTime], noa: Option[DateTime], bio: Option[DateTime], appr: Option[DateTime], rec: Option[DateTime]) {
  override def toString(): String = s"filed: $filed, noa: $noa, bio: $bio, appr: $appr, rec: $rec"
}

object Main extends App {

  implicit def mkdate(s: String): Option[DateTime] = if(s.isEmpty) None else Some(new DateTime(s))

  val histories = (1 to 20).flatMap(getPageHistories)
  
  def getPageHistories(pageNum: Int): List[History] = {

    def getHistory(l: List[Element]): Option[History] = l match {
      case f :: n :: b :: a :: r => Some(History(f.html, n.html, b.html, a.html, r.head.html))
      case _ => None
    }

    val urlString = s"http://www.visajourney.com/timeline/eadlist.php?cfl=0&op1=&op2=&op3=&op4=$pageNum&op5=4,7,11,12&op6="
    val doc = Jsoup.connect(urlString).get
    val table = doc.select("table[class*=pme-main]")
    val trs = table.select("tr").asScala.toList.tail
    
    trs.map(t => getHistory(t.select("td").asScala.toList.drop(3))).flatMap(h => h)

  }

  println(histories.mkString("\n"))

}