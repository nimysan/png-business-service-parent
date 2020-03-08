(function($) {
	function isPoneAvailable(mobile) {
		var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
		if (!myreg.test(mobile)) {
			return false;
		} else {
			return true;
		}
	}

	var unitWaitingTimeInSecond = 10;
	var smsRequestTimer;
	window.reduceWaitingTime = function() {
		unitWaitingTimeInSecond = unitWaitingTimeInSecond - 1;
		$("#triggerSmsCodeButton").text("可在(" + unitWaitingTimeInSecond + ")s后重新获取");
		if (unitWaitingTimeInSecond == 0) {
			$("#triggerSmsCodeButton").text("点击获取验证码").attr("disabled", false);
			clearInterval(smsRequestTimer);
		}
	}

	function getSalary() {
		var mobile = $("#mobileInput").val();
		if (!isPoneAvailable(mobile)) {
			alert("手机号码格式有误，请重新输入");
			return;
		}
		var vcode = $("#vcodeInput").val();
		if (!vcode) {
			alert("请输入您收到的4位数的短信验证码");
			return;
		}

		window.location.href= "/salary?mobile=" + mobile + "&vcode=" + vcode;
	};



	function getSmsCode()  {
		var mobile = $("#mobileInput").val();
		if (!isPoneAvailable(mobile)) {
			alert("手机号码格式有误，请重新输入");
			return;
		}

		var robotCheckCode = $("#robotCheckCodeInput").val();
		if (!robotCheckCode) {
			alert("请输入图形验证码中的数字");
			return;
		}
		var triggerButton = $("#triggerSmsCodeButton");
		if (smsRequestTimer) {
			clearInterval(smsRequestTimer);
		}
		smsRequestTimer = setInterval("reduceWaitingTime()", 1000);
		triggerButton.attr("disabled", true).text("可在(" + unitWaitingTimeInSecond + ")s后重新获取");
		$.get("/vcode?mobile=" + $("#mobileInput").val() + "&robotCheckCode=" + robotCheckCode, function(data)  {
			if (data.code == 'PNG-00000') {
				alert("发送验证码成功");
			} else {
					alert(data.message);
		}
		});
}


	$(document).ready(function() {
	$("#triggerSmsCodeButton").click(getSmsCode);
	$("#getSalaryButton").click(getSalary);
});

}) (jQuery);