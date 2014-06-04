package br.com.samirrolemberg.simplerssreader.tasks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import br.com.samirrolemberg.simplerssreader.model.Anexo;
import br.com.samirrolemberg.simplerssreader.model.Categoria;
import br.com.samirrolemberg.simplerssreader.model.ExceptionMessage;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.model.SimpleFeed;
import br.com.samirrolemberg.simplerssreader.u.Executando;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;

public class AtualizarFeedTask extends AsyncTask<String, Integer, Feed> {

	private final Context context;
	//private ProgressDialog progress;

	private SyndFeed syndFeed;
	private ExceptionMessage e;
	private int id = 0;

	private Feed feed;
	
	private int estimativa = 0;
	private int atual = 0;

	private NotificationManager mNotifyManager = null;
	private NotificationCompat.Builder mBuilder = null;

	public AtualizarFeedTask(Context context, Feed feed){
		this.context = context;
		this.id = new Random().nextInt(999);//colocar parametero
		this.feed = feed;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//progress = ProgressDialog.show(getContext(), "Adicionar Feed", "Sincronizando FEED...",true,true);
		this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("Atualizando "+feed.getTitulo())
		.setContentText("Sincronizando o Feed.")
		.setSmallIcon(android.R.drawable.arrow_down_float);
		estimativa = estimativaDosFor()*2;
		Executando.ATUALIZA_FEED.put(feed.getIdFeed()+feed.getRss(), 1);
	}
	@Override
	protected Feed doInBackground(String... arg) {
		try {
			URL feedUrl = new URL(arg[0]);
			SyndFeedInput input = new SyndFeedInput();
			this.syndFeed = input.build(new InputStreamReader(feedUrl.openStream()));
			if (feed != null) {
		        mBuilder.setProgress(0, 0, false);
		        mBuilder.setContentText("Verificando Entradas Antigas.");
		        mNotifyManager.notify(id, mBuilder.build());
				return SimpleFeed.consumir(syndFeed, arg[0]);
//				Feed f = SimpleFeed.consumir(feed, arg[0]);
//				return f;
			}
		} catch (MalformedURLException e) {//problema ao acessar url, verifique a digitação! protocolo nao encontrado.
			Log.e("ADDFEED", "MalformedURLException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (IllegalArgumentException e) {
			Log.e("ADDFEED", "IllegalArgumentException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (FeedException e) {//XMl invpalido - nenhum elemento encontrado
			Log.e("ADDFEED", "FeedException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (IOException e) {//arquivo n�o encontrado
			Log.e("ADDFEED", "IOException");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		} catch (Exception e) {
			Log.e("ADDFEED", "Exception");
			Log.e("ADDFEED", e.getMessage(),e);
			Log.e("ADDFEED", e.getLocalizedMessage(),e);
			this.e = new ExceptionMessage(e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Feed result) {
		super.onPostExecute(result);
		//progress.dismiss();
		if (result!=null) {
			DAOFeed daoFeed = new DAOFeed(context);
			DAOAnexo daoAnexo = new DAOAnexo(context);
			DAOCategoria daoCategoria = new DAOCategoria(context);
			DAOConteudo daoConteudo = new DAOConteudo(context);
			DAODescricao daoDescricao = new DAODescricao(context);
			DAOImagem daoImagem = new DAOImagem(context);
			DAOPost daoPost = new DAOPost(context);
			
			//verifica se o build do feed é igual ou recente
			if (feed.getData_publicacao().before(result.getData_publicacao())) {
				//TODO: VERIFICAR COMO REMOVER ALGUNS DADOS QUE DEIXARAM DE VIR (COMO CATEGORIAS)
				//se a data do ultimo build está antes da nova data
				//atualiza os dados do feed
				daoFeed.atualiza(result, feed.getIdFeed());//não atualiza o objeto feed(Aux) que já está aqui | quando a tarefa terminar elel carregará no devido lugar
				if (result.getCategorias()!=null) {//se tem categorias
					for (Categoria categoria : result.getCategorias()) {
						long idCategoria = daoCategoria.existe(categoria, feed);//verifica se a categoria do feed existe na base
						if (idCategoria>0) {//se a categoria existe atualiza pois pode ter url
							daoCategoria.atualiza(categoria, idCategoria);
						}else{//se não existe insere a categira nova
							daoCategoria.inserir(categoria, feed);
						}
					}				
				}
				if (result.getImagem()!=null) {
					daoImagem.atualiza(result.getImagem(), feed.getIdFeed());
				}
				if (result.getPosts()!=null) {//se tem post no novo feed
					//pega as novas entradas e verifica se há alguém com data de atualização
					//se há, verifica se o link existe na base.
					//se existe pega aquele post e atualiza TUDO dele
					List<Integer> idsp = new ArrayList<Integer>();//lista de ids de post para remover da lista
					for (int i = 0; i < result.getPosts().size(); i++) {
						Post post = result.getPosts().get(i);
						if (post.getData_atualizacao()!=null) {
							//atualiza o post de a cordo como link
							long idP = daoPost.existe(post);
							Post idPost = new Post.Builder().idPost(idP).build();
							if (idPost.getIdPost()>0) {
								idsp.add(i);//remove este no futuro;
								daoPost.atualiza(post, idPost.getIdPost());//atualiza os novos dados do post
								if (post.getAnexos()!=null) {//se tem anexo no novo post a ser atualizado
									for (Anexo anexo : post.getAnexos()) {//para cada novo anexo verifica se ele existe na base
										long idAnexo = daoAnexo.existe(anexo, idPost.getIdPost());
										if (idAnexo>0) {//se existe este anexo atualiza ele
											daoAnexo.atualiza(anexo, idAnexo);
										}else{//veio mais um anexo na atualização. insere
											daoAnexo.inserir(anexo, idPost.getIdPost());
										}
									}									
								}
								if (post.getCategorias()!=null) {//se tem categorias
									//adiciona novas categorias ao post se ela não existir antes.
									for (Categoria categoria : post.getCategorias()) {
										long idCategoria = daoCategoria.existe(categoria, idPost);
										if (idCategoria>0) {//atualiza pois algumas categorias podem ter URL. Mas o nome ainda ser igual.
											daoCategoria.atualiza(categoria, idCategoria);											
										}else{
											daoCategoria.inserir(categoria, idPost);
										}
									}
									
								}
							}//else mantém ele para ser adicionado
							//daoAnexo;
							//daoCategoria;
							//daoConteudo;
							//daoDescricao;
							//daoPost;
						}
					}
					for (Integer location : idsp) {
						result.getPosts().remove(location);
					}
				}
					//se não existe deixa na lista para ser inseriro como novo
				//verifica se algum dos feed necessita ser atualizado pesquisando o post.dataatualização
				//insere novas entradas
				//atualiza os 0 para 1
				//finaliza os daos com o Databasemanager
			}else{
				//não é necessário atualizar
			}
		}else{
			Toast.makeText(getContext(), "Não encontrado: "+e.getThrowable().getMessage(), Toast.LENGTH_SHORT).show();
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

}
