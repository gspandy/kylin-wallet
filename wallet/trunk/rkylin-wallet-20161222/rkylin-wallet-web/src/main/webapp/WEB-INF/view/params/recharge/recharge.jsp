<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8"%>
<%@ page import="java.text.*" %>   
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">


(function () {
    "use strict";
    angular.module('app')
        .controller('noRefundCtrl', NoRefundCtrl)
    NoRefundCtrl.$inject = ['$scope', '$http', 'localstorage', '$q', '$stateParams', 'refundService']
    function NoRefundCtrl($scope, $http, localstorage, $q, $stateParams, refundService) {
        var vm = this;
        vm.rootPath = "http://hkzs.rongcapital.cn:8163/yueshijue/";
       
        vm.iknow = function () {
            //window.close();

            
                    window.WebViewJavascriptBridge.callHandler(
                        'functionInColse',{'param': 'close Current Page'},function(data){

                        }
                    )

            
        }
    }
})();
</script>


</head>
<div class="wrap bg-ff listBox">
    <i class="ioc-record"></i>
    <h4>success</h4>
    <div class="btn-box" ng-click='vm.iknow()'>return</div>
    
   订单编号：${orderNo}</br>
   支付订单号： ${payOrderNo}</br>
   订单时间 ：${orderTime}</br>
   支付时间： ${orderPayTime}</br>
   消息： ${msg}
</div>