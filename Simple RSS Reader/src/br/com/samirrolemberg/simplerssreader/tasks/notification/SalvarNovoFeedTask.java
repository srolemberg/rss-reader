package br.com.samirrolemberg.simplerssreader.tasks.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOFeed;
import br.com.samirrolemberg.simplerssreader.dao.DAOImagem;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.u.Executando;

public class SalvarNovoFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private int id = 0;
	private Feed feed = null;
	private int estimativa = 0;
	private long idFeed = 0;
	private List<Long> idsPost = null;
	private int atual = 0;

	private DAOFeed daoFeed = null;
	private DAOPost daoPost = null;
	private DAODescricao daoDescricao = null;
	private DAOConteudo daoConteudo = null;
	private DAOAnexo daoAnexo = null;
	private DAOCategoria daoCategoria = null;
	private DAOImagem daoImagem = null;

	//TODO: POR ALGUM MOTIVO, DEPOIS DA ATUALIZAÇÃO DO ACESSO, UM DOS VALORES DO PROGRESSO ESTÁ MENOR.
	public SalvarNovoFeedTask(Context context, Feed feed, long idFeed){
		this.context = context;
		this.id = new Random().nextInt(999);//colocar parametero
		this.feed = feed;
		this.idFeed = idFeed;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Adicionando "+feed.getTitulo())
		.setContentText("Adicionando novos registros.")
		.setSmallIcon(android.R.drawable.arrow_down_float);
		estimativa = estimativaDosFor()*2;
		Executando.ADICIONAR_FEED.put(idFeed+feed.getRss(), 1);
	}
	@Override
	protected Feed doInBackground(String... params) {
		addFeed();
		// When the loop is finished, updates the notification
        mBuilder.setContentText("Novo Feed adicionado.")
        // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());

		return null;
	}
	@Override
	protected void onPostExecute(Feed result) {
		super.onPostExecute(result);
				
		Executando.ADICIONAR_FEED.remove(idFeed+feed.getRss());
		Log.w("OUTPUT-TEST", estimativa+" estimativa");
		Log.w("OUTPUT-TEST", atual+" atual");
		Toast.makeText(getContext(), feed.getTitulo()+" foi adicionado com sucesso.", Toast.LENGTH_SHORT).show();
	}

	protected Context getContext(){
		return this.context;
	}
	private void atualiza(){
		Feed idf= new Feed.Builder().idFeed(idFeed).build();
		daoFeed.atualizaAcesso(idf, 1);
		
		atual+=	daoCategoria.atualizaAcesso(idf, 1);
		mBuilder.setProgress(estimativa, atual, false);
        mNotifyManager.notify(id, mBuilder.build());

		daoImagem.atualizaAcesso(idf, 1);
		for (Long idPost : idsPost) {
			Post post = new Post.Builder().idPost(idPost).build();
			
			atual+=	daoPost.atualizaAcesso(post, 1);

	        mBuilder.setProgress(estimativa, atual, false);
	        mNotifyManager.notify(id, mBuilder.build());

			atual+=	daoCategoria.atualizaAcesso(post, 1);
	        mBuilder.setProgress(estimativa, atual, false);
	        mNotifyManager.notify(id, mBuilder.build());
			
			daoDescricao.atualizaAcesso(post, 1);
			
			atual+=	daoConteudo.atualizaAcesso(post, 1);
	        mBuilder.setProgress(estimativa, atual, false);
	        mNotifyManager.notify(id, mBuilder.build());
			
			atual+=	daoAnexo.atualizaAcesso(post, 1);
	        mBuilder.setProgress(estimativa, atual, false);
	        mNotifyManager.notify(id, mBuilder.build());
		}
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
	private void addFeed(){
		daoFeed = new DAOFeed(getContext());
		//long idFeed = daoFeed.inserir(feed);
		//int estimativa = estimativaDosFor();
		if (idFeed!=-1) {
			daoPost = new DAOPost(getContext());
			daoDescricao = new DAODescricao(getContext());
			daoConteudo = new DAOConteudo(getContext());
			daoAnexo = new DAOAnexo(getContext());
			daoCategoria = new DAOCategoria(getContext());
			daoImagem = new DAOImagem(getContext());
			
			if (feed.getCategorias()!=null) {
				//pega a categoria do feed
				for (int i = 0; i < feed.getCategorias().size(); i++) {
					//objeto de Feed ainda não tem id. Cria um objeto apenas com o id retornado do insert
					daoCategoria.inserir(feed.getCategorias().get(i), (new Feed.Builder().idFeed(idFeed).build()));
	                atual ++;
					mBuilder.setProgress(estimativa, atual, false);
	                mNotifyManager.notify(id, mBuilder.build());
					
				}
			}
			if (feed.getImagem()!=null) {
				//pega a imagem do feed
				daoImagem.inserir(feed.getImagem(), idFeed);
			}
			if (feed.getPosts()!=null) {
				idsPost = new ArrayList<Long>();
				for (int i = 0; i < feed.getPosts().size(); i++) {
	                atual ++;
	                mBuilder.setProgress(estimativa, atual, false);
	                mNotifyManager.notify(id, mBuilder.build());
	                
					long idPost = daoPost.inserir(feed.getPosts().get(i), idFeed);
					idsPost.add(idPost);
					//pega as categorias dos posts
					if (feed.getPosts().get(i).getCategorias()!=null) {
						//pega a categoria do feed
						for (int j = 0; j < feed.getPosts().get(i).getCategorias().size(); j++) {
							//objeto de Post ainda não tem id. Cria um objeto apenas com o id retornado do insert
							daoCategoria.inserir(feed.getPosts().get(i).getCategorias().get(j), (new Post.Builder().idPost(idPost).build()));
			                atual ++;
							mBuilder.setProgress(estimativa, atual, false);
			                mNotifyManager.notify(id, mBuilder.build());

						}
						
					}
					//pega os anexos do post
					if (feed.getPosts().get(i).getAnexos()!=null) {
						for (int j = 0; j < feed.getPosts().get(i).getAnexos().size(); j++) {
							daoAnexo.inserir(feed.getPosts().get(i).getAnexos().get(j), idPost);							
			                atual ++;
			                mBuilder.setProgress(estimativa, atual, false);
			                mNotifyManager.notify(id, mBuilder.build());
						}
					}
					//para cada post tem uma ou mais descrições
					if (feed.getPosts().get(i).getDescricao()!=null) {
						daoDescricao.inserir(feed.getPosts().get(i).getDescricao(), idPost);								
					}
					//para cada post tem uma ou mais conteudos
					if (feed.getPosts().get(i).getConteudos()!=null) {
						for (int j = 0; j < feed.getPosts().get(i).getConteudos().size(); j++) {
							daoConteudo.inserir(feed.getPosts().get(i).getConteudos().get(j), idPost);																	
			                atual ++;
			                mBuilder.setProgress(estimativa, atual, false);
			                mNotifyManager.notify(id, mBuilder.build());
						}
					}

				}
			}
			atualiza();//atualiza flag de acesso
			daoAnexo.close();
			daoCategoria.close();
			daoConteudo.close();
			daoDescricao.close();
			daoImagem.close();
			daoPost.close();
			daoFeed.close();
		}
		//TODO: COLOCAR UMA MUDANÇA DE FLAG NO FEED PARA SER ACESSÍVEL.
		//daoFeed.close();
		//TODO: JOGAR O PROCESSO DE ADIÇÃO EM BACKGROUND NUMA NOTIFICAÇÃO.
	}
}
