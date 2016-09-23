<%--
  Created by IntelliJ IDEA.
  User: YT
  Date: 2016/6/30
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Log</title>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container">
      <br>
      <textarea id="logList" class="form-control" rows="40" readonly="readonly" style="background: #f6f6f6;"></textarea>
      <br>
      <button id="logClear" type="button" class="btn btn-warning">Clear</button>
    </div>

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script>
    (function () {
      var ctx = "${pageContext.request.contextPath}";
      var taskFlag = null;
      var logList;
      var logClear;
      var clientId;

      $(document).ready(function () {
        logList = $("#logList");
        logClear = $("#logClear");
        logClear.click(function () {
          logList.val("");
        });
        initClientId();
        startLogTask();
      });

      function initClientId() {
        clientId = localStorage.getItem("log.clientId");
        if (clientId == null || clientId == "") {
          clientId = new Date().getTime() + "_" + parseInt(Math.random() * 9999)
          localStorage.setItem("log.clientId", clientId);
        }
      }

      function startLogTask() {
        taskFlag = new Date().getTime() + "_" + parseInt(Math.random() * 1000);
        $("body").append("<div id='" + taskFlag + "'></div>");
        logTask();
      }

      function logTask() {
        $.get(ctx + "/log/list?clientId=" + clientId + "&_=" + new Date().getTime(), function(result){
          if (result.data) {
            var logs = result.data;
            if (logs.length > 0) {
              var newLogs = "";
              for (var i = 0, len = logs.length; i < len; i++) {
                newLogs += logs[i] + "\n";
              }
              var str = logList.val();
              logList.val(str + newLogs);
            }

            if ($("#" + taskFlag).length > 0) {
              logTask();
            }
          }
        });
      }


    })();
  </script>
  </body>
</html>
