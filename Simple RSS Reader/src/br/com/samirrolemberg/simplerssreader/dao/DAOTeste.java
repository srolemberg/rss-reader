package br.com.samirrolemberg.simplerssreader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;

public class DAOTeste {

	private SQLiteDatabase database = null;
	public DAOTeste(Context context){
		super();
		database = DatabaseManager.getInstance().openDatabase();
	}
	
	public void insert(){
		//database.insert(table, nullColumnHack, values);
	}
}
