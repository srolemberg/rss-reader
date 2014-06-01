package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.Connection;
import br.com.samirrolemberg.simplerssreader.model.Categoria;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAOCategoria extends Connection {

	public final static String TABLE = "categoria";

	public DAOCategoria(Context context) {
		super(context);
	}
	
	public long inserir(Categoria categoria, Post idPost){
		ContentValues values = new ContentValues();
		values.put("nome", categoria.getNome());
		values.put("url", categoria.getUrl());
		values.put("idPost", idPost.getIdPost());
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}
	public long inserir(Categoria categoria, Feed idFeed){
		ContentValues values = new ContentValues();
		values.put("nome", categoria.getNome());
		values.put("url", categoria.getUrl());
		values.put("idFeed", idFeed.getIdFeed());
		long id = getWritableDatabase().insert(TABLE, null, values);
		
		return id;
		
	}
	public int atualizaAcesso(Feed feed, int acesso){
		ContentValues values = new ContentValues();
		values.put("acesso", acesso);
		String[] args = {feed.getIdFeed()+""};

		return getWritableDatabase().update(TABLE, values, "idFeed = ?", args);
	}
	public int atualizaAcesso(Post post, int acesso){
		ContentValues values = new ContentValues();
		values.put("acesso", acesso);
		String[] args = {post.getIdPost()+""};

		return getWritableDatabase().update(TABLE, values, "idPost = ?", args);
	}

	public List<Categoria> listarTudo(Post post){
		List<Categoria> categorias = new ArrayList<Categoria>();
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Categoria categoria = new Categoria.Builder()
				.idCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")))
				.post(post)
				.nome(cursor.getString(cursor.getColumnIndex("nome")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
				categorias.add(categoria);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return categorias;
	}
	public List<Categoria> listarTudo(Feed feed){
		List<Categoria> categorias = new ArrayList<Categoria>();
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idFeed = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Categoria categoria = new Categoria.Builder()
				.idCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")))
				.feed(feed)
				.nome(cursor.getString(cursor.getColumnIndex("nome")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
				categorias.add(categoria);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return categorias;
	}

	public Categoria buscar(Post post){
		Categoria categoria = null;
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				categoria = new Categoria.Builder()
				.idCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")))
				.post(post)
				.nome(cursor.getString(cursor.getColumnIndex("nome")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return categoria;
	}
	public Categoria buscar(Feed feed){
		Categoria categoria = null;
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idFeed = ?");
			Cursor cursor = getWritableDatabase().rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				categoria = new Categoria.Builder()
				.idCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")))
				.feed(feed)
				.nome(cursor.getString(cursor.getColumnIndex("nome")))
				.url(cursor.getString(cursor.getColumnIndex("url")))
				.build();
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return categoria;
	}

	public int remover(Post post){
		String[] args = {post.getIdPost()+""};
		return getWritableDatabase().delete(TABLE, "idPost=?", args);
	}
	public int remover(Feed feed){
		String[] args = {feed.getIdFeed()+""};
		return getWritableDatabase().delete(TABLE, "idFeed=?", args);
	}

}
