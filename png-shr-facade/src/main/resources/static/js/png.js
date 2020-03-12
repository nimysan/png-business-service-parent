(function($) {
	/*
	jconfirm.defaults = {
		theme: 'light',
		title: '铂涛集团',
	}*/

	function _png_alert(msg) {
		$.dialog(msg);
	}

	function isPoneAvailable(mobile) {
		var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
		if (!myreg.test(mobile)) {
			return false;
		} else {
			return true;
		}
	}

	var unitWaitingTimeInSecond = 60;
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
			_png_alert("手机号码格式有误，请重新输入");
			return false;
		}
		var name = $("#nameInput").val();
		if (!name) {
			_png_alert("请输入姓名");
			return false;
		}
		var vcode = $("#vcodeInput").val();
		if (!vcode) {
			_png_alert("请输入您收到的6位数的短信验证码");
			return false;
		}
		var salaryDetailUrl = "/salary?mobile=" + mobile + "&vcode=" + vcode + "&userName=" + name;
		window.location.href = salaryDetailUrl;
		return false;
	};

	function refreshCodeImageAndShow() {
		$("#robotCheckImage").attr("src", "/robotCheckImage?" + Math.random());
		return false;
	}


	function getSmsCode() {
		var mobile = $("#mobileInput").val();
		if (!isPoneAvailable(mobile)) {
			_png_alert("手机号码格式有误，请重新输入");
			return false;
		}
		var name = $("#nameInput").val();
		if (!name) {
			_png_alert("请输入姓名");
			return;
		}
		var robotCheckCode = $("#robotCheckCodeInput").val();
		if (!robotCheckCode) {
			_png_alert("请输入图形验证码中的数字");
			return false;
		}


		$('#robotCheckModal').modal('hide');
		$.get("/vcode?mobile=" + $("#mobileInput").val() + "&robotCheckCode=" + robotCheckCode + "&userName=" + name, function(data) {
			if (data.code == 'PNG-00000') {
				setButtonClock();
				_png_alert("发送验证码成功");
			} else {
				cleanButtonClock();
				_png_alert(data.message);
			}
		});
	}

	function setButtonClock() {
		var triggerButton = $("#triggerSmsCodeButton");
		smsRequestTimer = setInterval("reduceWaitingTime()", 1000);
		triggerButton.attr("disabled", true).text("可在(" + unitWaitingTimeInSecond + ")s后重新获取");
	}

	function cleanButtonClock() {
		if (smsRequestTimer) {
			clearInterval(smsRequestTimer);
		}
	}


	function openRobotCheckModal() {
		var mobile = $("#mobileInput").val();
		if (!isPoneAvailable(mobile)) {
			_png_alert("手机号码格式有误，请重新输入");
			return;
		}
		$('#robotCheckModal').modal('show');
		return false;
	}


	$(document).ready(function() {
		$("#triggerSmsCodeButton").click(openRobotCheckModal);
		$("#getSalaryButton").click(getSalary);
		$("#refreshImageCode").click(refreshCodeImageAndShow);
		$("#robotCheckModelConfirm").click(getSmsCode);
	});

})(jQuery);