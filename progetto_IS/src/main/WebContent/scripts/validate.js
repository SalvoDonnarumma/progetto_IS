let count = 1;
const nameOrLastnameErrorMessage = "Questo campo deve avere solo lettere<br>";
const formatErrorMessage = "Questo campo non &eacute; nel formato giusto<br>";
const emailErrorMessage = "L'email deve essere almeno del formato username@domain.ext<br>";
const phoneErrorMessage = "Il numero telefonico deve essere nel formato ###-#######<br>";
const emptyFieldErrorMessage = "Il campo non puo' essere vuoto<br>";
const wrongconfirmPassErrorMessage = "La password non cambacia con la precedente<br>";
const lengthPassErrorMessage = "La password deve contenere almeno 12 caratteri!";

function validateFormElem(formElem, span, errorMessage) {
	if(formElem.checkValidity()){
		formElem.classList.remove("error");
		span.style.color = "red";
		span.innerHTML = "";
		return true;
	}
	formElem.classList.add("error");
	  span.style.color = "red";
	  if (formElem.validity.valueMissing) {
	    span.innerHTML = emptyFieldErrorMessage;
	  } else if (formElem.id === "password" && formElem.value.length < 10) {
	    span.innerHTML = passwordMinLengthErrorMessage;
	  } else {
	    span.innerHTML = errorMessage;
	  }
  return false;
}

function validate() {
	let valid = true;	
	let form = document.getElementById("regForm");
	
	let spanName = document.getElementById("errorName");
	if(!validateFormElem(form.firstname, spanName, nameOrLastnameErrorMessage)){
		valid = false;
	} 
	let spanLastname = document.getElementById("errorLastname");
	if (!validateFormElem(form.lastname, spanLastname, nameOrLastnameErrorMessage)){
		valid = false;
	}
	let spanEmail = document.getElementById("errorEmail");
	if (!validateFormElem(form.email, spanEmail, emailErrorMessage)){
		valid = false;
	}
	let spanPassword = document.getElementById("errorPassword");
 	if (!validateFormElem(form.password, spanPassword, passwordErrorMessage)) {
    	valid = false;
  	}
	return valid;
}