let pathArray = window.location.pathname.split('/');
let contextPath = '/' + pathArray[1];
let url = contextPath + "/StoreServlet";
let response_i;

function dynamicStore(url, page = 1, itemsPerPage = 15) {
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json; charset=utf-8'
    }).done((response) => {
        response = JSON.parse(response);
        const totalItems = response.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const paginatedData = response.slice(startIndex, endIndex);

        let contenutoHTML = "";
        for (const bean of paginatedData) {
            contenutoHTML += "<div class=\"box1\">";
			contenutoHTML += "</a> <h3 class=\"pname\">"+bean.name+"</h3>";
			contenutoHTML += "<a href=\"OttieniDettagliProdotto?id="+bean.code+"\">";
 			contenutoHTML += "<img src="+bean.imagePath+"\ onerror=\"this.src='images/nophoto.png'\">";
 			contenutoHTML += "</a><h5>Categoria:</h5><h4 class=\"categoria\">"+bean.categoria+"</h4>";
 			contenutoHTML += "<h5>Costo:<h3 class=\"prezzo\">"+bean.price.toFixed(2)+"</h3>&euro;</h5>";
 			if( bean.taglie.quantitaM==0 && bean.taglie.quantitaL==0 && bean.taglie.quantitaXL==0 && bean.taglie.quantitaXXL==0)
				contenutoHTML += "<h3 style=\"color: red; text-align:center;\"> Prodotto momentaneamente<br>  non disponibile! <br> <h3> </div>";
			else{
				contenutoHTML += "<div class=\"cart\">";
 				contenutoHTML += "<a href=\"cart.jsp?id="+bean.code+"\"><i class='bx bx-cart-add'></i></a> </div> </div>";
 			}
        }

        $("#prodotti").empty().append(contenutoHTML);
        renderPagination(totalPages, page);
    });
}

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
			dynamicStore(url);
    }
  }
}

function renderPagination(totalPages, currentPage) {
    const pagination = $("#pagination");
    pagination.empty();

    for (let i = 1; i <= totalPages; i++) {
        const li = $("<li>").text(i).on('click', function () {
            dynamicStore(url, i);
        });
        if (i === currentPage) {
            li.addClass('active');
        }
        pagination.append(li);
    }
}