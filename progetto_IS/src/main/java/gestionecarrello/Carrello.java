package gestionecarrello;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import gestioneprodotti.Prodotto;

public class Carrello implements Serializable {
	/**
	 * 
	 */
	private Integer idcarrello;
	private static final long serialVersionUID = 1L;
	private List<Prodotto> cart;
	
	public Carrello() {
		cart = new ArrayList<>();
		idcarrello = -1;
	}
	

	public void removeProduct(Prodotto p) {
		int code = p.getCode();
		Iterator<Prodotto> iterator = cart.iterator();
		while (iterator.hasNext()) {
		    Prodotto prod = iterator.next();
		    if (prod.getCode() == code) {
		        iterator.remove();
		    }
		}
	}
	
	public int getSize() {
		return cart.size();
	}
	
	public Prodotto getProduct(Prodotto p) {
		return cart.get(p.getCode());		
	}
	
	public void addProduct(Prodotto product) {
		for(Prodotto prod : cart) {
			if(prod.getCode() == product.getCode()) {
				return;
			}
		}
		cart.add(product);
	}
	
	public void deleteProduct(Prodotto product) {
		for(Prodotto prod : cart) {
			if(prod.getCode() == product.getCode()) {
				cart.remove(prod);
				break;
			}
		}
 	}
	
	public Boolean isEmpty() {
		if( cart == null || cart.size() == 0)
			return true;
		else 
			return false;
	}
	
	public void clearCart() {
		cart.clear();
	}
	
	public List<Prodotto> getAllProduct(){
		return cart;
	}

	public Integer getIdcarrello() {
		return idcarrello;
	}

	public void setIdcarrello(Integer idcarrello) {
		this.idcarrello = idcarrello;
	}


	@Override
	public int hashCode() {
		return Objects.hash(cart, idcarrello);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carrello other = (Carrello) obj;
		return Objects.equals(cart, other.cart) && Objects.equals(idcarrello, other.idcarrello);
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Carrello [idcarrello=").append(idcarrello).append(", cart=[");
	    for (Prodotto prodotto : cart) {
	        sb.append(prodotto.toString()).append(", ");
	    }
	    sb.append("]]");
	    return sb.toString();
	}


}
