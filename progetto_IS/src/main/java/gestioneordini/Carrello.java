package gestioneordini;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gestioneprodotti.Prodotto;

public class Carrello implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Prodotto> cart;
	
	public Carrello() {
		cart = new ArrayList<>();
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
}
