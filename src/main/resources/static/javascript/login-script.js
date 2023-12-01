
const passwordField = document.getElementById('password');
const showPasswordCheckbox = document.getElementById('showPasswordCheckbox');

showPasswordCheckbox.addEventListener('change', function() {
	if (this.checked) {
		passwordField.type = 'text';
	} else {
		passwordField.type = 'password';
	}
});
