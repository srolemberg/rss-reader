package br.com.samirrolemberg.simplerssreader.tasks.notification;

import java.util.List;
import java.util.Random;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.u.Executando;

public class LimparConteudoFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private int id = 0;
	private Feed feed = null;
	//private int atual = 0;
	
	public LimparConteudoFeedTask(Context context, Feed feed){
		this.context = context;
		this.id = new Random().nextInt(999);//colocar parametero
		this.feed = feed;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Limpando "+feed.getTitulo())
		.setContentText("Limpando o conteúdo do feed.")
		.setSmallIcon(android.R.drawable.arrow_down_float);
		Executando.ADICIONAR_FEED.put(feed.getIdFeed()+feed.getRss(), 1);
	}
	@Override
	protected Feed doInBackground(String... params) {
		//addFeed();
		limpar(getContext(), feed);
		// When the loop is finished, updates the notification
        mBuilder.setContentText("Conteúdo removido.")
        // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());

		return null;
	}
	@Override
	protected void onPostExecute(Feed result) {
		super.onPostExecute(result);
				
		Executando.ADICIONAR_FEED.remove(feed.getIdFeed()+feed.getRss());
		Toast.makeText(getContext(), feed.getTitulo()+" foi limpo com sucesso.", Toast.LENGTH_SHORT).show();
	}

	protected Context getContext(){
		return this.context;
	}

	private void limpar(Context context, Feed feed){
		mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());
		
		DAOPost daoPost = new DAOPost(context);
		//TODO: REFAZER NO FUTURO
		//obter todos os ids de posts
		List<Post> posts = daoPost.listarTudo(feed);
		DAOAnexo daoAnexo = new DAOAnexo(context);
		DAOCategoria daoCategoria = new DAOCategoria(context);
		DAOConteudo daoConteudo = new DAOConteudo(context);
		DAODescricao daoDescricao = new DAODescricao(context);
		for (Post post : posts) {
			daoPost.remover(post);
			daoAnexo.remover(post);
			daoCategoria.remover(post);
			daoConteudo.remover(post);
			daoDescricao.remover(post);
		}
		daoAnexo.close();
		daoCategoria.close();
		daoConteudo.close();
		daoPost.close();
		daoPost.close();

	}
	
	
}
