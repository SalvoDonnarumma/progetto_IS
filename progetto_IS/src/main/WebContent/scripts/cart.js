function dynamicCart(url){
	$.ajax({
		url : url,
		type: 'GET',
		contentType : 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHTML = "";
		let contenutoHTML2 = "";
		
		if( response.length == 0 ){
			contenutoHTML +=  "<tr>";
			contenutoHTML +=  "<td colspan=\"6\">No products available</td>";
			contenutoHTML +=  "</tr>";
			
			contenutoHTML2 += "<p> Puoi acquistare articoli dal nostro store! </p>";
     		contenutoHTML2 +=	 "<a href=\"store.jsp\"> <button >Torna allo store</button> </a>";
		} else {
				for(const bean of response){
				  	contenutoHTML += "<tr>";
				  	contenutoHTML += "<td><img class=\"front\"  src='./getPicture?id="+bean.code+"\" onerror=\"this.src='img/nophoto.png'\">";
		          	contenutoHTML += "<br>"+ bean.name+"</td>";
		          	contenutoHTML += "<td  class=\"d-none\">"+ bean.categoria +"</td>";
		          	contenutoHTML += "<td> <input type=number min=1 max=\"10\" class=quantita onchange=totaleParziale() value=\"1\"> </td>";
		          	contenutoHTML += "<td> <select class=\"size\">";
		          	//faccio visualizzare solo le taglie disponibile per ogni specifico prodotto
			          	if( bean.taglie.quantitaM>0 )
			          			contenutoHTML += "<option value=\"M\"> M </option>";
			          	if( bean.taglie.quantitaL>0 )
			          			contenutoHTML += "<option value=\"L\"> L </option>";		
					  	if( bean.taglie.quantitaXL>0 )
			          			contenutoHTML += "<option value=\"XL\"> XL </option>";
					  	if( bean.taglie.quantitaXXL>0)
					  			contenutoHTML += "<option value=\"XXL\"> XXL </option>";
				  	contenutoHTML += "</select>";
		          	contenutoHTML += "<td class=\"d-none\">"+"<p class=price>"+bean.price.toFixed(2)+"&#8364</p>"+"</td>";
		          	contenutoHTML += "<td class=totProd>"+"</td>";
		          	contenutoHTML += "<td> <button id='" + bean.code + "'onclick=eliminaRiga(this)><i  class='bx bx-trash'></i></button>";
		         	contenutoHTML += "</tr>"; 
		      }
		    		contenutoHTML += "<tr>";
      			 	contenutoHTML += "<th colspan=\"6\">";
       				contenutoHTML += "</th>;"
      	  			contenutoHTML += "<th>";
       		        contenutoHTML += "<div id=\"cassa\" s>";
	 		        contenutoHTML += "<h5>TOTALE</h5>";
	 			    contenutoHTML += "<div class=\"totale\">";
	 				contenutoHTML += "<h6> Prodotti: </h6>";
	 				contenutoHTML += "<div style=\"display: inline-block;\"> <p id=\"tot\" class=\"tot\">totale</p>&euro; </div>";
	 				contenutoHTML += "</div>";
					contenutoHTML += "</div>"; 
       				contenutoHTML += "</th>";
     			  	contenutoHTML += "</tr>";    
     			  	
     				contenutoHTML2 +=	 "<a href=\"store.jsp\"> <button >Torna allo store</button> </a>";
      				contenutoHTML2 +=	 "<a id=\"link\" onClick=\"addValuesToLink()\" href=\"AcquistaProdotti?action=readAll&fromStore=get2\"><button class=\"pagamento\">Procedi al pagamento</button> </a>";	   
		} 
		$("#cart").empty();
		$("#cart").append(contenutoHTML);
		$("#checkout").empty();
		$("#checkout").append(contenutoHTML2);
		totaleParziale(); 
	});
} 

function addValuesToLink(){
		let product = document.getElementById("cart");
		let link = document.getElementById("link");	
		let elem2 = product.getElementsByClassName('quantita');
		let elem1 = product.getElementsByClassName('size');
		let totale = document.getElementById("tot");
		link.href=link.href.substring(0, 78);
		console.log(totale.textContent.trim());
		
		for(let i = 0; i < elem2.length; i++){
			console.log(link.href+="&qnt"+i+"=");
			console.log(link.href+=elem2[i].value);
			console.log(link.href+="&sz"+i+"=");
			console.log(link.href+=elem1[i].value);
		}
		
		link.href+="&tot="+(totale.textContent);
}

function totaleParziale(){
	let product, elem2, costo, quantita, totParz, tot = 0;
		
	product = document.getElementById("cart");
	elem2 = product.getElementsByClassName('quantita');
	let elementi = document.querySelectorAll('p.price');
	console.log(elementi);
	
	for(let i = 0; i < elementi.length; i++){
		totParz = 0;
		
		let elementoDesiderato = elementi[i]; 
		let valore = elementoDesiderato.textContent.trim();
    	console.log(valore);
    	costo = parseFloat(valore);
		quantita = parseInt(elem2[i].value)
		console.log(elem2[i].value);
		totParz += costo * quantita;
		tot += totParz;
		
		product.getElementsByClassName("totProd")[i].innerHTML = "&euro;" + totParz.toFixed(2); 
	}
	
	let cassa;
	
	cassa = document.getElementById("cassa");
	cassa.getElementsByClassName("tot")[0].innerHTML = tot.toFixed(2);
}

function eliminaRiga(button) {
  let row = button.parentNode.parentNode;
  let idProdotto = button.getAttribute("id");
  console.log(idProdotto);
  let pathArray = window.location.pathname.split('/');
  let contextPath = '/' + pathArray[1];
  let url = contextPath + "/RemoveProductFromCart";
  console.log(url);
  $.ajax({
    url: url,
    type: 'POST',
    data:  { id: idProdotto },
    success: function(response) {
      // Rimuovi la riga del prodotto dal carrello nell'interfaccia utente
      row.parentNode.removeChild(row);

	  dynamicCart(contextPath+"/CartServlet");
      // Aggiorna i totali
      totaleParziale();
    },
    error: function(xhr, status, error) {
      console.error(error);
    }
  });
}
