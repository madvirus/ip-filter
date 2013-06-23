package org.chimi.ipfilter.parser

import scala.util.parsing.combinator.JavaTokenParsers
import org.chimi.ipfilter.Config

class Conf extends JavaTokenParsers {
  override val whiteSpace = """[ \t]+""".r

  //  def conf: Parser[Config] = opt(commentList) ~ order ~ alloOrDenyList ^^ (
  //    x => {
  //      //val commentValueList: List[String] = x._1._1
  //      val allowFirst: Boolean = x._1._2
  //      val config = new Config()
  //      config.setAllowFirst(allowFirst)
  //
  //      //val allowOrDenyList: List[Tuple2[String, String]] = x._2
  //      x._2.foreach(value => value match {
  //        case ("allow", v) => config.allow(v)
  //        case ("deny", v) => config.deny(v)
  //      })
  //      config
  //    }
  //    )

  def conf: Parser[Config] = repsep(confPart, eol) ^^ (
    x => {
      val config = new Config
      x.foreach(part => part match {
        case comment:String =>
        case ("order", firstAllow:Boolean) => config.setAllowFirst(firstAllow)
        case ("allow", ip:String) => config.allow(ip)
        case ("deny", ip:String) => config.deny(ip)
      })
      config
    }
    )

  def confPart: Parser[Any] = commentPart | orderPart | allowOrDenyPart

  def commentPart: Parser[String] = """#(.*)""".r ^^ (x => x)

  def orderPart: Parser[Tuple2[String, Boolean]] = "order" ~ orderValue ^^ (x => ("order", x._2))

  def orderValue: Parser[Boolean] = {
    "allow" ~ "," ~ "deny" ^^ (x => true) |
      "deny" ~ "," ~ "allow" ^^ (x => false)
  }

  def allowOrDenyPart: Parser[Tuple2[String, String]] =
    allow ^^ (x => ("allow", x)) |
      deny ^^ (x => ("deny", x))

  def allow: Parser[String] = "allow" ~ "from" ~ ipPattern ^^ (x => x._2)

  def deny: Parser[String] = "deny" ~ "from" ~ ipPattern ^^ (x => x._2)

  def ipPattern: Parser[String] =
    "all" ^^ (x => "*") |
      """(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\*)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\d+)(\/)(\d+)""".r ^^ (x => x) |
      """(\d+)(\.)(\d+)(\.)(\d+)(\.)(\d+)""".r ^^ (x => x)

  def eol: Parser[String] = """(\r?\n)+""".r
}

class ConfParser extends Conf {
  def parse(confText: String): Config = {
    val result = parseAll(conf, confText)
    result.get
  }
}