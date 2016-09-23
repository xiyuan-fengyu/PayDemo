package com.xiyuan.template.controller

import javax.servlet.http.HttpServletRequest

import com.google.gson.{Gson, JsonObject}
import com.xiyuan.pay.ali.response.AlipayAsyncNotifyResult
import com.xiyuan.pay.util.IpUtil
import com.xiyuan.template.dao.{TbTestDao, TbTestMapper}
import com.xiyuan.template.log.XYLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, ResponseBody}

/**
  * Created by YT on 2016/6/30.
  */
@Controller
class TestController {

  @Autowired
  private val tbTestDao: TbTestDao = null

  @RequestMapping(value = Array("test"))
  @ResponseBody
  def test(): JsonObject = {
    val result = new JsonObject
    result.addProperty("success", true)
    result.addProperty("message", "test")
    result
  }

  @RequestMapping(value = Array("test/ip"))
  @ResponseBody
  def testIp(request: HttpServletRequest): JsonObject = {
    val result = new JsonObject
    result.addProperty("ip", IpUtil.get(request))
    result.addProperty("success", true)
    result.addProperty("message", "test")
    result
  }

  @RequestMapping(value = Array("test/list"))
  @ResponseBody
  def testList(): JsonObject = {
    val result = new JsonObject
    result.add("all", new Gson().toJsonTree(tbTestDao.selectByExample(null)))
    result.add("maxId", new Gson().toJsonTree(tbTestDao.maxId()))
    result.add("idBetween", new Gson().toJsonTree(tbTestDao.idBetween(0, 10)))
    result.addProperty("success", true)
    result.addProperty("message", "test")
    result
  }

  @RequestMapping(value = Array("test/params"))
  @ResponseBody
  def aliCallback(request: HttpServletRequest): String = {
    LogController.logs += "回调参数：start"
    val it = request.getParameterMap.entrySet().iterator()
    while (it.hasNext) {
      val keyVal = it.next()
      LogController.logs += keyVal.getKey.asInstanceOf[String] + "\t->\t" + keyVal.getValue.asInstanceOf[Array[String]](0)
    }
    LogController.logs += "回调参数：end"
    "success"
  }

}
