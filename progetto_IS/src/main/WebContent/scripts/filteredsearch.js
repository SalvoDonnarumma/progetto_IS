function searchAndFilter(){
  let input, filter, schede, product;
  input = document.getElementById("search-input");
  filter = input.value.toUpperCase();
  schede = document.getElementById("prodotti");
  product = schede.querySelectorAll(".box1");
    
  const selectedCategoria = document.getElementById("categoria-select").value;
  const selectedPrice = document.getElementById("prezzo-select").value;
	
  for (const bean of product) {
    let textValue = bean.querySelector(".pname").textContent.trim();
    const prodottoCategoria = bean.querySelector(".categoria").textContent.trim();
    const prodottoPrezzo = parseFloat(bean.querySelector(".prezzo").textContent.trim());
	
	let minprezzo = 0;
	let maxprezzo = 0;
	
	if( selectedPrice != null ){
		let rangeprezzi = selectedPrice.split('-');
		console.log(rangeprezzi);
		minprezzo = parseFloat(rangeprezzi[0]);
		maxprezzo = parseFloat(rangeprezzi[1]);
	}
	
    const nameMatches = textValue.toUpperCase().indexOf(filter) > -1;
    const categoryMatches = selectedCategoria.length === 0 || selectedCategoria.includes(prodottoCategoria);
    const priceMatches = selectedPrice.length === 0 || (prodottoPrezzo >= minprezzo && prodottoPrezzo <= maxprezzo);
    
    console.log(textValue);
    console.log("Match col nome: "+textValue.toUpperCase().indexOf(filter));
    console.log("Match col categoria: "+categoryMatches);
	console.log("Match col prezzo: "+priceMatches);
	
    if (filter && selectedCategoria.length > 0 && selectedPrice.length > 0) {
      // Se è presente una ricerca per nome e filtri attivi, considera solo i prodotti che corrispondono a entrambi
     	 if (nameMatches && categoryMatches && priceMatches){  //tutti i filtri sono attivi + ricerca nome
        	console.log("CASO: TUTTI E 3 I FILTRI FUNZIONANTI");
        	bean.style.display = "";
      	}else
			bean.style.display= "none";							
    } else if( filter && (selectedCategoria.length > 0) && (selectedPrice.length <= 0)) { //attivo solo ricerca con nome e filtro categoria
		  		if(nameMatches && categoryMatches){
					  console.log("CASO: SOLO CATEGORIA E SOLO NOME");
		  			bean.style.display= "";
		  		}else	
		  			bean.style.display= "none";	
	} else if(filter && (selectedPrice.length > 0) && (selectedCategoria.length <= 0)){ //attivo solo ricerca e filtro prezzo
					if(nameMatches && priceMatches){ 
						console.log("CASO: SOLO PREZZO E SOLO NOME"); 
		  				bean.style.display= "";
		  			}else	
		  				bean.style.display= "none";	
	} else if (filter && (selectedCategoria.length <= 0) && (selectedPrice.length <= 0) ) {
      // Se è presente solo una ricerca per nome, considera solo i prodotti che corrispondono al nome
      	if (nameMatches) {
			 console.log("CASO: SOLO NOME"); 
        	bean.style.display = "";
      	} else {
        	bean.style.display = "none";
      	}
      // Se sono presenti solo i filtri attivi, calcolo i 4 casi possibile: 
    } else if(selectedCategoria.length > 0 && selectedPrice.length > 0){ //categorie e prezzi attivi
		if (categoryMatches && priceMatches) {
				console.log("CASO: SOLO CATEGORIA E PREZZO"); 
       		 bean.style.display = "";
      } else { 
        	 bean.style.display = "none";
      }
	} else if (selectedCategoria.length > 0 && (selectedPrice.length <= 0) ) { //solo categoria attivi
      		if (categoryMatches) {
				  console.log("CASO: SOLO CATEGORIA");
        		bean.style.display = "";
      		} else {
        		bean.style.display = "none";
      		}
    }  else if( selectedPrice.length > 0 && (selectedCategoria.length <= 0) ) { //solo prezzo attivi
		   if (priceMatches){
			   console.log("CASO: PREZZO");
        		bean.style.display = "";
      		} else {
        		bean.style.display = "none";
      		}
    }  else { // Se non ci sono né ricerca né filtri attivi, mostra tutti i prodotti
    		console.log("CASO: NIENTE");
			bean.style.display = "";
    }
  }
}

function searchAndFilterOrders(){
  let input, filter, schede, orders;
  input = document.getElementById("search-input");
  filter = input.value.toUpperCase();
  schede = document.getElementById("orders");
  orders = schede.querySelectorAll(".order-card");
  
  let dateinit = document.getElementById("dateinit").value;
  let dateend = document.getElementById("dateend").value;

  console.log(dateinit);
  console.log(dateend);
  let gg1, mm1, aa1, gg2, mm2, aa2, gg, mm, aa;
	
	if( dateinit.length > 0 ){
		let date = dateinit.split('-');
		console.log("Data splittata: "+date);
		gg1 = parseInt(date[0]);
		mm1 = parseInt(date[1]);
		aa1 = parseInt(date[2]);
	}
	
	if( dateend.length > 0 ){
		let date = dateend.split('-');
		console.log("Data splittata: "+date);
		gg2 = parseInt(date[0]);
		mm2 = parseInt(date[1]);
		aa2 = parseInt(date[2]);
	}
  	
  for (const bean of orders) {
    let textValue = bean.querySelector(".email").textContent.trim();
    console.log(textValue);
	console.log(dateinit);
	
	let date = bean.querySelector(".data").textContent.trim();
	if( date.length > 0 ){
		let subdate = date.substring(0,10);
		date = subdate.split('-');
		gg = parseInt(date[0]);
		mm = parseInt(date[1]);
		aa = parseInt(date[2]);
		console.log(gg);
		console.log(mm);
		console.log(aa);
	}
	
    const nameMatches = textValue.toUpperCase().indexOf(filter) > -1;
    const dateBothMatches = (gg >= gg1 && gg <= gg2) && (mm >= mm1 && mm <= mm2) && (aa >= aa1 && aa <= aa2);
    const dateInfMatches = (gg >= gg1) && (mm >= mm1) && (aa >= aa1);
    const dateSupMatches = (gg <= gg2) && (mm <= mm2) && (aa <= aa2);
	
	if (filter && dateinit.length > 0 && dateend.length > 0) {
      // Se è presente una ricerca per nome e filtri attivi, considera solo i prodotti che corrispondono a entrambi
     	 if (nameMatches && dateBothMatches){  //tutti i filtri sono attivi + ricerca nome
        	console.log("CASO: TUTTI E 3 I FILTRI FUNZIONANTI");
        	bean.style.display = "";
      	}else
			bean.style.display= "none";							
    } else if( filter && (dateinit.length > 0) && (dateend.length <= 0)) { 
		  		if(nameMatches && dateInfMatches){
					  console.log("CASO: SOLO CATEGORIA E SOLO NOME");
		  			bean.style.display= "";
		  		}else	
		  			bean.style.display= "none";	
	} else if(filter && (dateend.length > 0) && (dateinit.length <= 0)){ 
					if(nameMatches && dateSupMatches){ 
						console.log("CASO: SOLO PREZZO E SOLO NOME"); 
		  				bean.style.display= "";
		  			}else	
		  				bean.style.display= "none";	
	} else if (filter && (dateinit.length <= 0) && (dateend.length <= 0) ) {
      	if (nameMatches) {
			 console.log("CASO: SOLO NOME"); 
        	bean.style.display = "";
      	} else {
        	bean.style.display = "none";
      	}
      // Se sono presenti solo i filtri attivi, calcolo i 4 casi possibile: 
    } else if(dateinit.length > 0 && dateend.length > 0){ //categorie e prezzi attivi
		if (dateBothMatches) {
				console.log("CASO: SOLO CATEGORIA E PREZZO"); 
       		 bean.style.display = "";
      } else { 
        	 bean.style.display = "none";
      }
	} else if (dateinit.length > 0 && (dateend.length <= 0) ) { //solo categoria attivi
      		if (dateInfMatches) {
				  console.log("CASO: SOLO CATEGORIA");
        		bean.style.display = "";
      		} else {
        		bean.style.display = "none";
      		}
    }  else if( dateend > 0 && (dateinit.length <= 0) ) { //solo prezzo attivi
		   if (dateSupMatches){
			   console.log("CASO: PREZZO");
        		bean.style.display = "";
      		} else {
        		bean.style.display = "none";
      		}
    }  else { // Se non ci sono né ricerca né filtri attivi, mostra tutti i prodotti
    		console.log("CASO: NIENTE");
    		bean.style.display = "";	
    }
  }  
}