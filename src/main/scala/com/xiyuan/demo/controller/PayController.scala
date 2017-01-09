package com.xiyuan.demo.controller

import java.util.Date
import javax.servlet.http.HttpServletRequest

import com.google.gson.JsonObject
import com.xiyuan.demo.dao.PayItemMapper
import com.xiyuan.demo.model.PayItem
import com.xiyuan.pay.ali.Alipay
import com.xiyuan.pay.ali.response.AlipayAsyncNotifyResult
import com.xiyuan.pay.ali.value.AlipayStatus
import com.xiyuan.pay.util.{IpUtil, DateUtil, RandomUtil}
import com.xiyuan.pay.weixin.{Weixinpay, WeixinpayCfg}
import com.xiyuan.pay.weixin.model.PayReq
import com.xiyuan.pay.weixin.request.{WeixinpayCreateRequest, WeixinpayRequest}
import com.xiyuan.pay.weixin.response.{WeixinpayAsyncNotifyResult, WeixinpayCreateResult}
import com.xiyuan.pay.weixin.util.{XmlUtil, SignUtil}
import com.xiyuan.pay.weixin.value.WeixinpayStatus
import com.xiyuan.template.controller.LogController
import com.xiyuan.template.log.XYLog
import com.xiyuan.template.util.ResponseUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, ResponseBody}

/**
  * Created by xiyuan_fengyu on 2016/8/25.
  */
@Controller
class PayController {

  @Autowired
  private val dao: PayItemMapper = null

  @RequestMapping(value = Array("pay/create"))
  @ResponseBody
  def payCreate(payType: Int, amount: Double, request: HttpServletRequest): JsonObject = {
    if (payType == 0 || payType == 1) {
      val payItem = new PayItem
      payItem.setOutTradeNo(RandomUtil.generateTimeNumber())
      payItem.setPayType(payType)
      payItem.setTotalAmount(amount)
      payItem.setPayStatus(0)
      payItem.setCreateTime(new Date())
      payItem.setRemark("支付测试")
      dao.insert(payItem)
      if (payType == 0) {
        // 支付宝
        // 构建支付信息，然后返回给client，client以此进行支付(Android和IOS类似)  https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.PsmSu9&treeId=193&articleId=105300&docType=1
        val orderStr = Alipay.generatePayInfo(payItem.getId.toString, "担保交易", "黄牛之家担保交易", payItem.getOutTradeNo, amount)
        if (orderStr != null) {
          ResponseUtil.createJson(true, "订单创建成功", orderStr)
        }
        else {
          ResponseUtil.createJson(false, "订单创建失败")
        }
      }
      else {
        //微信支付
        //创建预支付订单
        //在正式开发的时需要检查当前订单是否已经预创建过，已经预创建过的订单不能再次使用相同的商户订单号创建订单，要么更新商户订单号，要么使用旧的prepayid
        val userIp = IpUtil.get(request)
        val payReq = Weixinpay.createPayReq(payItem.getId.toString, "担保交易", "黄牛之家担保交易", payItem.getOutTradeNo, amount, userIp)
        if (payReq != null) {
          //预创建支付订单成功
          ResponseUtil.createJson(true, "微信支付订单预创建成功", payReq)
        }
        else {
          //创建失败
          ResponseUtil.createJson(false, "微信支付订单预创建失败,请稍后重试")
        }
      }
    }
    else {
      //参数错误
      ResponseUtil.createJson(false, "支付类型有误")
    }
  }

  /**
    * 支付宝支付回调
    *
    * @param id
    * @param notify
    * @param request
    * @return
    */
  @RequestMapping(value = Array("pay/aliCallback/{id}"))
  @ResponseBody
  def aliCallback(@PathVariable id: String, notify: AlipayAsyncNotifyResult, request: HttpServletRequest): String = {
    LogController.logs += XYLog.argsToString(notify)

    val payItem = dao.selectByPrimaryKey(id.toInt)

    LogController.logs += XYLog.argsToString(payItem)

    if (payItem == null) {
      "fail"
    }
    else {
      if (Alipay.checkAsyncNotifyResult(notify, payItem.getOutTradeNo, payItem.getTotalAmount)) {
        LogController.logs += XYLog.argsToString("信息校验通过")

        payItem.setTradeNo(notify.getTrade_no)
        payItem.setBuyerId(notify.getBuyer_id)

        if (AlipayStatus.TRADE_SUCCESS.equals(notify.getTrade_status)) {
          payItem.setFinishTime(DateUtil.strToDate(notify.getGmt_payment))
          payItem.setPayStatus(1)
        }
        else if (AlipayStatus.TRADE_CLOSED.equals(notify.getTrade_status)) {
          payItem.setCloseTime(DateUtil.strToDate(notify.getGmt_close))
          payItem.setPayStatus(-1)
        }
        dao.updateByPrimaryKey(payItem)
        LogController.logs += XYLog.argsToString("更新订单信息，并返回success")
        "success"
      }
      else {
        LogController.logs += XYLog.argsToString("信息校验失败，并返回fail")
        "fail"
      }
    }
  }

  @RequestMapping(value = Array("pay/weixinCallback/{id}"))
  @ResponseBody
  def weixinCallback(@PathVariable id: String, request: HttpServletRequest): String = {
    val in = request.getInputStream
    val notify = XmlUtil.convertoObj(in, classOf[WeixinpayAsyncNotifyResult])
    LogController.logs += XYLog.argsToString(notify)

    val payItem = dao.selectByPrimaryKey(id.toInt)
    if (payItem == null) {
      Weixinpay.generateResultForNotify("FAIL", "订单不存在")
    }
    else {
      if (Weixinpay.notifyCheck(notify, payItem.getOutTradeNo, (payItem.getTotalAmount * 100).toInt)) {
        //对订单信息做更新

        Weixinpay.generateResultForNotify("SUCCESS", "OK")
      }
      else {
        Weixinpay.generateResultForNotify("FAIL", "签名校验失败或者订单信息有误")
      }
    }
  }

}
