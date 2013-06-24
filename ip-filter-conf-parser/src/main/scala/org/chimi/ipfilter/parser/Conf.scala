package org.chimi.ipfilter.parser

import scala.util.parsing.combinator.JavaTokenParsers
import org.chimi.ipfilter.Config

class Conf extends JavaTokenParsers {
  override val whiteSpace = """[ \t]+""".r

  def conf: Parser[Config] = repsep(confPart, eol) ^^ (
    x => {
      val config = new Config
      x.foreach(part =>
        part match {
        case ("order", firstAllow:Boolean) => config.setAllowFirst(firstAllow)
        case ("allow", ip:String) => config.allow(ip)
        case ("deny", ip:String) => config.deny(ip)
        case _ =>
      })
      config
    }
    )

  def confPart: Parser[Any] = commentPart | orderPart | allowOrDenyPart | emptyLine

  def commentPart: Parser[String] = """#(.*)""".r ^^ (x => x)

  def orderPart: Parser[Tuple2[String, Boolean]] =
    "order" ~ orderValue ~ opt(commentPart) ^^ (x => ("order", x._1._2))

  def orderValue: Parser[Boolean] = {
    "allow" ~ "," ~ "deny" ^^ (x => true) |
      "deny" ~ "," ~ "allow" ^^ (x => false)
  }

  def allowOrDenyPart: Parser[Tuple2[String, String]] =
    allow ^^ (x => ("allow", x)) |
      deny ^^ (x => ("deny", x))

  def allow: Parser[String] = "allow" ~ "from" ~ ipPattern ~ opt(commentPart) ^^ (x => x._1._2)

  def deny: Parser[String] = "deny" ~ "from" ~ ipPattern ~ opt(commentPart) ^^ (x => x._1._2)

  def ipPattern: Parser[String] =
    "all" ^^ (x => "*") |
      """(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\d+)(\/)(\d+)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\d+)""".r ^^ (x => x)

  def emptyLine: Parser[String] = ""
  def eol: Parser[String] = """(\r?\n)+""".r
}

class ConfParser extends Conf {
  def parse(confText: String): Config = {
    val result = parseAll(conf, confText)
    if (result.successful)
      result.get
    else
      throw new ConfParserException(result.toString)
  }
}

class ConfParserException(msg:String) extends RuntimeException(msg) {

}