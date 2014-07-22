package br.com.samirrolemberg.simplerssreader.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class ExcluirPostTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private int id = 0;
	private Post post = null;
	
	public ExcluirPostTask(Context context, Post post){
		this.context = context;
		this.id = 60;//colocar parametero
		this.post = post;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Removendo "+post.getTitulo())
		.setContentText("Removendo registros do post.")
		.setSmallIcon(R.drawable.ic_action_rss_icon_bola_transparente);
	}
	@Override
	protected Feed doInBackground(String... params) {
		excluir(getContext(), post);
        mBuilder.setContentText("Post removido.")
        // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());

		return null;
	}
	protected Context getContext(){
		return this.context;
	}

	private void excluir(Context context, Post post){
		mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());

		//DAOPost daoPost = new DAOPost(context); já removeu na outra tela
		DAOAnexo daoAnexo = new DAOAnexo(context);
		DAOCategoria daoCategoria = new DAOCategoria(context);
		DAOConteudo daoConteudo = new DAOConteudo(context);
		DAODescricao daoDescricao = new DAODescricao(context);
		
		daoAnexo.remover(post);
		daoCategoria.remover(post);
		daoConteudo.remover(post);
		daoDescricao.remover(post);
		
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
	}

}
