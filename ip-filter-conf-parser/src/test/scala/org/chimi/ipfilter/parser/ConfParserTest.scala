package org.chimi.ipfilter.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class ConfParserTest extends FunSuite {

  test("ConfParser should parse valid conf text") {
    val confValue =
      """order deny,allow
        |allow from 1.2.3.4
        |deny from 10.20.30.40
        |allow from 101.102.103.*
        |allow from 201.202.203.10/64
      """.stripMargin
    val conf = new ConfParser().parse(confValue)

    assert(!conf.isAllowFirst)
    assert(!conf.isDefaultAllow)
    assert(conf.getAllowList.size == 3)
    assert(conf.getAllowList.get(0) == "1.2.3.4")
    assert(conf.getAllowList.get(1) == "101.102.103.*")
    assert(conf.getAllowList.get(2) == "201.202.203.10/64")
    assert(conf.getDenyList.size == 1)
    assert(conf.getDenyList.get(0) == "10.20.30.40")
  }
}