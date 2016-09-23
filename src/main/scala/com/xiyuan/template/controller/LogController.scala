package com.xiyuan.template.controller

import java.util.Date

import com.google.gson.JsonObject
import com.xiyuan.template.log.XYLog
import com.xiyuan.template.util.ResponseUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by YT on 2016/6/30.
  */
@Controller
class LogController {

  @RequestMapping(value = Array("log"))
  def log(): String = {
    "log/index"
  }

  @RequestMapping(value = Array("log/test"))
  @ResponseBody
  def logTest(): JsonObject = {
    LogController.logs += "日志测试：" + new Date()
    val result = ResponseUtil.createJson(true, "测试日志添加成功")
    result
  }

  @RequestMapping(value = Array("log/add"))
  @ResponseBody
  def logAdd(log: String): JsonObject = {
    LogController.logs += log
    val result = ResponseUtil.createJson(true, "测试日志添加成功")
    result
  }

  @RequestMapping(value = Array("log/list"))
  @ResponseBody
  def logList(clientId: String): JsonObject = {
    val logClient: LogClient =  if (!LogController.logClients.contains(clientId)) {
      val tempLogClient = new LogClient(clientId, System.currentTimeMillis(), 0)
      LogController.logClients += clientId -> tempLogClient
      tempLogClient
    }
    else {
      val tempLogClient = LogController.logClients(clientId)
      tempLogClient.lastActionTime = System.currentTimeMillis()
      tempLogClient
    }


    var waitTime = 0
    while (waitTime < LogController.maxWaitTime && logClient.msgIndex >= LogController.logs.length) {
      Thread.sleep(100)
      waitTime += 100
    }

    val logLen = LogController.logs.length
    val msgArr = new Array[String](logLen - logClient.msgIndex)
    for (i <- msgArr.indices) {
      msgArr(i) = LogController.logs(logClient.msgIndex + i)
    }
    logClient.msgIndex = logLen
    val result = ResponseUtil.createJson(true, "日志获取成功", msgArr)

    //当logs数量达到1000时,按照logClients中的最小msgIndex，清除之前的消息，并更新所有client的msgIndex
    if (logLen >= 1000) {
      var minMsgIndex = Int.MaxValue
      val now = System.currentTimeMillis()
      LogController.logClients.foreach(item => {
        val client = item._2
        if (now - client.lastActionTime >= 30000) {
          //客户端断开连接
          LogController.logClients.remove(item._1)
        }
        else if (minMsgIndex > client.msgIndex) {
          minMsgIndex = client.msgIndex
        }
      })

      //更新所有client的msgIndex
      LogController.logClients.foreach(_._2.msgIndex -= minMsgIndex)
    }

    result
  }

}

object LogController {

  private val maxWaitTime = 20000

  val logClients: mutable.HashMap[String, LogClient] = new mutable.HashMap[String, LogClient]()

  val logs: ArrayBuffer[String] = new ArrayBuffer[String]()

}

/**
  * 记录接收Log客户端的信息
  *
  * @param id 客户端id
  * @param lastActionTime 客户端最后一次与服务器交互的时间，用来判断客户端是否断开连接
  * @param msgIndex 客户端读取消息的游标
  */
class LogClient(val id: String, var lastActionTime: Long, var msgIndex: Int)


object Log {

  def d(log: String): Unit = {
    LogController.logs += log
  }

}