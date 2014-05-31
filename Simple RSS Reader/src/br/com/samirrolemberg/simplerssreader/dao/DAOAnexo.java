package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.Connection;
import br.com.samirrolemberg.simplerssreader.model.Anexo;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAOAnexo extends Connection {

	public final static String TABLE = "anexo";

	public DAOAnexo(Context context) {
		super(context);
	}
	
	public long inserir(Anexo anexo, long idPost){
		ContentValues values = new ContentValues();
		values.put("tamanho", anexo.getTamanho());
		values.put("tipo", anexo.getTipo());
		values.put("url", anexo.getUrl());
		values.put("idPost", idPost);
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}

	public List<Anexo> listarTudo(Post post){
		List<Anexo> anexos = new ArrayList<Anexo>();
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Anexo anexo = new Anexo.Builder()
				.idAnexo(cursor.getLong(cursor.getColumnIndex("idAnexo")))
				.tamanho(cursor.getLong(cursor.getColumnIndex("tamanho")))
				.post(post)
				.tipo(cursor.getString(cursor.getColumnIndex("tipo")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
				anexos.add(anexo);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return anexos;
	}

	public Anexo buscar(Post post){
		Anexo anexo = null;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				anexo = new Anexo.Builder()
				.idAnexo(cursor.getLong(cursor.getColumnIndex("idAnexo")))
				.tamanho(cursor.getLong(cursor.getColumnIndex("tamanho")))
				.post(post)
				.tipo(cursor.getString(cursor.getColumnIndex("tipo")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return anexo;
	}

	public void remover(Post post){
		String[] args = {post.getIdPost()+""};
		getWritableDatabase().delete(TABLE, "idPost=?", args);
	}

}
