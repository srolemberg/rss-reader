package br.com.samirrolemberg.simplerssreader.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class DAOPost {

	public final static String TABLE = "post";
	private SQLiteDatabase database = null;

	public DAOPost(Context context) {
		super();
		database = DatabaseManager.getInstance().openDatabase();
	}
	
	public long inserir(Post post, long idFeed){
		ContentValues values = new ContentValues();
		values.put("autor", post.getAutor());
		values.put("link", post.getLink());
		values.put("data_publicacao", post.getData_publicacao()==null?null:post.getData_publicacao().getTime());
		values.put("titulo", post.getTitulo());
		values.put("data_atualizacao", post.getData_atualizacao()==null?null:post.getData_atualizacao().getTime());
		values.put("link_URI", post.getLink_URI());
		values.put("idFeed", idFeed);
		long id = database.insert(TABLE, null, values);
		
		return id;
		
	}
	public int atualizaAcesso(Post post, int acesso){
		ContentValues values = new ContentValues();
		values.put("acesso", acesso);
		String[] args = {post.getIdPost()+""};

		return database.update(TABLE, values, "idPost = ?", args);
	}

	public List<Post> listarTudo(Feed feed){
		List<Post> posts = new ArrayList<Post>();
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select * from "+TABLE+" where idFeed = ?");
			Cursor cursor = database.rawQuery(sql.toString(), args);
			while (cursor.moveToNext()) {
				Post post = new Post.Builder()
				.idPost(cursor.getLong(cursor.getColumnIndex("idPost")))
				.autor(cursor.getString(cursor.getColumnIndex("autor")))
				.link(cursor.getString(cursor.getColumnIndex("link")))
				.data_publicacao(new Date(cursor.getLong(cursor.getColumnIndex("data_publicacao"))))
				.titulo(cursor.getString(cursor.getColumnIndex("titulo")))
				.data_atualizacao(new Date(cursor.getLong(cursor.getColumnIndex("data_atualizacao"))))
				.link_URI(cursor.getString(cursor.getColumnIndex("link_URI")))
				.feed(feed)
				.build();
				posts.add(post);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		return posts;
	}

	public long size(Feed feed){
		long resultado = 0;
		try {
			String[] args = {feed.getIdFeed()+""};
			StringBuffer sql = new StringBuffer();
			sql.append("select count(idPost) total from "+TABLE+" where idFeed = ?");
			Cursor cursor = database.rawQuery(sql.toString(), args);
			if (cursor.moveToNext()) {
				resultado = cursor.getLong(cursor.getColumnIndex("total"));
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("DAOs", e.getLocalizedMessage(),e);
		}
		
		return resultado;
	}

	public int remover(Post post){
		String[] args = {post.getIdPost()+""};
		return database.delete(TABLE, "idPost=?", args);
	}

}
