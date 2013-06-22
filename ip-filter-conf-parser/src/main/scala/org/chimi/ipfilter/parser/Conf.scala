package org.chimi.ipfilter.parser

import scala.util.parsing.combinator.JavaTokenParsers
import org.chimi.ipfilter.Config

class Conf extends JavaTokenParsers {
  def conf: Parser[Config] = opt(order) ~ rep(allowOrDeny) ^^ (
    x => {
      val allowFirst: Option[Boolean] = x._1
      val config = new Config()
      config.setAllowFirst(allowFirst.get)

      val allowOrDenyList: List[Tuple2[String, String]] = x._2
      allowOrDenyList.foreach(value => value match {
        case ("allow", v) => config.allow(v)
        case ("deny", v) => config.deny(v)
      })
      config
    }
    )

  def order: Parser[Boolean] = "order" ~ orderValue ^^ (_._2)

  def orderValue: Parser[Boolean] = {
    orderAllowValue ~ "," ~ orderDenyValue ^^ (x => true) |
      orderDenyValue ~ "," ~ orderAllowValue ^^ (x => false)
  }

  def orderAllowValue: Parser[Any] = "allow"

  def orderDenyValue: Parser[Any] = "deny"

  def allowOrDeny: Parser[Tuple2[String, String]] =
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

}

class ConfParser extends Conf {
  def parse(confText: String): Config = {
    val result = parseAll(conf, confText)
    result.get
  }
}

