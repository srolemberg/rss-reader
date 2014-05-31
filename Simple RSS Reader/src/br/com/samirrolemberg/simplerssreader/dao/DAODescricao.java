package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.Connection;
import br.com.samirrolemberg.simplerssreader.model.Descricao;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAODescricao extends Connection {

	public final static String TABLE = "descricao";

	public DAODescricao(Context context) {
		super(context);
	}
	
	public long inserir(Descricao descricao, long idPost){
		ContentValues values = new ContentValues();
		values.put("modo", descricao.getModo());
		values.put("tipo", descricao.getTipo());
		values.put("valor", descricao.getValor());
		values.put("idPost", idPost);
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}

	public long size(Post post){
		long resultado = 0;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select count(idDescricao) total from "+TABLE+" where idPost = ?");
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
			sql.append("select idDescricao from "+TABLE+" where idPost = ? limit 1");
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
	
	public List<Descricao> listarTudo(Post post){
		List<Descricao> descricoes = new ArrayList<Descricao>();
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Descricao descricao = new Descricao.Builder()
				.idDescricao(cursor.getLong(cursor.getColumnIndex("idDescricao")))
				.modo(cursor.getString(cursor.getColumnIndex("modo")))
				.post(post)
				.tipo(cursor.getString(cursor.getColumnIndex("tipo")))
				.valor(cursor.getString(cursor.getColumnIndex("valor")))
				.build();
				descricoes.add(descricao);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return descricoes;
	}

	public Descricao buscar(Post post){
		Descricao descricao = null;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				descricao = new Descricao.Builder()
				.idDescricao(cursor.getLong(cursor.getColumnIndex("idDescricao")))
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
		return descricao;
	}

	public int remover(Post post){
		String[] args = {post.getIdPost()+""};
		return getWritableDatabase().delete(TABLE, "idPost=?", args);
	}

}
