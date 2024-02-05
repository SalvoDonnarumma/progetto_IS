let count = 1;
const nameOrLastnameErrorMessage = "Questo campo deve avere solo lettere<br>";
const formatErrorMessage = "Questo campo non &eacute; nel formato giusto<br>";
const emailErrorMessage = "L'email deve essere almeno del formato username@domain.ext<br>";
const phoneErrorMessage = "Il numero telefonico deve essere nel formato ###-#######<br>";
const emptyFieldErrorMessage = "Il campo non puo' essere vuoto<br>";
const wrongconfirmPassErrorMessage = "La password non cambacia con la precedente<br>";
const lengthPassErrorMessage = "La password deve contenere almeno 12 caratteri!";
const formatNumberCardErrorMessage = "<br>Il numero della carta non rispetta il formato richiesto!<br>";
const dateErrorMessage = "Il formato della data non &egrave; valido"

function validateFormElem(formElem, span, errorMessage) {
  const emptyFieldErrorMessage = "Campo obbligatorio";
  const passwordMinLengthErrorMessage = "La password deve essere lunga almeno 12 caratteri";
  console.log("Eseguito");
  
  if (formElem.id === "cardNumber") {
    const cardNumberRegex = /^\d{4}-\d{4}-\d{4}-\d{4}$/;
    if (!cardNumberRegex.test(formElem.value)) {
      formElem.classList.add("error");
      span.style.color = "red";
      span.innerHTML = "Il formato della carta non &egrave; valido";
      return false;
    }
  } else if (formElem.id === "cardHolder") {
    const nameRegex = /^[A-Za-z\s]+$/;
	if( formElem.validity.valueMissing){
	  span.innerHTML = emptyFieldErrorMessage;
	  return false;
  	}
  
    if (!nameRegex.test(formElem.value)) {
      formElem.classList.add("error");
      span.style.color = "red";
      span.innerHTML = nameOrLastnameErrorMessage;
      return false;
    }
  } else if (formElem.id === "expirationdate") {
    const dateRegex = /^(0[1-9]|1[0-2])\/\d{4}$/;

    if (!dateRegex.test(formElem.value)) {
      formElem.classList.add("error");
      span.style.color = "red";
      span.innerHTML = dateErrorMessage;
      return false;
    }
  }
 

  if (formElem.checkValidity()) {
    formElem.classList.remove("error");
    span.style.color = "red";
    span.innerHTML = "";
    return true;
  }

  formElem.classList.add("error");
  span.style.color = "red";
  if (formElem.validity.valueMissing) {
    span.innerHTML = emptyFieldErrorMessage;
  } else if (formElem.id === "password" && formElem.value.length < 12) {
    span.innerHTML = passwordMinLengthErrorMessage;
  } else {
    span.innerHTML = errorMessage;
  }

  return false;
}


function validateForm2Elem(formElem1, formElem2, span, errorMessage) {
	if(formElem1.checkValidity()){
		formElem1.classList.remove("error");
		span.style.color = "red";
		span.innerHTML = "";
		return true;
	}
	if(formElem2.checkValidity()){
		formElem2.classList.remove("error");
		span.style.color = "red";
		span.innerHTML = "";
		return true;
	}
	formElem1.classList.add("error");
	  span.style.color = "red";
	  if (formElem1.validity.valueMissing) {
	    span.innerHTML = emptyFieldErrorMessage;
	  } else if (formElem1.id === "password" && formElem1.value.length < 10) {
	    span.innerHTML = passwordMinLengthErrorMessage;
	  } else {
	    span.innerHTML = errorMessage;
	  }
	 formElem2.classList.add("error");
	  span.style.color = "red";
	  if (formElem2.validity.valueMissing) {
	    span.innerHTML = emptyFieldErrorMessage;
	  } else if (formElem2.id === "password" && formElem2.value.length < 10) {
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