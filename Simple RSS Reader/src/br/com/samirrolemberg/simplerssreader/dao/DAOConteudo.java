package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.Connection;
import br.com.samirrolemberg.simplerssreader.model.Conteudo;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAOConteudo extends Connection {

	public final static String TABLE = "conteudo";

	public DAOConteudo(Context context) {
		super(context);
	}
	
	public long inserir(Conteudo conteudo, long idPost){
		ContentValues values = new ContentValues();
		values.put("modo", conteudo.getModo());
		values.put("tipo", conteudo.getTipo());
		values.put("valor", conteudo.getValor());
		values.put("idPost", idPost);
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}

	public long size(Post post){
		long resultado = 0;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select count(idConteudo) total from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				resultado = cursor.getLong(cursor.getColumnIndex("total"));
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		
		return resultado;
	}

	public boolean isExist(Post post){
		boolean resultado = false;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select idConteudo from "+TABLE+" where idPost = ? limit 1");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				resultado = true;
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		
		return resultado;
	}

	public List<Conteudo> listarTudo(Post post){
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Conteudo conteudo = new Conteudo.Builder()
				.idConteudo(cursor.getLong(cursor.getColumnIndex("idConteudo")))
				.modo(cursor.getString(cursor.getColumnIndex("modo")))
				.post(post)
				.tipo(cursor.getString(cursor.getColumnIndex("tipo")))
				.valor(cursor.getString(cursor.getColumnIndex("valor")))
				.build();
				conteudos.add(conteudo);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return conteudos;
	}

	public Conteudo buscar(Post post){
		Conteudo conteudo = null;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				conteudo = new Conteudo.Builder()
				.idConteudo(cursor.getLong(cursor.getColumnIndex("idConteudo")))
				.modo(cursor.getString(cursor.getColumnIndex("modo")))
				.post(post)
				.tipo(cursor.getString(cursor.getColumnIndex("tipo")))
				.valor(cursor.getString(cursor.getColumnIndex("valor")))
				.build();
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return conteudo;
	}

	public int remover(Post post){
		String[] args = {post.getIdPost()+""};
		return getWritableDatabase().delete(TABLE, "idPost=?", args);
	}

}
