package br.com.samirrolemberg.simplerssreader.tasks;

import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOImagem;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.services.ExcluirFeedService;

public class ExcluirFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private int id = 0;
	private Feed feed = null;
	private int estimativa = 0;
	//private int atual = 0;
	private ExcluirFeedService service;
	private Intent intent;
	
//	public ExcluirFeedTask(Context context, Feed feed){
//		this.context = context;
//		this.id = new Random().nextInt(999);//colocar parametero
//		this.feed = feed;
//	}

	public ExcluirFeedTask(Context context, ExcluirFeedService service, Intent intent, Feed feed){
		this.context = context;
		this.id = 45;//colocar parametero
		this.feed = feed;
		this.service = service;
		this.intent = intent;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Removendo "+feed.getTitulo())
		.setContentText("Removendo registros do feed.")
		.setSmallIcon(R.drawable.ic_action_rss_icon_bola_transparente);
		estimativa = estimativaDosFor();
	}
	@Override
	protected Feed doInBackground(String... params) {
		//addFeed();
		Log.w("OUTPUT-TEST", "estimativa "+estimativa);
		excluir(getContext(), feed);
		Log.w("OUTPUT-TEST", "atual "+estimativa);
		// When the loop is finished, updates the notification
        mBuilder.setContentText("Feed removido.")
        // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());

		return null;
	}
	@Override
	protected void onPostExecute(Feed result) {
		super.onPostExecute(result);
		if (service!=null) {
			Log.i("MY-SERVICES", "EscluirFeedTask - TRY STOP");
			this.cancel(false);
			service.stopService(intent);
		}
	}
	protected Context getContext(){
		return this.context;
	}

	private int estimativaDosFor(){
		int i = 0;//pq pode adicionar apenas o feed sem nada (!)
		if (feed.getCategorias()!=null) {
			i += feed.getCategorias().size();
		}
		if (feed.getPosts()!=null) {
			i += feed.getPosts().size();
			for (Post post : feed.getPosts()) {
				if (post.getAnexos()!=null) {
					i += post.getAnexos().size();					
				}
				if (post.getCategorias()!=null) {
					i += post.getCategorias().size();					
				}
				if (post.getConteudos()!=null) {
					i += post.getConteudos().size();					
				}
			}
		}
		return i;
	}
	private void excluir(Context context, Feed feed){
		mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());

		//DAOFeed daoFeed = new DAOFeed(context); // ja reoveu na mainactv
		DAOPost daoPost = new DAOPost(context);
		DAODescricao daoDescricao = new DAODescricao(context);
		DAOImagem daoImagem = new DAOImagem(context);
		DAOAnexo daoAnexo = new DAOAnexo(context);
		DAOCategoria daoCategoria = new DAOCategoria(context);
		DAOConteudo daoConteudo = new DAOConteudo(context);
		
		List<Post> posts = daoPost.listarTudo(feed);
		
		//daoFeed.remover(feed);
		daoCategoria.remover(feed);
		daoImagem.remover(feed);
		for (Post post : posts) {
			daoPost.remover(post);//resultado da remoção total
            daoDescricao.remover(post);
            daoAnexo.remover(post);
            daoCategoria.remover(post);
            daoConteudo.remover(post);
		}
		
		//daoFeed.DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		
	}
}
