package vj

import com.github.nscala_time.time.Imports._
import java.io.PrintWriter
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import scala.collection.JavaConverters._

case class History(filed: Option[DateTime],
                   noa: Option[DateTime], 
                   bio: Option[DateTime], 
                   appr: Option[DateTime], 
                   rec: Option[DateTime]) {

  def timeDiff(start: Option[DateTime], finish: Option[DateTime]): Option[Long] = 
    (start, finish) match {
      case (Some(d1), Some(d2)) => if(d1 < d2) Some((d1 to d2).toDuration.getStandardDays) else None
      case (_, _) => None 
    }

  def filedToAppr = timeDiff(filed, appr)
  def bioToAppr = timeDiff(bio, appr)
  def apprToRec = timeDiff(appr, rec)
  def noaToAppr = timeDiff(noa, appr)
}

object Main extends App {

  implicit def mkdate(s: String): Option[DateTime] = 
    try{ Some(new DateTime(s)) } catch { case _: Throwable => None }

  def printToFile(c: String, l: String) =
    Some(new java.io.PrintWriter(s"./$l")).foreach{f => try{f.write(c)} finally{f.close}}
  
  def getPageHistories(pageNum: Int): List[History] = {
    def getHistory(l: List[Element]): Option[History] = l match {
      case f :: n :: b :: a :: r => Some(History(f.html, n.html, b.html, a.html, r.head.html))
      case _ => None
    }
    Jsoup.connect(s"http://www.visajourney.com/timeline/eadlist.php?cfl=0&op1=&op2=&op3=&op4=$pageNum&op5=4,7,11,12&op6=")
      .get
      .select("table[class*=pme-main]")
      .select("tr").asScala.toList.tail.map(
        t => getHistory(t.select("td").asScala.toList.drop(3))
      ).flatMap(h => h)
  }

  val histories = (1 to 10).flatMap(getPageHistories)
  printToFile(histories.map(_.filedToAppr).flatMap(f => f).mkString("\n"), "filedToApproval.txt")
  printToFile(histories.map(_.bioToAppr).flatMap(f => f).mkString("\n"), "biometricsToApproval.txt")
  printToFile(histories.map(_.apprToRec).flatMap(f => f).mkString("\n"), "approvalToCard.txt")
  printToFile(histories.map(_.noaToAppr).flatMap(f => f).mkString("\n"), "noaToApproval.txt")
  println(s"Data taken from present day to ${histories.last.filed.getOrElse("invalid last day").toString}")

}