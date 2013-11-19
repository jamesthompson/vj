package vj

import org.jsoup.Jsoup
import org.jsoup.helper.Validate
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

object Main extends App {

  val pageNum = 1

  val urlString = s"http://www.visajourney.com/timeline/eadlist.php?cfl=0&op1=&op2=&op3=&op4=$pageNum&op5=4,7,11,12&op6="

  val doc = Jsoup.connect("http://www.visajourney.com").get


	
}