function pwdValidation() {

  console.log("비밀번호 재설정 validation ");

  var email = $("#email").val();
  var username = $("#username").val();

  console.log(email);
  console.log(username);


  if (email == '' || username == '') {
    alert("필수값을 입력하세요.");
    return;
  }


  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8080/user/resetPwdValidation",
    dataType: "text",
    data: {
      "username": username,
      "email": email
    },

    success: function (data, textStatus) {
      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == true) {

        alert("test 성공");
        location.href='user/resetPwd';

      } else {

        alert(" 정보가 일치하지 않습니다. ");
        // $('#message').text("사용할 수 없는 ID입니다.");
      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    }

  }); // end ajax

}


function changePwd() {

  console.log("비밀번호 재설정 ");

  // var _id = $("#t_id").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  var email = $("#email").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  var username = $("#username").val();

  console.log(email);
  console.log(username);


  if (email == '' || username == '') {
    alert("필수값을 입력하세요.");
    return;
  }

  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8080/user/resetPwdValidation",
    dataType: "text",
    data: { id: _id}, <!-- ID를 서블릿으로 전송 -->

    success: function (data, textStatus) {
      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == true) {

        alet("test 성공");
        location.href='user/resetPwd';

      } else {

        alert(" 정보가 일치하지 않습니다. ");
        // $('#message').text("사용할 수 없는 ID입니다.");
      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    }

  }); // end ajax

}

function fn_process() {

  console.log("비밀번호 재설정 validation ");

  var _id = $("#t_id").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  if (_id == '') {
    alert("ID를 입력하세요."); <!-- ID를 입력하지 않으면 오류 메시지를 출력 -->
    return;
  }
  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8090/pro16/mem",
    dataType: "text",
    data: { id: _id}, <!-- ID를 서블릿으로 전송 -->
    success: function (data, textStatus) {

      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == 'usable') {
        $('#message').text("사용할 수 있는 ID입니다.");
        $('#btn_duplicate').prop("disabled", true); <!-- 사용할 수 있는 ID면 버튼을 비활성화 시킴 -->
      } else {
        $('#message').text("사용할 수 없는 ID입니다.");

      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    },
    complete:function(data,textStatus) {
      // alert("작업을 완료했습니다.");
    }
  }); // end ajax

}