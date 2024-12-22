// 역할을 저장할 변수
let selectedRole = null;

function selectOption(button, role) {

  // 모든 버튼에서 'active' 클래스 제거
  const buttons = document.querySelectorAll('.button');

  buttons.forEach(btn => {
    btn.classList.remove('active');
  });

  // 클릭된 버튼에 'active' 클래스 추가
  button.classList.add('active');

  selectedRole = role;

  console.log("role 확인 :: " + role);

  document.getElementById('selectedRole').value = selectedRole;

}

// 역할을 선택하지 않았을 때
function validateForm() {

  const selectedRole = document.getElementById('selectedRole').value;
  console.log(selectedRole);

  if( !selectedRole ) {
    alert("역할을 선택하세요.");
    event.preventDefault();
    return;
  }
  return true;
}