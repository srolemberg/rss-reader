package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.model.Anexo;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAOAnexo {

	public final static String TABLE = "anexo";
	private SQLiteDatabase database = null;

	public DAOAnexo(Context context) {
		super();
		database = DatabaseManager.getInstance().openDatabase();
	}
	
	public long inserir(Anexo anexo, long idPost){
		ContentValues values = new ContentValues();
		values.put("tamanho", anexo.getTamanho());
		values.put("tipo", anexo.getTipo());
		values.put("url", anexo.getUrl());
		values.put("idPost", idPost);
		long id = database.insert(TABLE, null, values);
		
		return id;
		
	}
	public int atualizaAcesso(Post post, int acesso){
		ContentValues values = new ContentValues();
		values.put("acesso", acesso);
		String[] args = {post.getIdPost()+""};

		return database.update(TABLE, values, "idPost = ?", args);
	}

	public List<Anexo> listarTudo(Post post){
		List<Anexo> anexos = new ArrayList<Anexo>();
		try {
			String[] args = {post.getIdPost()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idPost = ?");
			Cursor cursor = database.rawQuery(sql.toString(), args);
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
			Cursor cursor = database.rawQuery(sql.toString(), args);
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

	public int remover(Post post){
		String[] args = {post.getIdPost()+""};
		return database.delete(TABLE, "idPost=?", args);
	}

	public long existe(Anexo anexo, long idPost) {
		//select idAnexo id from anexo where idPost = ? and url = ?
		long retorno = 0;
		try {
			String[] args = {idPost+"",anexo.getUrl()};
			StringBuffer sql = new StringBuffer();
			sql.append("select idAnexo id from "+TABLE+" where idPost = ? and url = ?");
			Cursor cursor = database.rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				retorno = cursor.getLong(cursor.getColumnIndex("id"));
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}

		return retorno;
	}

	public int atualiza(Anexo anexo, long idAnexo) {
		ContentValues values = new ContentValues();
		values.put("tamanho", anexo.getTamanho());
		values.put("tipo", anexo.getTipo());
		values.put("url", anexo.getUrl());
		String[] args = {idAnexo+""};

		return database.update(TABLE, values, "idAnexo = ?", args);
	}

}
