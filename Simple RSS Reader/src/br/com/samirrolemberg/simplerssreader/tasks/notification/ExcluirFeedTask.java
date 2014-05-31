package br.com.samirrolemberg.simplerssreader.tasks.notification;

import java.util.List;
import java.util.Random;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOImagem;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class ExcluirFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private int id = 0;
	private Feed feed = null;
	private int estimativa = 0;
	//private int atual = 0;
	
	public ExcluirFeedTask(Context context, Feed feed){
		this.context = context;
		this.id = new Random().nextInt(999);//colocar parametero
		this.feed = feed;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Removendo "+feed.getTitulo())
		.setContentText("Removendo registros do feed.")
		.setSmallIcon(android.R.drawable.arrow_down_float);
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
		
		//daoFeed.close();
		daoPost.close();
		daoDescricao.close();
		daoImagem.close();
		daoAnexo.close();
		daoCategoria.close();
		daoConteudo.close();
		
	}

//	private void addFeed(){
//		DAOFeed daoFeed = new DAOFeed(getContext());
//		long idFeed = daoFeed.inserir(feed);
//		//int estimativa = estimativaDosFor();
//		int atual = 0;
//		if (idFeed!=-1) {
//			DAOPost daoPost = new DAOPost(getContext());
//			DAODescricao daoDescricao = new DAODescricao(getContext());
//			DAOConteudo daoConteudo = new DAOConteudo(getContext());
//			DAOAnexo daoAnexo = new DAOAnexo(getContext());
//			DAOCategoria daoCategoria = new DAOCategoria(getContext());
//			DAOImagem daoImagem = new DAOImagem(getContext());
//			
//			if (feed.getCategorias()!=null) {
//				//pega a categoria do feed
//				for (int i = 0; i < feed.getCategorias().size(); i++) {
//					//objeto de Feed ainda não tem id. Cria um objeto apenas com o id retornado do insert
//					daoCategoria.inserir(feed.getCategorias().get(i), (new Feed.Builder().idFeed(idFeed).build()));
//	                atual ++;
//					mBuilder.setProgress(estimativa, atual, false);
//	                mNotifyManager.notify(id, mBuilder.build());
//					
//				}
//			}
//			if (feed.getImagem()!=null) {
//				//pega a imagem do feed
//				daoImagem.inserir(feed.getImagem(), idFeed);
//			}
//			if (feed.getPosts()!=null) {
//				for (int i = 0; i < feed.getPosts().size(); i++) {
//	                atual ++;
//	                mBuilder.setProgress(estimativa, atual, false);
//	                mNotifyManager.notify(id, mBuilder.build());
//	                
//					long idPost = daoPost.inserir(feed.getPosts().get(i), idFeed);
//					//pega as categorias dos posts
//					if (feed.getPosts().get(i).getCategorias()!=null) {
//						//pega a categoria do feed
//						for (int j = 0; j < feed.getPosts().get(i).getCategorias().size(); j++) {
//							//objeto de Post ainda não tem id. Cria um objeto apenas com o id retornado do insert
//							daoCategoria.inserir(feed.getPosts().get(i).getCategorias().get(j), (new Post.Builder().idPost(idPost).build()));
//			                atual ++;
//							mBuilder.setProgress(estimativa, atual, false);
//			                mNotifyManager.notify(id, mBuilder.build());
//
//						}
//						
//					}
//					//pega os anexos do post
//					if (feed.getPosts().get(i).getAnexos()!=null) {
//						for (int j = 0; j < feed.getPosts().get(i).getAnexos().size(); j++) {
//							daoAnexo.inserir(feed.getPosts().get(i).getAnexos().get(j), idPost);							
//			                atual ++;
//			                mBuilder.setProgress(estimativa, atual, false);
//			                mNotifyManager.notify(id, mBuilder.build());
//						}
//					}
//					//para cada post tem uma ou mais descrições
//					if (feed.getPosts().get(i).getDescricao()!=null) {
//						daoDescricao.inserir(feed.getPosts().get(i).getDescricao(), idPost);								
//					}
//					//para cada post tem uma ou mais conteudos
//					if (feed.getPosts().get(i).getConteudos()!=null) {
//						for (int j = 0; j < feed.getPosts().get(i).getConteudos().size(); j++) {
//							daoConteudo.inserir(feed.getPosts().get(i).getConteudos().get(j), idPost);																	
//			                atual ++;
//			                mBuilder.setProgress(estimativa, atual, false);
//			                mNotifyManager.notify(id, mBuilder.build());
//						}
//					}
//
//				}
//			}
//			Log.w("OUTPUT-TEST", estimativa+" estimativa");
//			Log.w("OUTPUT-TEST", atual+" atual");
//			daoAnexo.close();
//			daoCategoria.close();
//			daoConteudo.close();
//			daoDescricao.close();
//			daoImagem.close();
//			daoPost.close();
//		}
//		daoFeed.close();
//		//TODO: JOGAR O PROCESSO DE ADIÇÃO EM BACKGROUND NUMA NOTIFICAÇÃO.
//	}
}