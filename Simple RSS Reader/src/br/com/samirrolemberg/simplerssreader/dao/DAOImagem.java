package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.Connection;
import br.com.samirrolemberg.simplerssreader.model.Imagem;
import br.com.samirrolemberg.simplerssreader.model.Feed;

public class DAOImagem extends Connection {

	public final static String TABLE = "imagem";

	public DAOImagem(Context context) {
		super(context);
	}
	
	public long inserir(Imagem imagem, long idFeed){
		ContentValues values = new ContentValues();
		values.put("descricao", imagem.getDescricao());
		values.put("link", imagem.getLink());
		values.put("titulo", imagem.getTitulo());
		values.put("url", imagem.getUrl());
		values.put("idFeed", idFeed);
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}

	public List<Imagem> listarTudo(Feed feed){
		List<Imagem> imagens = new ArrayList<Imagem>();
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idFeed = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Imagem imagem = new Imagem.Builder()
				.idImagem(cursor.getLong(cursor.getColumnIndex("idImagem")))
				.feed(feed)
				.descricao(cursor.getString(cursor.getColumnIndex("descricao")))
				.link(cursor.getString(cursor.getColumnIndex("link")))
				.titulo(cursor.getString(cursor.getColumnIndex("titulo")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
				imagens.add(imagem);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return imagens;
	}

	public Imagem buscar(Feed feed){
		Imagem imagem = null;
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idFeed = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				imagem = new Imagem.Builder()
				.idImagem(cursor.getLong(cursor.getColumnIndex("idImagem")))
				.feed(feed)
				.descricao(cursor.getString(cursor.getColumnIndex("descricao")))
				.link(cursor.getString(cursor.getColumnIndex("link")))
				.titulo(cursor.getString(cursor.getColumnIndex("titulo")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return imagem;
	}

	public void remover(Feed feed){
		String[] args = {feed.getIdFeed()+""};
		getWritableDatabase().delete(TABLE, "idFeed=?", args);
	}

}
